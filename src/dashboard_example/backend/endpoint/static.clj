(ns dashboard-example.backend.endpoint.static
  (:require [compojure.core :refer :all]
            [ring.util.response :as resp]
            [sweet-tooth.endpoint.liberator :as el]
            [integrant.core :as ig]))

(def app-paths ["/"
                "/query/*"
                "/report/*"])

(defmethod ig/init-key :dashboard-example.backend.endpoint/static [_ options]
  
  (apply routes
         ;; load the single page app
         (map (fn [path] (GET path [] (el/html-resource "index.html")))
              app-paths)))
