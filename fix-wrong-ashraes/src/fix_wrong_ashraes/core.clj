(ns fix-wrong-ashraes.core
  (:use [clojure.java.shell :only [sh]]
        [clojure.java.io :only [reader writer]]
	[clojure.string :only [split]])
  (:import (java.io File)))

;; These should all be updated before making the files

(def infile "d:\\fix-weather\\020440_IWC.PRN")
(def outfile (str infile "_fixed"))




(defn read-lines [file]
  (with-open [r (reader file)]
    (doall (line-seq r))))

(defn write-lines [file s]
  (with-open [w (writer file)]
    (doseq [line s]
      (.write w  line))))


(defn fix-it [in-file out-file]
  (let [inlines (read-lines in-file)
        n-lines (count inlines)]
    (with-open [w (writer out-file)]
      (.write w (str (first inlines)))
      (.write w "\r\n")
      (.write w (str (second inlines)))
      (.write w "\r\n")
      (loop [i 2]
        (if (= i n-lines)
          "Done"
          (do
            (let [line (re-seq #"[\d-.]+ *" (nth inlines i))
                  hour (read-string (first line))
                  new-hour (- hour 0.5)]
              (.write w (str "  " new-hour "  " (apply str (rest line))))
              (.write w "\r\n")
              (recur (inc i)))))))))


(fix-it infile outfile)
    

(re-seq #"[\d.]+ *" ".5445   5.5455 7.767767")

