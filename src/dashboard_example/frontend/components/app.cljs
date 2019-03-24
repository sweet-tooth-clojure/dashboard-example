(ns dashboard-example.frontend.components.app
  (:require [re-frame.core :as rf]
            [dashboard-example.frontend.subs :as ds]))

(def fields
  [:movie/year :movie/imdb :movie/title :movie/test :movie/clean-test
   :movie/binary :movie/budget :movie/domgross :movie/intgross
   :movie/budget-2013 :movie/domgross-2013 :movie/intgross-2013])

(defn row
  [movie]
  (into ^{:key (:db/id movie)} [:tr]
        (map (fn [field] [:td (field movie)]) fields)))

(defn app
  []
  [:div "APP!"
   [:table
    [:tbody
     (map row @(rf/subscribe [::ds/movies]))]]])
