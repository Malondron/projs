(ns eggDrop
  (:use [clojure.java.io  :only [reader writer]]
        [clojure.string  :only [split]])
  (:import (java.io File)))

(def d-max 30000)
(def b-max 33)
;(def infile "c:\\gcj\\2008Practise\\PracticeProblems\\C-small-practice.in")
;(def outfile "c:\\gcj\\2008Practise\\PracticeProblems\\C-small-practice.out.clj")
(def infile "/home/andreask/gcj/2008Practise/PracticeProblems/C-small-practice.in")
(def outfile "/home/andreask/gcj/2008Practise/PracticeProblems/C-small-practice.out.clj")


(defn read-lines [file]
  (with-open [r (reader file)]
    (doall (line-seq r))))

(defn write-lines
	  "Writes lines (a seq) to f, separated by newlines.  f is opened with
	  writer, and automatically closed at the end of the sequence."
	  [f lines]
	  (with-open [writer (writer f)]
	    (loop [lines lines]
	      (when-let [line (first lines)]
	        (.write writer (str line))
	        (.newLine writer)
	        (recur (rest lines))))))


(defn calc-new-term [d b F]
   (if (or (== d 0) (== b 0))
     0
    (if (== d 1)
      1
      (let [res (+ 1 ((F (- d 1)) b) ((F (- d 1)) (- b 1)))]
        (if (> res 4294967296) 4294967296 res)))))


(defn set-up-f [F]
  (loop [x (int 0) out F]
    (if (== x d-max)
      out
      (let [y-calcs (loop [y (int 0) out2 []]
                      (if (== b-max y)
                        out2
                        (do
;                          (print out2)
                        (recur (inc y) (conj out2 (calc-new-term x y out))))))]
        (recur (inc x) (conj out y-calcs))))))

(defn egg-drop []
   (let [lines (read-lines infile)
         nCases (read-string (first lines))
         F (set-up-f [])
         out-file outfile]
     ((F 2345) 24)))

(set! *warn-on-reflection* true)

(defn foo [x] (.indexOf x "o"))

(foo "jk")
(time (egg-drop))
(Math/pow 2 32)

(defn calc-test [i i res]
  (if (== i 0)
    0
    (if (== i 1)
      1
      (+ 1 (res (- i 1))))))

         

(defn teste []
  (let [^IPersistentMap m {}
        r (* 30 3000)
        ut (loop [i (int 1) res m]
             (if (== i r)
               res
               (recur (inc i) (conj res {i (calc-test i i res)}))))]
    (ut (dec r))))


(defn teste2 [F]
  (loop [x (int 0) out F]
    (if (== x d-max)
      out
      (let [y-calcs (loop [y (int 0) out2 []]
                      (if (== b-max y)
                        out2
                        (do
;                          (print out2)
                        (recur (inc y) (conj out2 (calc-test x y out2))))))]
        (recur (inc x) (conj out y-calcs))))))
  
    

(time
 (count (teste2 [])))
(range 10)
(let [v [[]]]
  (conj (v 0) 2))

(str i:i)
(let [k {:1s2 3}
      i 1
      j 2]
  (k (key i "s" j)))
(time (def str-keys (map str (range 100000))))
(time (def m (zipmap str-keys (range 100000))))
(time (dotimes [i 1] (doseq [k str-keys] (m k))))