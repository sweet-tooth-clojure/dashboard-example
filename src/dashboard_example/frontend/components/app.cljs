(ns dashboard-example.frontend.components.app
  (:require [re-frame.core :as rf]
            [dashboard-example.frontend.subs :as ds]
            [dashboard-example.frontend.components.reports :as dr]))

(defprotocol TableFormat
  (format-cell [x]))

(extend-protocol TableFormat
  cljs.core/Keyword
  (format-cell [x] (name x))

  number
  (format-cell [x] (.toLocaleString x))

  string
  (format-cell [x] x)

  object
  (format-cell [x] x)

  boolean
  (format-cell [x] (if x "yes" "no"))

  nil
  (format-cell [x] nil))

(def attrs
  [:movie/year :movie/imdb :movie/title :movie/clean-test :movie/binary
   :movie/budget-2013 :movie/domgross-2013 :movie/intgross-2013])

(defn row
  [movie]
  (into ^{:key (:db/id movie)} [:tr]
        (map (fn [attr] [:td (format-cell (attr movie))]) attrs)))

(defn app
  []
  (let [movies @(rf/subscribe [::ds/filtered-movies])]
    [:div
     [dr/reports]
     [:div.data (str "showing " (count movies) " movies")
      [:table
       [:thead
        [:tr (map (fn [attr] ^{:key attr} [:th (name attr)]) attrs)]]
       [:tbody (map row movies)]]]]))
