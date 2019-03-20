(set-env!
  :source-paths #{"src" "test" "dev/src"}
  :resource-paths #{"resources" "dev/resources"}
  :dependencies '[[org.clojure/clojure "1.10.0"]
                  [org.clojure/test.check "0.9.0" :scope "test"]
                  [org.clojure/clojurescript "1.10.439"]
                  [adzerk/boot-cljs "RELEASE" :scope "test"]
                  [com.taoensso/timbre "4.10.0"]

                  [ring "1.7.1" :exclusions [org.clojure/tools.namespace]]
                  [liberator "0.14.1"]
                  [com.datomic/datomic-free "0.9.5344" :exclusions [com.google.guava/guava]]
                  [com.flyingmachine/datomic-booties "0.1.7"]
                  [com.flyingmachine/datomic-junk "0.2.3"]
                  [com.flyingmachine/liberator-unbound "0.1.1"]
                  [compojure "1.5.0"]
                  [medley "0.7.1"]
                  [org.mindrot/jbcrypt "0.3m"]
                  [reifyhealth/specmonstah "2.0.0-alpha-1", :scope "test"]

                  ;; TODO this is gross
                  [nrepl "0.6.0" :scope "test"]
                  [cider/cider-nrepl "0.21.0" :scope "test"]
                  [refactor-nrepl "2.4.0" :scope "test"]

                  ;; duct
                  [duct/core "0.7.0"]
                  [duct/module.logging "0.4.0"]
                  [duct/module.web "0.7.0"]
                  [integrant "0.7.0"]

                  [sweet-tooth/describe "0.1.0"]
                  [sweet-tooth/sweet-tooth-endpoint "0.4.0"]
                  [sweet-tooth/sweet-tooth-workflow "0.2.6"]])

(load-data-readers!)

(require '[adzerk.boot-reload :refer [reload]]
         '[sweet-tooth.workflow.tasks :refer :all]
         '[com.flyingmachine.datomic-booties.tasks :refer [migrate-db create-db delete-db bootstrap-db recreate-db]]
         '[com.flyingmachine.datomic-junk :as dj]
         '[datomic.api :as d]
         '[clojure.spec.gen.alpha :as gen]
         '[clojure.spec.alpha :as s]
         '[adzerk.boot-cljs :refer [cljs]]
         '[dev]
         '[integrant.repl :as ir]
         '[integrant.repl.state :as irs]
         '[clojure.tools.namespace.repl :as tnsr])

(tnsr/disable-unload!)

(let [db     (:sweet-tooth.endpoint.datomic/connection (dev/prep))
      db-uri (select-keys db [:uri])]
  (task-options!
    dev  {:no-cljs true}
    build {:version "0.1.0"
           :project 'dashboard-example
           :main    'dashboard-example.core
           :file    "app.jar"}
    reload-integrant {:prep-fn 'dev/prep}
    
    create-db    db-uri
    delete-db    db-uri
    migrate-db   db
    bootstrap-db db
    recreate-db  db))
