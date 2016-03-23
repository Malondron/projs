(ns kormatest.handler
  (:use compojure.core
        ring.middleware.json)
  (:require [compojure.handler :as handler]
            [ring.util.response :refer [response]]
            [environ.core :refer [env]]
            [taoensso.timbre :as timbre]
            [kormatest.middleware :as middleware] 
;            [korma.session :as session]
            [taoensso.timbre.appenders.rotor :as rotor]
            [clojure.tools.nrepl.server :as nrepl]
            [compojure.route :as route]))

(defonce nrepl-server (atom nil))
(defn start-nrepl
  "Start a network repl for debugging when the :repl-port is set in the environment."
  []
  (when-let [port (env :repl-port)]
    (try
      (reset! nrepl-server (nrepl/start-server :port port))
      (timbre/info "nREPL server started on port" port)
      (catch Throwable t
        (timbre/error "failed to start nREPL" t)))))

(defn stop-nrepl []
  (when-let [server @nrepl-server]
    (nrepl/stop-server server)))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (timbre/set-config!
    [:appenders :rotor]
    {:min-level             (if (env :dev) :trace :info)
     :enabled?              true
     :async?                false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn                    rotor/appender-fn})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "clj_cider_debug_demo.log" :max-size (* 512 1024) :backlog 10})

  (start-nrepl)
  ;;start the expired session cleanup job
;  (session/start-cleanup-job!)
  (timbre/info (str
                 "\n-=[ clj-cider-debug-demo started successfully"
                 (when (env :dev) "using the development profile")
                 "]=-")))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "clj-cider-debug-demo is shutting down...")
  (stop-nrepl)
  (timbre/info "shutdown complete!"))

#dbg
(defn ffkj []
  "jklj")


(defn rere []
  (let [jk (ffkj)]
    jk))

(defroutes app-routes
  (GET "/" [] (rere))
  (route/not-found (response {:message "Page not found"})))

(defn wrap-log-request [handler]
  (fn [req]
    (println req)
    (handler req)))

(def app (-> app-routes
             (wrap-routes #'home-routes middleware/wrap-csrf)
             ))
