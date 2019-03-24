(ns dashboard-example.backend.endpoint.common
  (:require [sweet-tooth.endpoint.liberator :as el]
            [datomic.api :as d]
            [sweet-tooth.endpoint.utils :as eu]))

(defn endpoint
  [route decisions]
  (el/endpoint route
               decisions
               (fn [ctx component]
                 (assoc ctx
                        :db {:conn (d/connect (:uri (:db component)))}
                        :auth-id-key :db/id))))

(defn format-ent
  "Organize ent"
  [e & [id-key]]
  {:entity (eu/format-ent e (or id-key :db/id))})
