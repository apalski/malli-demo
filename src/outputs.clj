(ns outputs
  (:require [presentation-malli]))

;; Transform Malli Query to Swagger
{:type "object", 
 :properties {:brand {:type "string", 
                      :title "Brand - either qantas or jetstar", 
                      :default "qantas"}, 
              :region_name {:type "string", 
                            :title "Region Name", 
                            :default "Melbourne, VIC, Australia"}, 
              :adults {:type "string", 
                       :title "Adults", 
                       :default "2"}, 
              :children {:type "string", 
                         :title "Children", 
                         :default "0"}, 
              :infants {:type "string", 
                        :title "Infants", 
                        :default "0"}, 
              :check_in {:type "string", 
                         :title "Check In", 
                         :default "2022-07-01"}, 
              :check_out {:type "string", 
                          :title "Check Out", 
                          :default "2022-07-02"}, 
              :user_groups {:type "string", 
                            :title "User Groups - either qff_member, flight_booker or blank", 
                            :default "qff_member"}, 
              :min_star_rating {:type "number", 
                                :format "double", 
                                :x-allOf [{:type "number", :format "double"} 
                                          {:type "number", :minimum 0} 
                                          {:type "number", :maximum 5}], 
                                :title "Minimum Star Rating", 
                                :default 4.5}, 
              :property_type {:type "string", 
                              :title "Property Types - one of the property types in the swagger introduction", 
                              :default "Hotels"}, 
              :ne_lng {:type "number", 
                       :format "double", 
                       :x-allOf [{:type "number", :format "double"} 
                                 {:type "number", :minimum -180} 
                                 {:type "number", :maximum 180}], 
                       :title "North East Longitude", 
                       :default 134}, 
              :ne_lat {:type "number", 
                       :format "double", 
                       :x-allOf [{:type "number", :format "double"} 
                                 {:type "number", :minimum -90} 
                                 {:type "number", :maximum 90}], 
                       :title "North East Latitude", 
                       :default 85}, 
              :sw_lng {:type "number", 
                       :format "double", 
                       :x-allOf [{:type "number", :format "double"} 
                                 {:type "number", :minimum -180} 
                                 {:type "number", :maximum 180}], 
                       :title "South West Longitude", 
                       :default 135}, 
              :sw_lat {:type "number", 
                       :format "double", 
                       :x-allOf [{:type "number", :format "double"} 
                                 {:type "number", :minimum -90} 
                                 {:type "number", :maximum 90}], 
                       :title "South West Latitude", 
                       :default 84}}, 
              :required [:brand 
                         :region_name 
                         :adults 
                         :check_in 
                         :check_out 
                         :user_groups]}


