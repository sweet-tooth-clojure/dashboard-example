(ns dashboard-example.backend.data
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))


(defn load-data
  []
  (->> "data/bechdel-movies.csv"
       io/resource
       io/reader
       csv/read-csv
       doall
       (drop 1)))

(def fields
  [:movie/year :movie/imdb :movie/title :movie/test :movie/clean-test
   :movie/binary :movie/budget :movie/domgross :movie/intgross
   :movie/budget-2013 :movie/domgross-2013 :movie/intgross-2013])

(defn update-vals
  "Takes a map to be updated, x, and a map of
  {[k1 k2 k3] update-fn-1
   [k4 k5 k6] update-fn-2}
  such that such that k1, k2, k3 are updated using update-fn-1
  and k4, k5, k6 are updated using update-fn-2"
  [x update-map]
  (reduce (fn [x [keys update-fn]]
            (reduce (fn [x k] (update x k update-fn))
                    x
                    keys))
          x
          update-map))

(defn parse-data
  [data]
  (map (fn [row]
         (-> (zipmap fields (map row [0 1 2 3 4 5 6 7 8 10 11 12]))
             (update-vals {[:movie/year :movie/budget :movie/domgross :movie/domgross-2013
                            :movie/budget-2013 :movie/intgross :movie/intgross-2013]
                           #(Long. %)

                           [:movie/binary] #(= "PASS" %)
                           [:movie/clean-test :movie/test] keyword})))
       data))
