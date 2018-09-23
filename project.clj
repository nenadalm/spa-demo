(defproject spa-demo "0.0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [macchiato/core "0.2.14"]
                 [metosin/reitit "0.2.2"]
                 [org.clojure/test.check "0.10.0-alpha2"]
                 [phrase "0.3-alpha4"]]
  :profiles {:dev
             {:dependencies [[com.bhauman/figwheel-main "0.1.9"]]}}
  :source-paths ["src/server"]
  :aliases {"cljfmt" ["update-in" ":plugins" "conj" "[lein-cljfmt \"0.6.1\"]" "--" "cljfmt"]
            "build-server-dev"
            ["with-profile" "dev,repl" "run" "-m" "figwheel.main" "-b" "server-dev" "-r"]})

