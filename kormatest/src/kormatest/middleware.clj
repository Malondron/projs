(ns kormatest.middleware
  (:use ring.middleware.json)
  (:require 
            [ring.util.response :refer [redirect]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            
            
            ))




(defn wrap-csrf [handler]
  (wrap-anti-forgery handler))

(defn wrap-base [handler]
  (-> handler
;      wrap-dev
;      (wrap-idle-session-timeout {:timeout (* 60 30) :timeout-response (redirect "/")})
;      wrap-formats
      (wrap-json-body {:keywords? true})
      wrap-json-response
      (wrap-defaults
        (-> site-defaults
            (assoc-in [:security :anti-forgery] false)
;            (assoc-in  [:session :store] (memory-store session/mem))
            ))
;      wrap-servlet-context
;      wrap-internal-error
))
