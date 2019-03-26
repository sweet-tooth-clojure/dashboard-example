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
   [:report/budget-2013-min stfilter/filter-attr>= :movie/budget-2013]
   [:report/budget-2013-max stfilter/filter-attr<= :movie/budget-2013]

   [:report/domgross-2013-min stfilter/filter-attr>= :movie/domgross-2013]
   [:report/domgross-2013-max stfilter/filter-attr<= :movie/domgross-2013]

   [:report/intgross-2013-min stfilter/filter-attr>= :movie/intgross-2013]
   [:report/intgross-2013-max stfilter/filter-attr<= :movie/intgross-2013]])
