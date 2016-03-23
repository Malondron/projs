(ns restful-clojure.db
  (:require [clojure.java.jdbc :as sql]
            [environ.core :as env]
            )
  (:import (java.util UUID))
  (:refer-clojure :exclude [find]))

(def db (env/env :database-url))
(def db "jdbc:postgresql://localhost:5432/restful_test?user=restful_test&password=pass_test")


(defn update-status [id args]
  (sql/with-connection db
    (sql/update-values :instances ["id = ?" id] args)))

(defn status [owner project status & [args]]
  (sql/with-connection db
    (sql/with-query-results [{:keys [id]}]
      ["SELECT * FROM instances WHERE owner = ? AND project = ? ORDER BY at DESC"
       owner project]
      (update-status id (merge {:status status} args)))))

(defn invite [owner project invitee]
  (sql/with-query-results [instance]
    ["SELECT * FROM instances WHERE project = ? and owner = ? ORDER BY at DESC"
     project owner]
    (sql/insert-record :invites {:instance_id (:id instance) :invitee invitee})))

(defn find [username project-name & [include-halted?]]
  (sql/with-connection db
    (sql/with-query-results [instance]
      [(str "SELECT * FROM instances WHERE owner = ? AND project = ?"
            (if-not include-halted? (str " AND status <> 'halted'"
                                         " AND status <> 'halting'"
                                         " AND status <> 'failed'"
                                         " AND status <> 'error'"
                                         " AND status <> 'timeout'"
                                         " AND status <> 'unconfigured'"
                                         " AND status <> 'unauthorized'"))
            " ORDER BY at DESC") username project-name]
      ;; whatever I suck at sql
      (if instance
        (sql/with-query-results invitees
          ["SELECT * FROM invites WHERE instance_id = ?" (:id instance)]
          (assoc instance
            :invitees (mapv :invitee invitees)))))))

(defn find-all [username]
  (sql/with-connection db
    (sql/with-query-results instances
      ["SELECT * FROM instances WHERE owner = ? ORDER BY at DESC" username]
      (doall instances))))

(defn by-token [shutdown-token]
  (sql/with-connection db
    (sql/with-query-results [instance]
      ["SELECT * FROM instances WHERE shutdown_token = ?" shutdown-token]
      instance)))

;; migrations

(defn initial-schema []
  (sql/db-do-commands db
                      (sql/create-table-ddl :users
                                            [:id :serial "PRIMARY KEY"]
                                            [:name :varchar "NOT NULL"]
                                            [:email :varchar :unique "NOT NULL"]
                                            [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]
                                            [:updated_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]
                                            )
                      (sql/create-table-ddl "lists"
                                            [:id :serial "PRIMARY KEY"]
                                            [:user_id :integer :references "users" "NOT NULL"]
                                            [:instance_id :integer "NOT NULL"]
                                            [:at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))

(defn add-instance-id []
  (sql/do-commands "ALTER TABLE instances ADD COLUMN instance_id VARCHAR"))

(defn add-shutdown-token []
  (sql/do-commands "ALTER TABLE instances ADD COLUMN shutdown_token VARCHAR"))

(defn add-dns []
  (sql/do-commands "ALTER TABLE instances ADD COLUMN dns VARCHAR"))

(defn add-region []
  (sql/do-commands "ALTER TABLE instances ADD COLUMN region VARCHAR"))

;; migrations mechanics

#dbg
(defn run-and-record [migration]
  (println "Running migration:" (:name (meta migration)))
  (migration)
  (sql/insert! db :migrations [:name :created_at]
                     [(str (:name (meta migration)))
                      (java.sql.Timestamp. (System/currentTimeMillis))]))

#dbg
(defn test-mig [& migrations]
  (try
    (when (not (migrated?))
      (sql/db-do-commands db
                          (sql/create-table-ddl
                           :migrations
                           [:name :varchar "NOT NULL"]
                           [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))
    (sql/with-db-transaction [dconn db]
      (let [migs (sql/query dconn ["SELECT name FROM migrations"])
            has-run? (set (map :name migs))]
        (has-run? (str (:name (meta (first migrations)))))
        (doseq [m migrations
                :when (not (has-run? (str (:name (meta m)))))]
          (run-and-record m))

        ))
        (catch Exception e (str "caught: " (.getMessage e)))   ))

(defn test-blaj []    
  (sql/with-db-transaction [dconn db]
      (sql/query dconn ["SELECT name FROM migrations"]
                                                      :row-fn #(set (map :name (% :name)))))
 
   )

(test-blaj)
(test-mig #'initial-schema)

(defn migrated? []
  (-> (sql/query db [(str "select count(*) from information_schema.tables "
                          "where table_name='migrations'")])
      first :count pos?))

#dbg
(defn migrate [& migrations]
  (sql/db-do-commands db
                      (sql/create-table-ddl :migrations
                                            [:name :varchar "NOT NULL"]
                                            [:created_at :timestamp
                                             "NOT NULL"  "DEFAULT CURRENT_TIMESTAMP"])
                          (sql/with-db-transaction [d-conn db]
                            (let [has-run? (sql/query d-conn ["SELECT name FROM migrations"]
                                                      :row-fn #(set (map :name (% :name))))]
                              (doseq [m migrations
                                      :when (not (has-run? (str (:name (meta m)))))]
                                (run-and-record m))
  ))))



(comment (defn -main []
           (test-mig #'initial-schema)
           
           
           ))
