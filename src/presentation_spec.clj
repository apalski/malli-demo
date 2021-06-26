(ns presentation-spec
  (:require [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [java-time :as t]))

(defn days-from-today [n]
  (t/format "yyyy-MM-dd"
            (t/plus (t/local-date) (t/days n))))

(s/def ::brand (st/create-spec {:spec            string?
                                :description     "Brand (i.e qantas/jetstar)"
                                :swagger/title   "Brand"
                                :swagger/default "qantas"}))

(s/def ::region_name (st/create-spec {:spec          string?
                                      :description   "Either region_id or region_name, iata_code or the bounding box query params are required"
                                      :swagger/title "Region name"}))

(s/def ::adults (st/create-spec {:spec            string?
                                 :description     "Number of adults"
                                 :swagger/title   "Adults"
                                 :swagger/default "2"}))

(s/def ::children (st/create-spec {:spec            string?
                                   :description     "Number of children"
                                   :swagger/title   "Children"
                                   :swagger/default "0"}))

(s/def ::infants (st/create-spec {:spec            string?
                                  :description     "Number of infants"
                                  :swagger/title   "Infants"
                                  :swagger/default "0"}))

(s/def ::check_in (st/create-spec {:spec            string?
                                   :description     "Checkin date"
                                   :swagger/title   "Checkin"
                                   :swagger/default (days-from-today 1)}))

(s/def ::check_out (st/create-spec {:spec            string?
                                    :description     "Checkout date"
                                    :swagger/title   "Checkout"
                                    :swagger/default (days-from-today 2)}))

(s/def ::user_groups (st/create-spec {:spec            string?
                                      :description     "The user groups this request is made for (i.e flight_booker/member)"
                                      :swagger/title   "User groups"
                                      :swagger/default "qff_member"}))

(s/def ::min_star_rating (st/create-spec {:spec          number?
                                          :description   "Minimum star rating"
                                          :swagger/title "Minimum star rating"}))

(s/def ::property_type (st/create-spec {:spec          string?
                                        :description   "Property type (i.e. hotels/motels/apartments/resorts)"
                                        :swagger/title "Property type"}))

;; bounding box query params
(def double-number-pattern "^-?[0-9]+(\\.[0-9]+)?$")
(s/def ::ne_lng (st/create-spec {:spec            (s/and string? (partial re-matches (re-pattern double-number-pattern)))
                                 :description     "Bounding box parameter
                                               Either region_id or region_name, iata_code or the bounding box parameters are required"
                                 :swagger/title   "North east longitude"
                                 :swagger/pattern double-number-pattern}))

(s/def ::ne_lat (st/create-spec {:spec            (s/and string? (partial re-matches (re-pattern double-number-pattern)))
                                 :description     "Bounding box parameter
                                               Either region_id or region_name, iata_code or the bounding box parameters are required"
                                 :swagger/title   "North east latitude"
                                 :swagger/pattern double-number-pattern}))

(s/def ::sw_lng (st/create-spec {:spec            (s/and string? (partial re-matches (re-pattern double-number-pattern)))
                                 :description     "Bounding box parameter
                                               Either region_id or region_name, iata_code or the bounding box parameters are required"
                                 :swagger/title   "South west longitude"
                                 :swagger/pattern double-number-pattern}))

(s/def ::sw_lat (st/create-spec {:spec            (s/and string? (partial re-matches (re-pattern double-number-pattern)))
                                 :description     "Bounding box parameter
                                               Either region_id or region_name, iata_code or the bounding box parameters are required"
                                 :swagger/title   "South west latitude"
                                 :swagger/pattern double-number-pattern}))

(s/def ::query (st/create-spec
                {:spec          (s/keys :req-un [::brand
                                                 ::region_name
                                                 ::adults
                                                 ::children
                                                 ::infants
                                                 ::check_in
                                                 ::check_out
                                                 ::user_groups]
                                        :opt-un [::min_star_rating
                                                 ::property_type
                                                 ::ne_lng
                                                 ::ne_lat
                                                 ::sw_lng
                                                 ::sw_lat])
                 :description   "The query parameters for the availability request"
                 :swagger/title "Query"}))