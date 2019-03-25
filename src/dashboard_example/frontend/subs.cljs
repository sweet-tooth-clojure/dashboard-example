(ns dashboard-example.frontend.subs
  (:require [re-frame.core :as rf]
            [sweet-tooth.frontend.filter.flow :as stfilter]))

(rf/reg-sub ::movies
  (fn [db _]
    (->> (get-in db [:entity :movie])
         (vals)
         (sort-by :movie/title))))


(stfilter/reg-filtered-sub
  ::filtered-movies
  ::movies
  [:report :create]
  [[:report/title stfilter/filter-query :movie/title]
   [:report/budget-min stfilter/filter-attr>= :movie/budget]
   [:report/budget-max stfilter/filter-attr<= :movie/budget]

   [:report/domgross-min stfilter/filter-attr>= :movie/domgross]
   [:report/domgross-max stfilter/filter-attr<= :movie/domgross]

   [:report/intgross-min stfilter/filter-attr>= :movie/intgross]
   [:report/intgross-max stfilter/filter-attr<= :movie/intgross]])
