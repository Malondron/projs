(defproject compcheck "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["central-proxy" "http://repository.sonatype.org/content/repositories/central/"]]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-http "0.7.7"]
                 [org.apache.commons/commons-email "1.2"]]
    :main compcheck.core)
