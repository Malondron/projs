(ns user
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))


(def config 
  {:datastore (jdbc/sql-database {:connection-uri "jdbc:postgresql://localhost:5432/restful_dev?user=restful_dev&password=pass_dev"})
   :migrations (jdbc/load-resources "migrations")})



