(ns kormatest.entities
  (:use korma.core
        kormatest.db))

(declare users lists)

(defentity users
  (pk :id)
  (table :users)
  (has-many lists)
  (entity-fileds :name :email))

(defentity lists
  (pk :id)
  (table :lists)
  (belongs-to users {:fk :user_id})
  (entity-fileds :title))

(defentity trupp
 (database prod)
 (entity-fields :name :email)
)
