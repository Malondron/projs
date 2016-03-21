(ns kormatest.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [ring.util.response :refer [response]]
            [compojure.route :as route]))


(defroutes app-routes
  (route/not-found (response {:message "Page not found"})))

(defn wrap-log-request [handler]
  (fn [req]
    (println req)
    (handler req)))

(def app (-> app-routes
             wrap-log-request
             wrap-json-response
             wrap-json-body))