;; m/explain tc/invalid-lat-lng-query
{:schema [:and [:map {:closed true} 
                [:brand #:swagger{:title "Brand - either qantas or jetstar", 
                                  :default "qantas"} string?] 
                [:region_name #:swagger{:title "Region Name", 
                                        :default "Melbourne, VIC, Australia"} string?] 
                [:adults #:swagger{:title "Adults", 
                                   :default "2"} string?] 
                [:children {:optional true, 
                            :swagger/title "Children", 
                            :swagger/default "0"} string?] 
                [:infants {:optional true, 
                           :swagger/title "Infants", 
                           :swagger/default "0"} string?] 
                [:check_in #:swagger{:title "Check In", 
                                     :default "2022-07-01"} string?] 
                [:check_out #:swagger{:title "Check Out", 
                                      :default "2022-07-02"} string?] 
                [:user_groups #:swagger{:title "User Groups - either qff_member, flight_booker or blank", 
                                        :default "qff_member"} string?] 
                [:min_star_rating {:optional true, 
                                   :swagger/title "Minimum Star Rating", 
                                   :swagger/default 4.5} 
                 [:and float? [:>= 0] [:<= 5]]] 
                [:property_type {:optional true, 
                                 :swagger/title "Property Types - one of the property types in the swagger introduction", 
                                 :swagger/default "Hotels"} string?] 
                [:ne_lng {:optional true, 
                          :swagger/title "North East Longitude", 
                          :swagger/default 134} 
                 [:and float? [:>= -180] [:<= 180]]] 
                [:ne_lat {:optional true, 
                          :swagger/title "North East Latitude", 
                          :swagger/default 85} 
                 [:and float? [:>= -90] [:<= 90]]] 
                [:sw_lng {:optional true, 
                          :swagger/title "South West Longitude", 
                          :swagger/default 135} 
                 [:and float? [:>= -180] [:<= 180]]] 
                [:sw_lat {:optional true, 
                          :swagger/title "South West Latitude", 
                          :swagger/default 84} 
                 [:and float? [:>= -90] [:<= 90]]]] 
                 [:fn #:error{:message "Must be either qantas or jetstar", 
                              :path [:brand]} #function[presentation-malli/fn--16584]] 
                 [:fn #:error{:message "Region Name cannot be blank", 
                              :path [:region_name]} #function[presentation-malli/fn--16588]] 
                 [:fn #:error{:message "Must be a positive numerical string", 
                              :path [:adults]} #function[presentation-malli/fn--16592]] 
                 [:fn #:error{:message "Must be a numerical string", 
                              :path [:children]} #function[presentation-malli/fn--16596]] 
                 [:fn #:error{:message "Must be a numerical string", 
                              :path [:infants]} #function[presentation-malli/fn--16600]] 
                 [:fn #:error{:message "Must be a valid date e.g. '2021-10-01' ", 
                              :path [:check_in]} #function[presentation-malli/fn--16604]] 
                 [:fn #:error{:message "Must be a valid date e.g. '2021-10-01' ", 
                              :path [:check_out]} #function[presentation-malli/fn--16608]] 
                 [:fn #:error{:message "Check out must be after check in", 
                              :path [:check_out]} #function[presentation-malli/fn--16612]] 
                 [:fn #:error{:message "Must be either qff_member, flight_booker or blank", 
                              :path [:user_groups]} #function[presentation-malli/fn--16617]] 
                 [:fn #:error{:message "Must be one of the property types in the swagger introduction", 
                              :path [:property_type]} #function[presentation-malli/fn--16621]] 
                 [:fn #:error{:message "ne_lat must be greater than sw_lat", 
                              :path [:sw_lat]} #function[presentation-malli/fn--16625]] 
                 [:fn #:error{:message "ne_lng must be greater than sw_lng", 
                              :path [:sw_lng]} #function[presentation-malli/fn--16629]]], 
                  :value {:children "0", 
                          :region_name "Melbourne, VIC, Australia", 
                          :sw_lng 133.6, 
                          :check_out "2021-07-11", 
                          :sw_lat 86.0, 
                          :property_type "apartments", 
                          :brand "qantas", 
                          :ne_lng 190.0, 
                          :ne_lat 85.0, 
                          :check_in "2021-07-10", 
                          :min_star_rating 4.0, 
                          :user_groups "qff_member", 
                          :infants "0", 
                          :adults "2"}, 
                  :errors (#Error{:path [0 :ne_lng 2], :in [:ne_lng], :schema [:<= 180], :value 190.0} 
                           #Error{:path [11], :in [], 
                                  :schema [:fn #:error{:message "ne_lat must be greater than sw_lat", 
                                                       :path [:sw_lat]} #function[presentation-malli/fn--16625]], 
                                  :value {:children "0", 
                                          :region_name "Melbourne, VIC, Australia", 
                                          :sw_lng 133.6, 
                                          :check_out "2021-07-11", 
                                          :sw_lat 86.0, 
                                          :property_type "apartments", 
                                          :brand "qantas", 
                                          :ne_lng 190.0, 
                                          :ne_lat 85.0, 
                                          :check_in "2021-07-10", 
                                          :min_star_rating 4.0, 
                                          :user_groups "qff_member", 
                                          :infants "0", 
                                          :adults "2"}})}

