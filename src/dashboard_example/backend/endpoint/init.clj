(ns dashboard-example.backend.endpoint.init
  (:require [sweet-tooth.endpoint.datomic.liberator :as ed]
            [integrant.core :as ig]
            [dashboard-example.backend.endpoint.common :as ec]
            [dashboard-example.backend.query.movie :as qm]))

(defn decisions [_]
  {:list {:handle-ok (fn [ctx]
                       (-> (qm/all-movies (ed/db ctx))
                           (with-meta {:ent-type :movie})
                           (ec/format-ent)
                           vector))}})

(def endpoint (ec/endpoint "/init" decisions))

(defmethod ig/init-key :dashboard-example.backend.endpoint/init [_ options]
  (endpoint options))
