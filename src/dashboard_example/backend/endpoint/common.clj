(ns dashboard-example.backend.endpoint.common
  (:require [sweet-tooth.endpoint.liberator :as el]
            [datomic.api :as d]))

(defn endpoint
  [route decisions]
  (el/endpoint route
               decisions
               (fn [ctx component]
                 (assoc ctx
                        :db {:conn (d/connect (:uri (:db component)))}
                        :auth-id-key :db/id))))
