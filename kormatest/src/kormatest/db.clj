(ns kormatest.db
 (:require [korma.db :as db]
           [environ.core :as env]
           [clojure.string :as string]))



(db/defdb prod
  (db/postgres 
   {:db (env/env :database)
    :user (env/env :db-user)
    :password (env/env :password) 
    :host "localhost"
    :port "5432"
    :delimiters ""}))




