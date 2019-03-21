(ns dashboard-example.frontend.route-lifecycles
  (:require [re-frame.core :as rf]
            [clojure.walk :as walk]
            [medley.core :as medley]

            [dashboard-example.frontend.components.home :as h]
            
            [sweet-tooth.frontend.core.utils :as stcu]
            [sweet-tooth.frontend.paths :as paths]
            [sweet-tooth.frontend.nav.flow :as stnf]
            [sweet-tooth.frontend.nav.utils :as stnu]
            [sweet-tooth.frontend.form.flow :as stff]))

(def ignore-forms #{[:preferences :update]})

(defn flatten-forms
  "creates a flat map of form paths to forms"
  [forms]
  (walk/postwalk (fn [x]
                   (if (vector? x)
                     (let [[k v] x]
                       (cond (:buffer v)
                             [[k] v]

                             (and (map? v) (vector? (first (keys v))))
                             (medley/map-keys #(into [k] %) v)

                             :else
                             [k v]))
                     
                     x))
                 forms))

(defn dirty-forms?
  [db & [ignore-keys]]
  (let [current-forms (apply dissoc (flatten-forms (get-in db (paths/full-path :form))) ignore-forms)]
    (some (fn [f] (not (stcu/projection? (apply dissoc
                                                (->> (:buffer f)
                                                     (medley/map-vals #(if (= % "") nil %))
                                                     (medley/filter-vals identity))
                                                ignore-keys)
                                         (:base f))))
          (vals current-forms))))

(defn form-confirmation
  [& [ignore-keys]]
  (fn [db]
    (or (not (dirty-forms? db ignore-keys))
        (js/confirm "If you leave this page you will discard your work in progress. Do you want to continue and discard your work?"))))

;; lists topics
(defmethod stnf/route-lifecycle :home
  [{:keys [params]}]
  {:param-change (fn [_])
   :can-exit?    (form-confirmation)
   :can-unload?  (complement dirty-forms?)
   :components   {:main     [h/component]
                  :main-nav nil}})
