(ns kormatest.core-test
  (:use clojure.test)
  (:require [korma.db :as db]))

(defn with-rollback [test-fn]
  (db/transaction
   (test-fn)
   (db/rollback)))
