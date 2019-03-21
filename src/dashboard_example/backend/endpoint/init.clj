(ns dashboard-example.backend.endpoint.init
  (:require [dashboard-example.backend.endpoint.common :as ec]
            [sweet-tooth.endpoint.datomic.liberator :as ed]
            [integrant.core :as ig]))

(defn decisions [_]
  {:list {:handle-ok (fn [ctx] [])}})

(def endpoint (ec/endpoint "/init" decisions))

(defmethod ig/init-key :dashboard-example.backend.endpoint/init [_ options]
  (endpoint options))
