(defproject kormatest "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.9.7"]
            [lein-environ "1.0.2"]
            [ragtime/ragtime.lein "0.3.6"]]
  :jvm-opts ["-server"]
  :main kormatest.core
  :ring {:handler kormatest.handler/app
         :init kormatest.handler/init
         :destroy kormatest.handler/destroy
         }
  :profiles {:dev {
                 :dependencies [[javax.servlet/servlet-api "2.5"]
                                [ring/ring-mock "0.3.0"]
                                [ring/ring-devel "1.3.2"]]
                   :repl-options {:init-ns kormatest.core}
                   
                   :env {:database "newtest" :db-user "newtest" :password "newtest"}}
           :test {:env {:database "newtest" :db-user "newtest" :password "newtest"}}
           }
            
  :dependencies [[org.clojure/clojure "1.8.0"]
 ;		[migratus "0.8.13"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [com.taoensso/timbre "3.4.0"]
                 [compojure "1.5.0"]
                 [ragtime "0.5.3"]
                 [ring-server "0.4.0"]
                 [environ "1.0.2"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [korma "0.4.0"]]
  :aliases {"migrate" ["run" "-m" "kormatest.migrations/migrate"]
                                       "rollback" ["run" "-m" "kormatest.migrations/rollback"]
                                       }
 )
