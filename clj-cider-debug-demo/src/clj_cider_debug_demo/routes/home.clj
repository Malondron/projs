(ns clj-cider-debug-demo.routes.home
  (:require [clj-cider-debug-demo.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))


(defn make-resp
  []
  (let [x 1
        y 2]
    (+ x y)))

(defn home-page []
  (let [resp (make-resp)]
    (layout/render
     "home.html" {:docs (-> "docs/docs.md" io/resource slurp)})))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))

