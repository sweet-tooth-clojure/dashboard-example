(ns dashboard-example.backend.endpoint.report
  (:require [integrant.core :as ig]
            [dashboard-example.backend.endpoint.common :as ec]
            [datomic.api :as d]
            [sweet-tooth.endpoint.datomic.liberator :as ed]
            [sweet-tooth.endpoint.liberator :as el]))

(defn decisions [_]
  {:create {:post!
            (fn [ctx]
              (-> @(d/transact (ed/conn ctx)
                               [(assoc (el/params ctx) :db/id (d/tempid :db.part/user))])
                  (el/->ctx :result)))

            :handle-created
            (fn [ctx]
              (let [db (ed/db-after ctx)]
                [(-> db
                     (d/pull ["*"] (:db/id (ed/created-entity ctx)))
                     (with-meta {:ent-type :report})
                     ec/format-ent)]))}})


(def endpoint (ec/endpoint "/api/report" decisions))

(defmethod ig/init-key :dashboard-example.backend.endpoint/report [_ options]
  (endpoint options))
