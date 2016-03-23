(ns restful-clojure.users-test
  (:use clojure.test
       
        restful-clojure.db
        restful-clojure.test-core)
  (:require [restful-clojure.models.users :as users]
            [clojure.java.jdbc :as db]
            [clojure.tools.logging :as log]
            [environ.core :refer [env]]))


(comment (let [a 2]
           (db/with-db-transaction [trans db]
             (db/db-set-rollback-only! trans)
             (db/query trans "SELECT * FROM users")
             (db/insert! trans :users  ["name" "email"] ["hjh" "lkkjl"])
             (db/query trans "SELECT * FROM users")
             )
           (db/query db "SELECT * FROM users")
           ))

(deftest create-read-users
          (testing "Create user"
            (log/info "creating")
;            (log/info "delete" (users/delete-all))
            (db/with-db-transaction [trans db]
              (db/db-set-rollback-only! trans)
              (let [count-orig (users/count-users trans)]
                (log/info "count" count-orig)
                (users/create {:name "Charlie" :email "charlie@example.com"} trans)
                (log/info "count2" (users/count-users trans))
                (is (= (inc count-orig) (users/count-users trans))))
              (testing "Retrieve user"
                (let [user (users/create {:name "Andrew" :email "me@mytest.com"}  trans)
                      found-user (users/find-by-email "me@mytest.com" trans)]
                  (is (= "Andrew" (found-user :name)))
                  (is (= "me@mytest.com" (found-user :email)))))
              (testing "Find by email"
                (users/create {:name "John Doe" :email "j.doe@ihearttractors.com"})
                (let [user (users/find-by-email "j.doe@ihearttractors.com" trans)]
                  (is (= "John Doe" (user :name))))))))

(comment (deftest multiple-user-operations
           (testing "Find all users"
             (users/delete-all)
             (doseq [i (range 10)]
               (users/create {:name "Test user" :email (str "user." i "@example.com")}))
             (is (= 10 (count (users/find-all)))))))

(comment (deftest update-users
           (testing "Modifies existing user"
             (users/delete-all)
             (let [user-orig (users/create {:name "Curious George" :email "i.go.bananas@hotmail.com"})
                   user (users/find-by-email "i.go.bananas@hotmail.com")]
               (users/update-user (assoc user :name "Chiquita Banana"))
               (is (= "Chiquita Banana" (:name (users/find-by-id (user :id)))))))))


(comment (deftest delete-users
           (testing "Decrease user count"
             (let [user-c (users/create {:name "Temporary" :email "esphemerial@shortlived.com"})
                   user-count (users/count-users)
                   user (users/find-by-email "esphemerial@shortlived.com")]
               (users/delete-user user)
               (is (= (dec user-count) (users/count-users)))))
           (testing "Deleted correct user"
             (let [user-keep-c (users/create {:name "Keep" :email "important@users.net"})
                   user-del-c (users/create {:name "Delete" :email "irrelevant@users.net"})
                   user (users/find-by-email "irrelevant@users.net")
                   user2 (users/find-by-email "important@users.net")
                   ]
               (users/delete-user user)
               (is (= user2  (users/find-by-email "important@users.net")))
               (is (= nil (users/find-by-email "irrelevant@users.net")))))))


