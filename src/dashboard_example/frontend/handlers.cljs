(ns dashboard-example.frontend.handlers
  (:require [re-frame.core :as rf]
            [re-frame.db :as rdb]
            
            [sweet-tooth.frontend.core.flow :as stcf]
            [sweet-tooth.frontend.sync.flow :as stsf]
            [sweet-tooth.frontend.nav.flow :as stnf]))

(rf/reg-event-fx :init
  [rf/trim-v]
  (fn [cofx [config]]
    (stsf/sync-event-fx cofx [:get :init {:on-success [::init-success]}])))

;; ensure that nav dispatch happens after initial data is loaded,
;; because some operations depend on that initial data
(rf/reg-event-fx ::init-success
  [rf/trim-v]
  (fn [cofx [response]]
    (-> (select-keys cofx [:db])
        (update :db stcf/update-db [response])
        (assoc :dispatch [::stnf/dispatch-current]))))
