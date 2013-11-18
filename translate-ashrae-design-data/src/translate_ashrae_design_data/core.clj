(ns translate-ashrae-design-data.core 
  (:use [clojure.java.shell :only [sh]]
        [clojure.java.io :only [reader writer]]
	[clojure.string :only [split]])
  (:import (java.io File)))

(def *folder* "c:\\ashrae_design_days\\translate\\")

(defn read-lines [file]
  (with-open [r (reader file)]
    (doall (line-seq r))))

(defn write-lines [file s]
  (with-open [w (writer file)]
    (doseq [line s]
      (.write w  line))))


(defn file-exists? [file]
 (.exists (java.io.File. file)))


(defn parse-design-day-file [file]
  (let [lines (read-lines file)
        data (split (second lines) #"\t")
        station (nth data 3)
        station-nr (nth data 4)
        lat (nth data 6)
        long (nth data 7)
        elev (nth data 8)
        pressure (nth data 9)
        time-zone (nth data 10)
        monthly-5-dry-bulb (drop 22 (take 24 lines))
        mean-dry-temp-range (drop 35 (take 37 lines))]
        
    monthly-5-dry-bulb))





(defn translate-all []
  (let [files (map #(str *folder* %1) (seq (. (File. *folder*) (list))))]
    (map parse-design-day-file files)))

(translate-all)


