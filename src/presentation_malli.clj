(ns presentation-malli
  (:require [java-time :as t]))

(def property-types #"^apartments|backpackers_hostels|bed_breakfasts|cabins_cottages|caravans_camping
                      |farm_stays|holiday_houses|hotels|motels|resorts|retreats|retreats_lodges
                      |self_contained_apartments|villas|wilderness_safari_retreats$")

(def valid-latitude?
  [:and :double [:>= -90] [:<= 90]])

(def valid-longitude?
  [:and :double [:>= -180] [:<= 180]])

(def valid-date? #"^[0-9]{4}-[0-9]{2}-[0-9]{2}$")

(defn date-format [date]
  (t/local-date date))

(def swagger-query [:map {:closed true}
                    [:brand {:swagger/title "Brand - either qantas or jetstar"
                             :swagger/default "qantas"}
                      string?]
                    [:region_name {:swagger/title "Region Name"
                                   :swagger/default "Melbourne, VIC, Australia"} [:string {:min 1}]]
                    [:adults {:swagger/title "Adults"
                              :swagger/default "2"} string?]
                    [:children {:optional true
                                :swagger/title "Children"
                                :swagger/default "0"} string?]
                    [:infants {:optional true
                               :swagger/title "Infants"
                               :swagger/default "0"} string?]
                    [:check_in {:swagger/title "Check In"
                                :swagger/default "2022-07-01"} string?]
                    [:check_out {:swagger/title "Check Out"
                                 :swagger/default "2022-07-02"} string?]
                    [:user_groups {:swagger/title "User Groups - QFF Members or Flight Bookers"
                                   :swagger/default "qff_member"}
                     [:enum {:error/message "should be: empty string, qff_member, flight_booker or both qff_member and flight_booker"}
                      "" "qff_member" "flight_booker" "qff_member, flight_booker" "flight_booker, qff_member"]]
                    [:min_star_rating {:optional true
                                       :swagger/title "Minimum Star Rating"
                                       :swagger/default 4.5} [:and :double [:>= 0] [:<= 5]]]
                    [:property_type {:optional true
                                     :swagger/title "Property Types - one of the property types in the swagger introduction"
                                     :swagger/default "Hotels"} string?]
                    [:ne_lng {:optional true
                              :swagger/title "North East Longitude"
                              :swagger/default 134} valid-longitude?]
                    [:ne_lat {:optional true
                              :swagger/title "North East Latitude"
                              :swagger/default 85} valid-latitude?]
                    [:sw_lng {:optional true
                              :swagger/title "South West Longitude"
                              :swagger/default 135} valid-longitude?]
                    [:sw_lat {:optional true
                              :swagger/title "South West Latitude"
                              :swagger/default 84} valid-latitude?]])

(def query-with-validation ;; here :fn allows any predicate function to be used
  [:and swagger-query
   [:fn {:error/message "Must be either qantas or jetstar"
         :error/path [:brand]}
    (fn [{:keys [brand]}] (re-matches #"^qantas|jetstar$" brand))]
   [:fn {:error/message "Must be a positive numerical string"
         :error/path [:adults]}
    (fn [{:keys [adults]}] (re-matches #"^[1-9]+$" adults))]
   [:fn {:error/message "Must be a numerical string"
         :error/path [:children]}
    (fn [{:keys [children]}] (re-matches #"^[0-9]+$" children))]
   [:fn {:error/message "Must be a numerical string"
         :error/path [:infants]}
    (fn [{:keys [infants]}] (re-matches #"^[0-9]+$" infants))]
   [:fn {:error/message "Must be a valid date e.g. '2021-10-01' "
         :error/path [:check_in]}
    (fn [{:keys [check_in]}] (re-matches valid-date? check_in))]
   [:fn {:error/message "Must be a valid date e.g. '2021-10-01' "
         :error/path [:check_out]}
    (fn [{:keys [check_out]}] (re-matches valid-date? check_out))]
   [:fn {:error/message "Check out must be after check in"
         :error/path [:check_out]}
    (fn [{:keys [check_in check_out]}] (and (not= (date-format check_in) (date-format check_out)) 
                                            (= false (t/before? (date-format check_out) (date-format check_in)))))]
   [:fn {:error/message "Must be either qff_member, flight_booker or blank"
         :error/path [:user_groups]}
    (fn [{:keys [user_groups]}] (re-matches #"^qff_member|flight_booker|" user_groups))]
   [:fn {:error/message "Must be one of the property types in the swagger introduction"
         :error/path [:property_type]}
    (fn [{:keys [property_type]}] (re-matches property-types property_type))]
   [:fn {:error/message "ne_lat must be greater than sw_lat"
         :error/path [:sw_lat]}
    (fn [{:keys [ne_lat sw_lat]}] (>= ne_lat sw_lat))]
   [:fn {:error/message "ne_lng must be greater than sw_lng"
         :error/path [:sw_lng]}
    (fn [{:keys [ne_lng sw_lng]}] (>= ne_lng sw_lng))]])
