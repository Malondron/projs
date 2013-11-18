(ns compcheck.core (:gen-class)
  (:require [clj-http.client :as client])
  (:import (java.net InetAddress))
  (:import (org.apache.commons.mail SimpleEmail)))

(def my-file "q:\\users\\andreas\\test.txt")

(def rand-chars (map char (concat (range 48 58) (range 65 91) (range 97 123))))


(defn random-char []
  (nth rand-chars (rand (count rand-chars))))

(defn create-string [size]
  (apply str (take size (repeatedly random-char))))

(defn write-string [size]
  (spit my-file (create-string size)))


(defn read-file []
  (slurp my-file))


(defn time-stuff [times size]
  (let [start (System/nanoTime)]
    (dotimes [i times]
      (write-string size)
      (read-file))
    (/ (/ (-  (System/nanoTime) start) 1000000000.0) times)))


(defn file-up? [file]
  (let [res (try (client/get file) (catch Exception e) (finally false)) ]
    (if (and res (= 200 (:status res)))
      true
      false)))

(defn send-res []
  (let [date-and-time (java.util.Date.)
        app3 (file-up? "http://www.equaonline.com/ice4user/index.html")
        q (time-stuff 5 100000)]
    (doto (SimpleEmail.)
      (.setHostName "smtp.gmail.com")
      (.setSslSmtpPort "465")
      (.setSSL true)
      (.addTo "kissavos@gmail.com")
      (.setFrom "kissavos@gmail.com")
      (.setSubject (str "Results " (.toString date-and-time)))
      (.setMsg (format "%s%n%n%s%s%n%s%f" (.toString date-and-time) 

                   "Appserver3: " (if app3 "Up" "Down")
                   "Q-disk:"  q))
      (.setAuthentication "kissavos@gmail.com" "(sk8recht!M)")
      (.send))))

(defn -main [& args]
  (send-res))

;(-main)

;(time-stuff 1 1000)

;(defn ping-site [site]
;  (.isReachable (InetAddress/getByName site) 5000))

;(ping-site "www.dn.se")
;(InetAddress/getByName "www.equaonline.com")

;(time
;   (write-string 1000))

  
