(ns imgscan.db.core-test
  (:require
   [imgscan.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer [deftest is use-fixtures]]
   [next.jdbc :as jdbc]
   [imgscan.config :refer [env]]
   [mount.core :as mount]
   ;;--------------------
   [encaje.core :refer [-||]]
   [imgscan.util :refer [temp-imgfile]]))

(use-fixtures
  :once
  (fn [test-fn]
    (mount/start
     #'imgscan.config/env
     #'imgscan.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (test-fn)))

(deftest test-sql-queries
  (jdbc/with-transaction [_t-conn *db* {:rollback-only true}]
    (with-open [file (-|| .delete temp-imgfile)]
      (let [name (.getName @file)
            [{:keys [id]} & x-rest]
            ,, (db/create-image! {:imgfile name, :detect true})]
        (is (int? id))
        (is (nil? x-rest))))))
