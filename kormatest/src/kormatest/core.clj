(ns kormatest.core
  (:require [kormatest.handler :refer [app init destroy]]
            [ring.adapter.jetty :refer [run-jetty]]
            
            [ring.middleware.reload :as reload]
            [environ.core :refer [env]])
  (:gen-class))

(defn parse-port [[port]]
  (Integer/parseInt (or port (env :port) "3000")))





(defonce server (atom nil))

(defn start-server [port]
  (init)
  (reset! server
          (run-jetty
            (if (env :dev) (reload/wrap-reload #'app) app)
            {:port port
             :join? false})))

(defn stop-server []
  (when @server
    (destroy)
    (.stop @server)
    (reset! server nil)))

(defn start-app [args]
  (let [port (parse-port args)]
    (.addShutdownHook (Runtime/getRuntime) (Thread. stop-server))
    (start-server port)))




(defn -main [& args]
  (start-app args))

