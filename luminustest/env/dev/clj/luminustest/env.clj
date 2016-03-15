(ns luminustest.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [luminustest.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[luminustest started successfully using the development profile]=-"))
   :middleware wrap-dev})
