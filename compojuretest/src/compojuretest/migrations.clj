(ns compojuretest.migrations
  (:refer-clojure :exclude
                  [alter drop bigint boolean char double float time complement])
  (:use [compojuretest.database]
        [lobos migration core schema]))


(defmigration add-todo-table
  (up [] (create (table :items
                        (integer :id :primary-key :auto-inc)
                        (varchar :title 512))))
  (down [] (drop (table :items))))


(let [is-complete (table :items
                         (boolean :is_complete (default false)))]
  (defmigration add-is-complete-column
    (up [] (alter :add is-complete))
    (down [] (alter :drop is-complete))))

(defn run-migrations []
  (binding [lobos.migration/*migration-namespace* 'compojuretest.migrations]
    (migrate)))

(run-migrations)
