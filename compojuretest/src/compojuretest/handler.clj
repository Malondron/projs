(ns compojuretest.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [compojuretest.query :refer :all]
            [ring.middleware.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/api/todos" [] (get-todos))
  (GET "/api/todos/:id" [id] (get-todo (Integer/parseInt id)))
  (POST "/api/todos" [title] (add-todo title))
  (PUT "/api/todos/:id" [id] (update-todo (Integer/parseInt id)))
  (DELETE "/api/todos/:id" [id] (delete-todo (Integer/parseInt id)))
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (json/wrap-json-params)
      (json/wrap-json-response)))
