(ns formtest.core-test
  (:use [formtest.formtest] :reload-all)
  (:use [clojure.test]))


(deftest test-check-encoding
  (is (= (check-encoding []) "ERROR"))
  (is (= (check-encoding ["Adress" "" "" "" ""]) "UTF-8"))
  (is (= (check-encoding ["Sto" "Be" "" "" ""]) "UTF-8"))
  (is (= (check-encoding ["Sto" "" "" "" ""]) "ISO-8859-1")))

(deftest test-l-err
  (is (= (l-err 2 "stuff") "Rad 2: Startar inte med \"stuff\"."))) 

(deftest test-f-form
  (is (= (f-form 2 "stuff") "Rad 2: Fel stuff och/eller separatortecken."))) 
