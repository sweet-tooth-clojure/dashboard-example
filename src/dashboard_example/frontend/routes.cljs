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

(def api-routes
  [["/init" :init]])
