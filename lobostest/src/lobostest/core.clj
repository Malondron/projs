(ns lobostest.core
  (:refer-clojure :exclude
                  [alter drop bigint boolean char double float time complement])
  (:use 
        [lobos connectivity core schema]))



(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :user "test"
   :password ""
   :subname "//localhost:5432/test-db"})

(open-global db)
