(ns restful-clojure.entities
  (:use korma.core
        restful-clojure.db))

(declare users lists)

(defenity users
  (pk :id)
  (table :users)
  (has-many lists)
  (entity-fields :name :email))

(defentity lists
  (pk :id)
  (table :lists)
  (belongs-to users {:fk :user_id})
  (entity-fields :title))
