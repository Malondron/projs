(ns translate-ashrae.core
  (:use [clojure.java.shell :only [sh]]
        [clojure.java.io :only [reader writer file copy]]
        [clojure.string :only [split]])
  (:import (java.io File)))


(def *workdir* "c:\\ashrae_to_fix\\test\\")
(def file-list (seq (. (File. *workdir*) (list))))

(print file-list)



(defn zip-again
  [file1 file2]
  (let [file-path *workdir*]
    (sh "c:\\program files\\7-zip\\7za.exe""a"(str file-path (remove-file-ending file1) ".zip") (str file-path file1) (str file-path file2))))


(defn do-all
  [directory]
  (let [file-list (seq (. (File. directory) (list)))]
    (doseq [i file-list]
      (let [ending (second (split i #"\."))
            file-name (remove-file-ending i)]
        (make-script i)
        (translate (str file-name "_IW2.ws"))
        (zip-again (str file-name "_IW2.prn") "weather.idm")))))


(do-all *workdir*)
	 
(let [file-list (seq (. (File. "c:\\programming\\translate\\") (list)))]
      (make-script (remove-file-ending (second file-list)))
      (translate))



(defn translate
  [file]
  (let [file-path *workdir*]
    (sh "c:\\Programming\\weatherstuff\\newold\\IceWeather.exe" (str file-path file))))


(defn make-script
  [file]
  (let [line1 (str "< " *workdir*)
	line2 (str "> " *workdir*)
	line3 "# ASHRAE_IW2"
	line4 (str " " (remove-file-ending file))]
  (spit (str *workdir* (remove-file-ending file)"_IW2.ws") (format "%s%n%s%n%s%n%s" line1 line2 line3 line4))))


(defn remove-file-ending
  [file]
  (first (split file #"\.")))


(let [sol-dir-deg 63.9 
      sol-elev-deg 4.256
      sol-dir (/ (* sol-dir-deg Math/PI) 180)
      sol-elev (/ (* sol-elev-deg Math/PI) 180)]
  [(* (Math/sin  sol-dir) (Math/cos sol-elev)) (* (Math/cos  sol-dir) (Math/cos sol-elev))  (Math/sin sol-elev)])