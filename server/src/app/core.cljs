(ns app.core
  (:require
   [spec-tools.core :as st]
   [macchiato.server :as http]
   [taoensso.timbre :as timbre]
   [app.migrations :as migrations]
   [app.routes :refer [handler]]
   [app.phrases]
   [app.config :refer [env]]
   [app.timbre.appender.sentry :refer [sentry-appender]]))

(timbre/merge-config!
 {:appenders {:sentry (sentry-appender (:sentry-dsn @env))}})

;; https://github.com/metosin/spec-tools/pull/130
(extend-type st/Spec
  IKVReduce
  (-kv-reduce [coll f init]
    (reduce-kv f init (into {} coll))))

(defn server []
  (migrations/migrate
   (fn []
     (let [config @env
           host (:host config)
           port (:port config)]
       (http/start {:handler handler
                    :host host
                    :port port
                    :on-success #(println (str "App started on " host ":" port))})))))

(set! *main-cli-fn* server)
