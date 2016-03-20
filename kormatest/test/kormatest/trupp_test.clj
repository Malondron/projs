(ns kormatest.trupp-test
  (:use clojure.test
        kormatest.core-test)
  (:require [kormatest.core :as kt]))

(use-fixtures :each with-rollback)

(deftest create-find-user
  (testing "Create user"
    (let [user (kt/create {:name "jkjkjkjk" :email "llk99fdj"})]
      (is (= (dissoc user :id) (kt/find-by-email "llk99fdj")))))) 
