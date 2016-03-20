(defproject kormatest "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
;		[migratus "0.8.13"]
                 [ragtime "0.5.3"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
 		[korma "0.4.0"]]
  :aliases {"migrate" ["run" "-m" "kormatest.migrations/migrate"]
            "rollback" ["run" "-m" "kormatest.migrations/rollback"]
            }
 )
