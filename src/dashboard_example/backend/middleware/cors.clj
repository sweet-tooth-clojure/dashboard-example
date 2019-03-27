(ns dashboard-example.backend.middleware.cors
  (:require [integrant.core :as ig]))

(defmethod ig/init-key ::wrap-cors [_ config]
  (fn [handler]
    (fn [req]
      (let [headers {"Access-Control-Allow-Origin" "http://localhost:3000"
                     "Access-Control-Allow-Methods" "GET, PUT, POST, DELETE, OPTIONS"
                     "Access-Control-Allow-Headers" "Content-Type, *"
                     "Access-Control-Allow-Credentials" "true"}]
        (if (= (:request-method req) :options)
          {:status 200 :headers headers :body "preflight complete"}
          (-> (handler req)
              (update :headers merge headers)))))))
