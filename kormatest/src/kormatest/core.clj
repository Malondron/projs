(ns kormatest.core
 (:require [korma.db :as db]
           [korma.core :as korma]
           [clojure.java.jdbc :as sql]
           [clojure.string :as string]))


(db/defdb prod
 (db/postgres 
  {:db "newtest"
   :user "newtest"
   :password "newtest"
   :host "localhost"
   :port "5432"
   :delimiters ""}))

(korma/defentity trupp
 (korma/database prod)
 (korma/entity-fields :name :email)
)

(defn find-by [field value]
  (first (korma/select trupp (korma/where {field value}) (korma/limit 1))))

(defn find-by-email [email]
  (find-by :email email))

(defn create [user]
  (korma/insert trupp (korma/values user)))




(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))


