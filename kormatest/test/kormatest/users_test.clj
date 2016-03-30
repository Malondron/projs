(ns kormatest.users-test
  (:use clojure.test
        kormatest.core-test)
  (:require [kormatest.models.users :as users]
            [environ.core :refer env]))


(use-fixtures :each with-rollback)

(deftest create-read-users
  (testing "Create user"
    (let [count-orig (users/count-users)]
      (users/create {:name "Charlie" :email "charlie@example.com"})
      (is (= (inc count-orig) (users/count-users))))))
