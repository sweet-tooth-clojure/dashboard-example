(ns dashboard-example.backend.query.movie
  (:require [datomic.api :as d]))

(def movie-attrs
  [:db/id
   :movie/year
   :movie/imdb
   :movie/title
   :movie/test
   :movie/clean-test
   :movie/binary
   :movie/budget
   :movie/domgross
   :movie/intgross
   :movie/budget-2013
   :movie/domgross-2013
   :movie/intgross-2013])

(defn all-movies
  [db]
  (with-meta
    (->> (d/q {:find [(list 'pull '?e movie-attrs)]
               :in '[$]
               :where '[[?e :movie/title]]}
              db)
         (map first))
    {:ent-type :movie}))
