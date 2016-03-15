(ns lobostest.database
  (:require [korma.db :as korma]
            [lobos.connectivity :as lobos]))

(def db-connection-info
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :user "db-user"
   :password "SuperSecretPassword"
   :subname "//localhost:5432/todo"})

(korma/defdb db db-connection-info)
(lobos/open-global db-connection-info)
