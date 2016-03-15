(ns user
  (:require [mount.core :as mount]
            luminustest.core))

(defn start []
  (mount/start-without #'luminustest.core/repl-server))

(defn stop []
  (mount/stop-except #'luminustest.core/repl-server))

(defn restart []
  (stop)
  (start))


