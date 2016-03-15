(ns compojuretest.query
  (:require [compojuretest.database]
            [korma.core :refer :all]))

(defentity items)

(defn get-todos []
  (select items))

(defn add-todo [title]
  (insert items (values {:title title})))

(defn delete-todo [id]
  (delete items (where {:id [= id]})))

(defn update-todo [id title is-complete]
  (update items
          (set-fields {:title title
                       :is_complete is-complete})
          (where {:id [= id]})))

(defn get-todo [id]
  (first
   (select items
           (where {:id [= id]}))))

