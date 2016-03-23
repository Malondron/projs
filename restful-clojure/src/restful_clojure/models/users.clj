(ns restful-clojure.models.users
  (:use restful-clojure.db
;        restful-clojure.db
    )
  (:require [clojure.java.jdbc :as sql]
            [clojure.tools.logging :as log]
;            [restful-clojure.entities :as e]
 ))

(defn find-all [& [trans]] 
  (sql/query (or trans db) "SELECT * FROM users"))

(defn find-by [field value & [trans]]
  (first 
   (sql/query (or trans db) (str  "SELECT * FROM users WHERE " (name field) "='" value "' LIMIT 1")
           )))

(defn count-users [& [trans]]
  (count  (sql/query (or trans db) "SELECT * FROM users")))

(defn delete-all [& [trans]]
  (when (> (count-users) 0)
    (log/info "Deleting users...")
    (sql/query (or trans db) "DELETE FROM users")))

(defn find-by-id [id & [trans]]
  (find-by :id id trans))

(defn for-list [listdata & [trans]]
  (find-by-id (listdata :user_id) trans))

(defn find-by-email [email & [trans]]
  (find-by :email email trans))
 
(defn create [user & [trans]]
  (let [u-keys (vec (keys user))
        u-vals (vec (vals user))]
    (log/info "creating user...")
    (sql/insert! (or trans db) :users u-keys u-vals) (log/info "created user...")
    ))


(defn update-user [user & [trans]]
  (sql/update! (or trans db) :users (dissoc user :id) ["id = ?" (user :id)]))


(defn delete-user [user & [trans]]
  (sql/delete! (or trans db) :users ["id = ?" (user :id)]))
