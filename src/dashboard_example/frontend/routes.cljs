(ns dashboard-example.frontend.routes
  (:require [integrant.core :as ig]
            [bide.core :as bide]
            [taoensso.timbre :as log]))

(def location-routes
  [["/" :home]])

(defn location-route-coercion
  [_ params]
  params)

(def location-router
  (bide/router location-routes))

(defn resolve-location
  [route-name & args]
  (let [url (apply bide/resolve location-router route-name args)]
    (when-not url
      (log/warn "could not resolve route" ::unknown-browser-route {:route-name route-name :args args}))
    url))

(defn api-routes
  [prefix]
  (mapv (fn [route] (update route 0 #(str prefix %)))
        [["/init" :init]
         ["/report" :reports]]))

(defmethod ig/init-key ::api-routes
  [_ prefix]
  (let [x (api-routes prefix)]
    (println "API ROUTES" (str x))
    x))
