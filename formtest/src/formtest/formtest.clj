;;
;; A tester program to run files with to check if they are in certain formats.
;; 
;; The formats are as follows:
;;
;; 1. ISO-8859-1 (Latin-1) 
;;    8 rubrikrader
;;    tab (00x9) fältseparator
;;    , decimalseparator
;;    yyyy-mm-dd datumdel
;;    hh:mm:ss   tidsdel
;;    (blank),;  3 datum/tidsseparator
;;
;; 2. UTF-8
;;    8 rubrikrader
;;    , fältseparator
;;    . decimalseparator
;;    yyyy-mm-dd datumdel
;;    hh:mm:ss   tidsdel
;;    (blank),;  3 datum/tidsseparator
;; 3. UTF-8
;;    5 rubrikrader
;;    ; fältseparator
;;    , decimalseparator
;;    yyyy-mm-dd datumdel
;;    hh:mm:ss   tidsdel
;;    (blank),;  3 datum/tidsseparator

;; Bör ta antingen en (katalog med filer?) eller en fil (fler filer?) 
;; (zip-filer)?
;; Skriva rapport om vad som är fel.

(ns formtest.formtest (:gen-class))
(use 
 '[clojure.contrib.duck-streams :only (read-lines)]
 'clojure.test)
(import '(javax.swing JFrame JFileChooser)
       	'(java.io BufferedReader InputStreamReader FileInputStream))


;;
;; Tries to get the encoding from the text. This is in general not possible.
;; Also, it is not used right now, but might be.
;;



(defn check-encoding 
  "Checks the encoding from the text."
  [line-list]
  (let [c (count line-list)]
    (if (< c 5)
      "ERROR"
      (cond
       (re-find #"Adress" (first line-list)) "UTF-8-5"
       (or (re-find #"Be" (nth line-list 1)) (re-find #"Be" (nth line-list 2))) "UTF-8-8"
       (re-find #"Starttid" (nth line-list 1)) "ISO-8859-1"
       :else "ERROR"))))

(defn format-crit
  "Specifies the formats for the different formats (!). The last regexp is for the data lines."
  [enc]
  (cond
   (= enc "ISO-8859-1") [#"Loggpunkt" #"Starttid[ ,;]+\d\d\d\d-\d\d-\d\d[ ,;]+\d\d:\d\d:\d\d" #"Sluttid[ ,;]+\d\d\d\d-\d\d-\d\d[ ,;]+\d\d:\d\d:\d\d" #"Enhet" #"IOTyp" #"Text" #"Antal v.rden" #"Datum[ ,;]+Tid[ ,;]+[\w+[ ,;]*]+" #"\d\d\d\d-\d\d-\d\d\t\d\d:\d\d:\d\d[\t\d+[,]*\d+]+"]
   (= enc "UTF-8-8") [#"Loggpunkt:" #"Beteckning:" #"Beskrivning" #"Enhet:" #"Typ:" #"Starttid:[ ,;]+\d\d\d\d-\d\d-\d\d[ ,;]+\d\d:\d\d:\d\d" #"Stopptid:[ ,;]+\d\d\d\d-\d\d-\d\d[ ,;]+\d\d:\d\d:\d\d"  #"Datum[ ,;]+Tid[ ,;]+[\w+[ ,;]*]+" #"\d\d\d\d-\d\d-\d\d,\d\d:\d\d:\d\d[,\d+[.]*\d+]+"]
   (= enc "UTF-8-5") [#"Adress;" #"Rapport;" #"Starttid[ ,;]+\d\d\d\d-\d\d-\d\d[ ,;]+\d\d:\d\d:\d\d" #"Stopptid[ ,;]+\d\d\d\d-\d\d-\d\d[ ,;]+\d\d:\d\d:\d\d"  #"Datum[ ,;]+Tid[ ,;]+[\w+[ ,;]*]+" #"\d\d\d\d-\d\d-\d\d;\d\d:\d\d:\d\d[;\d+[,]*\d+]+"]
   :else "ERROR"))


(defn check-head-body
  "Checks f file comply to formats."
  [line-list fmts]
  (let [ll (vec line-list)
	fmtsadd (vec (flatten (conj fmts (take (- (count line-list) (count fmts)) (repeat (last fmts))))))]
    (for [i (range (count ll)) :when (not (re-find (fmtsadd i) (ll i)))]
      (str "Rad " (inc i) " ser ut som: " (ll i) " ,men borde ha formatet: " (str (fmtsadd i)) ".\n"))))
    

      

(defn check-format
  "Check that the file follows the formatting rules."
  [file encoding]
  (let [name (.getName file)]
    (if (= encoding "ERROR")
      (str "Error in reading of file: " name  ". File very short, empty or of wrong format.")
      (let [line-list (read-lines file)
	    fmts (format-crit encoding)]
	(check-head-body line-list fmts)))))


(defn -main 
  ;; Starts by opening a JFileChooser to get the user to choose the
  ;; file to check.
  []
  (let [frame (JFrame. "IDA Log File Parser")
	fc (JFileChooser.)
	rep []]
    (doto fc (.showOpenDialog frame))
    (doto frame
      (.setSize 300 180))
    (let [file (.getSelectedFile fc)
	  dir (.getParent file)
	  report (str dir "\\report.txt")]
      (if (not (.isFile file))
	(do
	  (spit report "Vald 'fil' är ingen fil.")
	  (System/exit 0)))
      (if file
	(let [enc (check-encoding (take 8 (read-lines file)))]
	    (if (not (= "ERROR" enc))
	      (let [outp (check-format file enc)]
		(if (empty? outp)
		  (do
		    (spit report "Inga fel hittades." :encoding "UTF-8")
		    (System/exit 0))
		  (do
		    (spit report (apply str outp) :encoding "UTF-8")
		    (System/exit 0))))))))))
      


