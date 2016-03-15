(ns restful-clojure.handler
  (:use compojure.core
        ring.middleware.json)
  (:require [compojure.handler :as handler]
            [ring.util.response :refer [response]]
            [compojure.route :as route]))

(defn wrap-log-request [handler]
  (fn [req]
    (println req)
    (handler req)))


(defroutes app-routes
  (route/not-found {:message "Page not found"}))

(def app (->  app-routes
              wrap-log-request
              wrap-json-response
              wrap-json-body))
