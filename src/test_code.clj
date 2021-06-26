(ns test-code)

(def good-query
  {:brand "qantas"
   :region_name "Melbourne, VIC, Australia"
   :adults "2"
   :children "0"
   :infants "0"
   :min_star_rating 4.0
   :property_type "apartments"
   :user_groups "qff_member"
   :sw_lat 84.0
   :sw_lng 133.6
   :ne_lat 85.0
   :ne_lng 134.1
   :check_in "2021-07-10"
   :check_out "2021-07-11"})

(def empty-region-name-query
  (assoc good-query :region_name ""))

(def invalid-lat-lng-query
  (assoc good-query :sw_lat 86.0 :ne_lng 190.0))

(def invalid-star-rating-query
  (assoc good-query :min_star_rating -1.0))

(def invalid-property-type-query
  (assoc good-query :property_type "small house"))