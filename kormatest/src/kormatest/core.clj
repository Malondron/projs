(ns kormatest.core
 (:require [korma.db :as db]
           [korma.core :as korma]
           [migratus.core :as migratus]
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

(korma/defentity blob
 (korma/database prod)
 (korma/entity-fields :color)
)

(korma/select blob (korma/where {:color "blue"}))
(korma/insert blob (korma/values {:color "blue"}))


(def config
 {:store :database
 :db {:connection-uri "jdbc:postgresql://localhost:5432/newtest?user=newtest&password=newtest"}
  })


(apply migratus/up config [(Long/parseLong "20160307000001")])
(migratus/migrate config)
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))


