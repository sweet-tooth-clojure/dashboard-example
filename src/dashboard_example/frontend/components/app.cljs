(ns dashboard-example.frontend.components.app
  (:require [re-frame.core :as rf]
            [dashboard-example.frontend.subs :as ds]
            [sweet-tooth.frontend.form.flow :as stff]
            [sweet-tooth.frontend.form.components :as stfc]))

(def fields
  [:movie/year :movie/imdb :movie/title :movie/test :movie/clean-test
   :movie/binary :movie/budget :movie/domgross :movie/intgross
   :movie/budget-2013 :movie/domgross-2013 :movie/intgross-2013])

(defn report
  []
  (let [form-path                  [:report :create]
        {:keys [field form-state]} (stfc/form form-path)]
    [:form.create-report (stfc/on-submit form-path)
     [field :text :report/title]
     [:div
      [field :number :report/budget-min]
      [field :number :report/budget-max]]
     [:div
      [field :number :report/domgross-min]
      [field :number :report/domgross-max]]

     [:div
      [field :number :report/intgross-min]
      [field :number :report/intgross-max]]]))

(defn row
  [movie]
  (into ^{:key (:db/id movie)} [:tr]
        (map (fn [field] [:td (str (field movie))]) fields)))

(defn app
  []
  (let [movies @(rf/subscribe [::ds/filtered-movies])]
    [:div 
     [report]
     [:div (str "showing " (count movies) " movies")]
     [:table
      [:tbody (map row movies)]]]))
