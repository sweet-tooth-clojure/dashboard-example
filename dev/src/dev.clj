(ns dev
  (:require [clojure.java.io :as io]
            [duct.core :as duct]))

(duct/load-hierarchy)

(defn read-config []
  (duct/read-config (io/resource "dashboard-example/config.edn")))

(defn prep []
  (duct/prep-config (read-config) [:duct.profile/dev]))
