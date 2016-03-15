(defproject restful-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.9.7"]
            [ragtime/ragtime.lein "0.3.6"]]
  :ring {:handler restful-clojure.handler/app
         :nrepl {:start? true
                 :port 9998}}
  :ragtime {
            :datastore {:connection-uri "jdbc:postgresql://localhost:5432/restful_dev?user=restful_dev&password=pass_dev"}
            }
  :profiles {:dev {
                   :dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.0"]]}
             :test {:ragtime {:datastore {:connection-uri "jdbc:postgresql://localhost:5432/restful_test?user=restful_test&password=pass_test"}}}
             }
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [compojure "1.5.0"]
                 [korma "0.4.0"]
                 [ragtime "0.5.3"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [cheshire "5.5.0"]])


