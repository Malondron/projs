(ns kormatest.trupp-test
  (:use clojure.test
        kormatest.core-test)
  (:require [kormatest.models.users :as users]))

(use-fixtures :each with-rollback)

(deftest create-find-user
  (testing "Create user"
    (let [user (users/create {:name "jkjkjkjk" :email "llk99fdj"})]
      (is (= (dissoc user :id) (users/find-by-email "llk99fdj")))))) 
