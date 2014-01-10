(ns anex58-translation.core
  (:use [clojure.java.shell :only [sh]]
        [clojure.java.io :only [reader writer]]
	[clojure.string :only [split replace]])
  (:import (java.io File)))

(def input-file "d:\\annex58\\twinhouse.csv")
(def output-file "d:\\annex58\\twinhouse.prn")
(def refrPoints [0.0 0.017453, 0.05236, 0.087266, 0.17453, 0.2618, 0.34907, 0.5236, 0.69813, 0.87266,  1.0472,  1.2217,   1.3963,  1.5708])
(def refrValues [0.010753, 0.0074467, 0.0055608, 0.0029816, 0.0016047, 0.0010763, 0.00079994 0.00050905, 0.00034422, 0.00024725, 0.00016968, 0.00010666,0.00005333, 0.0])


(defn interpolate [val points values]
  (if (< val (points 0))
    (values 0)
    (loop [i 0]
      (if (= (count points) i)
        0
        (if (< val (points i))
          (+ (values i) (/ (* (- val (points i)) (- (values (dec i)) (values i))) (- (points (dec i)) (points i))))
          (recur (inc i)))))))

(interpolate 3 refrPoints refrValues)

(defn read-lines [file]
  (with-open [r (reader file)]
    (doall (line-seq r))))

(defn write-lines [file s]
  (with-open [w (writer file)]
    (doseq [line s]
      (.write w  line))))

(defn make-wxy [ws wdir]
  (let [theta (* -1 (- wdir 90))
        deg2rad (/ Math/PI 180)]
    [(* ws (Math/cos (* deg2rad theta))) (* ws (Math/sin (* deg2rad theta)))]))

(defn norm-hor [glob-hor diff-hor time]
  (let [day-in-year (/ time 24)
        deg2rad (/ Math/PI 180)
        dec (* 23.45 deg2rad (Math/sin (* 2 Math/PI (/ (+ 284.5 day-in-year) 365))))
        daynr (inc (int day-in-year))
        B (* 2 Math/PI (/ (- daynr 81) 364))
        E (- (* 9.87 (Math/sin (* 2 B))) (* 7.53 (Math/cos B)) (* 1.5 (Math/sin B)))
        hour (- time (* 24 (- daynr 1)))
        soltime (+ hour (/ (* 4 (- -15 -11.728)) 60) (/ E 60))
        omega (* (- soltime 12) 15 deg2rad)
        sinelevsun (+ (* (Math/cos (* deg2rad 47.874)) (Math/cos dec) (Math/cos omega)) (* (Math/sin (* deg2rad 47.874)) (Math/sin dec)))
        rad2deg (/ 180 Math/PI)
        elevsun (* (Math/asin sinelevsun) rad2deg)
        refrcorr (interpolate elevsun refrPoints refrValues)
        correlevsun (+ elevsun refrcorr)
        A 1107
        B1 0.207
        idirnorm  (max (/ (- glob-hor diff-hor) (Math/cos (* deg2rad (- 90 correlevsun)))) 0)
        IDirNormMax (/ A (Math/exp (/ B1 (+ sinelevsun (* 0.15 (Math/pow (+ (* elevsun rad2deg) 3.885) -1.253))))))]
    (if (< elevsun 0)
      0
      (if (< 1.15 (/ idirnorm IDirNormMax))
        (* IDirNormMax 1.15)
        idirnorm))))
        
(defn annex58-translation
  []
  (let [inlines (map (fn [x] (replace x "," ".")) (drop 2 (read-lines input-file)))
        seps (map (fn [x] (split x #";" )) inlines)
        start-time (+ (/ 5.0 60) (dec (+ (* 4 24 31) (* 2 24 30) (* 28 24) (* 20 24))))
        n-lines (count inlines)]
    (with-open [w (writer output-file)]
      (loop [i 0 time start-time]
        (if (= i n-lines)
          "Done"
          (let [el (map read-string (drop 2 (nth seps i)))
                temp (nth el 0)
                glob-hor (nth el 1)
                diff-hor (nth el 2)
                ws (nth el 3)
                wdir (nth el 4)
                relh (nth el 5)
                wxy (make-wxy ws wdir)
                norm-hor (norm-hor glob-hor diff-hor time)]
            (do
              (.write w (str " " time "  " temp "  " relh "  " (first wxy) "  " (second wxy) "  " norm-hor "  " diff-hor \newline))              (recur (inc i) (+ time (/ 10.0 60))))))))))


(annex58-translation)

(read-string "2013/01/02")

(defn 


(defn sun-elev [time]
  (let [day-in-year (/ time 24)
        deg2rad (/ Math/PI 180)
        dec (* 23.45 deg2rad (Math/sin (* 2 Math/PI (/ (+ 284.5 day-in-year) 365))))
        daynr (inc (int day-in-year))
        B (* 2 Math/PI (/ (- daynr 81) 364))
        E (- (* 9.87 (Math/sin (* 2 B))) (* 7.53 (Math/cos B)) (* 1.5 (Math/sin B)))
        hour (- time (* 24 (- daynr 1)))
        soltime (+ hour (/ (* 4 (- -15 -11.728)) 60) (/ E 60))
        omega (* (- soltime 12) 15 deg2rad)
        sinelevsun (+ (* (Math/cos (* deg2rad 47.874)) (Math/cos dec) (Math/cos omega)) (* (Math/sin (* deg2rad 47.874)) (Math/sin dec)))
        rad2deg (/ 180 Math/PI)
        elevsun (* (Math/asin sinelevsun) rad2deg)
        refrcorr (interpolate elevsun refrPoints refrValues)
        correlevsun (+ elevsun refrcorr)]
   correlevsun ))

(Math/cos (* (/ Math/PI 180) (- 90 (sun-elev 6079.583333334265))))
(Math/cos (* deg2rad (- 90 correlevsun))

          (let [A 1107
                B1 0.207
                IDirNormMax (/ A (Math/pow (/ B1 (s))))]