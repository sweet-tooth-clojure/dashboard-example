(ns dashboard-example.frontend.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frame.db :as rfdb]
            [integrant.core :as ig]
            [meta-merge.core :refer [meta-merge]]

            [sweet-tooth.frontend.core.flow :as stcf]
            [sweet-tooth.frontend.load-all-handler-ns]
            [sweet-tooth.frontend.core.utils :as stcu]
            [sweet-tooth.frontend.config :as stconfig]
            [sweet-tooth.frontend.handlers :as sthandlers]
            [sweet-tooth.frontend.nav.routes.bide :as strb]
            [sweet-tooth.frontend.sync.dispatch.bide :as stsdb]
            [sweet-tooth.frontend.sync.dispatch.ajax :as stsda]
            
            [dashboard-example.frontend.components.app :as app]
            [dashboard-example.frontend.routes :as routes]
            [dashboard-example.frontend.route-lifecycles]
            [dashboard-example.frontend.handlers]
            
            [goog.events])
  (:import [goog.events EventType]))

;; treat node lists as seqs
(extend-protocol ISeqable
  js/NodeList
  (-seq [node-list] (array-seq node-list))

  js/HTMLCollection
  (-seq [node-list] (array-seq node-list)))

(def system-config
  (meta-merge stconfig/default-config
              {::stsda/sync-dispatch-fn {}
               ::stsdb/req-adapter      {:routes         (ig/ref ::routes/api-routes)
                                         :route-param-fn (fn [_ _ params]
                                                           (when-let [id (or (:db/id params) (:id params))]
                                                             {:id id}))}
               ::strb/match-route       {:routes routes/location-routes}
               ::routes/api-routes      "http://localhost\\:3010/api"}))

(defn -main []
  (rf/dispatch-sync [::stcf/init-system system-config])
  (rf/dispatch-sync [:init])
  (r/render [app/app] (stcu/el-by-id "app")))

(defonce initial-load (delay (-main)))
@initial-load

(defn stop [_]
  (when-let [system (:sweet-tooth/system @rfdb/app-db)]
    (ig/halt! system)))
