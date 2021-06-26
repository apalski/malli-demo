(ns presentation-spec-validation
  (:require [java-time :as t]
            [clojure.string :as s]))

(def bounding-box-err "The latitude must be a number between -90 and 90 and the longitude between -180 and 180. 'ne_lat' and 'sw_lat' (top and bottom) cannot be the same. 'ne_lat' should be greater than 'sw_lat'. 'ne_lng' should be greater than 'sw_lng'")

(def property-types #{"apartments" "backpackers_hostels" "bed_breakfasts" "cabins_cottages" "caravans_camping" "farm_stays"
                      "holiday_houses" "hotels" "motels" "resorts" "retreats" "retreats_lodges" "self_contained_apartments"
                      "villas" "wilderness_safari_retreats"})

(defn parse-double [s]
  (if (double? s)
    s
    (try (Double/parseDouble s)
         (catch Exception e))))

(defn valid-adults? [v]
  (and (string? v)
       (re-matches #"^[1-9]$" v)))

(defn valid-dependents? [v]
  (and (string? v)
       (re-matches #"^[0-9]$" v)))

(defn valid-latitude?
  "The latitude must be a number between -90 and 90"
  [v]
  (when-let [v (parse-double v)]
    (<= -90 v 90)))

(defn valid-longitude?
  "The longitude between -180 and 180"
  [v]
  (when-let [v (parse-double v)]
    (<= -180 v 180)))

(defn validate-latitude-points [ne_lat sw_lat]
  (when (and ne_lat sw_lat)
    (let [ne_lat (parse-double ne_lat)
          sw_lat (parse-double sw_lat)]
      (> ne_lat sw_lat))))

(defn validate-longitude-points
  "Ignores edge case where a bounding box sits across the 0deg meridean
  e.g. ne_lng -179 sw_lng 179, the box would either be over the ocean or
  remote east Russia"
  [ne_lng sw_lng]
  (when (and ne_lng sw_lng)
    (let [ne_lng (parse-double ne_lng)
          sw_lng (parse-double sw_lng)]
      (> ne_lng sw_lng))))

(defn valid-rating? [v]
  (try
    (let [star-rating (Double/parseDouble v)]
      (<= 0 star-rating 5))
    (catch Exception _)))

(defn valid-date? [d]
  (try
    (t/local-date d)
    (catch Throwable _ false)))

(defn days-from-now [n]
  (t/plus (t/local-date) (t/days n)))

(defn today-or-later? [d]
  (try
    (t/after? (t/local-date d) (days-from-now -1))
    (catch Throwable _)))

(defn add-error [m k message]
  (update-in m [:errors k] conj message))

(defn required-validator [m f k error]
  (if (not (f (get m k)))
    (add-error m k error)
    m))

(defn validator [m f k error]
  (if (get m k)
    (required-validator m f k error)
    m))

(defn check-in-before-check-out? [{:keys [check_in check_out]}]
  (try (t/before? (t/local-date check_in)
                  (t/local-date check_out))
       (catch Exception _)))

(defn validate-date-sequence [params]
  (if (check-in-before-check-out? params)
    params
    (add-error params :check_in "check_in must be before check_out")))

(defn validate-brand [params]
  (validator params #{"qantas" "jetstar"} :brand "is not valid"))

(defn validate-region-name [params]
  (validator params (complement clojure.string/blank?) :region_name "cannot be blank"))

(defn validate-adults [params]
  (validator params valid-adults? :adults "should be a number between 1 and 9"))

(defn validate-infants [params]
  (validator params valid-dependents? :infants "should be a number between 0 and 9"))

(defn validate-children [params]
  (validator params valid-dependents? :children "should be a number between 0 and 9"))

(defn validate-star-rating [params]
  (validator params valid-rating? :min_star_rating "should be a floating point between 0 and 5"))

(defn validate-check-in [params]
  (-> params
      (validator valid-date? :check_in "is not a valid date")
      (validator today-or-later? :check_in "is before today")))

(defn validate-check-out [params]
  (validator params valid-date? :check_out "is not a valid date"))

(defn validate-property-type [params]
  (validator params
             #(every? property-types (s/split % #","))
             :property_type
             "is not valid"))

(defn validate-required-param [r k]
  (required-validator r some? k "is required"))

(defn validate-required-params [params required-keys]
  (reduce validate-required-param
          params
          required-keys))

(defn validate-bounding-box [{:keys [ne_lng ne_lat sw_lng sw_lat] :as params}]
  (if (or ne_lng ne_lat sw_lat sw_lng)
    (if (and (valid-latitude? ne_lat)
             (valid-latitude? sw_lat)
             (valid-longitude? ne_lng)
             (valid-longitude? sw_lng)
             (not= ne_lat sw_lat)
             (validate-latitude-points ne_lat sw_lat)
             (validate-longitude-points ne_lng sw_lng))
      params
      (add-error params :bounding_box bounding-box-err))
    params))

(defn validate-query [query]
  (-> (assoc query :errors {})
      (validate-required-params [:region_name :brand :check_in :check_out :adults])
      validate-brand
      validate-region-name
      validate-check-in
      validate-check-out
      validate-date-sequence
      validate-brand
      validate-adults
      validate-children
      validate-infants
      validate-star-rating
      validate-property-type
      validate-bounding-box
      :errors))
