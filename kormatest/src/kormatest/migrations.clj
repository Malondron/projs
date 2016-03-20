(ns kormatest.migrations
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))


(defn load-config []
  {:datastore (jdbc/sql-database "jdbc:postgresql://localhost:5432/newtest?user=newtest&password=newtest")
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))
