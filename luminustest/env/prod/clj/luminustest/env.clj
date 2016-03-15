(ns luminustest.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[luminustest started successfully]=-"))
   :middleware identity})
