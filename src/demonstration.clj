(ns demonstration
  (:require [malli.swagger :as swagger]
            [malli.core :as m]
            [malli.error :as me]
            [presentation-malli :as malli]
            [test-code :as tc]))

(swagger/transform malli/swagger-query)

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
