(ns dashboard-example.frontend.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub ::movies
  (fn [db _]
    (->> (get-in db [:entity :movie])
         (vals)
         (sort-by :movie/title))))
