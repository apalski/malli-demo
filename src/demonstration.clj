(ns demonstration
  (:require [malli.swagger :as swagger]
            [malli.core :as m]
            [malli.error :as me]
            [presentation-malli :as malli]
            [test-code :as tc]
            [malli.generator :as mg]
            [malli.provider :as mp]))

;; validate using Malli

(m/validate malli/query-with-validation tc/good-query)
(m/validate malli/query-with-validation tc/empty-region-name-query)

;; validate with explanation

(m/explain malli/query-with-validation tc/good-query)
(m/explain malli/query-with-validation tc/invalid-lat-lng-query)

;; validate with a simple, readable explanation
(-> malli/query-with-validation
    (m/explain tc/good-query)
    (me/humanize))

(-> malli/query-with-validation
    (m/explain tc/empty-region-name-query)
    (me/humanize))

(-> malli/query-with-validation
    (m/explain tc/invalid-lat-lng-query)
    (me/humanize))

(-> malli/query-with-validation
    (m/explain tc/invalid-star-rating-query)
    (me/humanize))

(-> malli/query-with-validation
    (m/explain tc/invalid-property-type-query)
    (me/humanize))

;; You can use a regex pattern as a 'type' instead of 'string'
;; when validation messages aren't required

(def adults-string
  [:and {:adults "2"
         :swagger/title "Adults"
         :swagger/default "2"}
   string?])

(def adults-regex
  [:and {:adults "2"
         :swagger/title "Adults"
         :swagger/default "2"}
   #"^[1-9]+$"])

(-> adults-string
    (m/explain {:adults 2})
    (me/humanize))

(-> adults-regex
    (m/explain {:adults 2})
    (me/humanize))

;; generating swagger for swagger docs

(swagger/transform malli/query)

;; generating values from schemas

;; using a seed
(mg/generate [:enum "qff_member" "flight_booker" "qff_member,flight_booker"] {:seed 101})

;; using seed and size
(mg/generate pos-int? {:seed 2 :size 100})

;; using regex
(mg/generate [:re #"^apartments|backpackers_hostels|bed_breakfasts$"] {:seed 6 :size 101})

;; using a random schema
(mg/generate malli/query {:seed 10 :size 5})
;; using a targeted schema
(mg/generate malli/query-for-generation {:seed 10 :size 5})

;; infer a schema from samples
(def samples
  [{:id          "2345"
    :name        "It's a hotel"
    :description "I'm optional"
    :longitude   135
    :latitude    78
    :brand       "qantas"}
   {:id        "3456"
    :name      "It's a second hotel"
    :longitude 145
    :latitude  83
    :brand     "jetstar"}])

(mp/provide samples)
