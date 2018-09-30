(ns app.core
  (:require
   [spec-tools.core :as st]
   [macchiato.server :as http]
   [app.migrations :as migrations]
   [app.routes :refer [handler]]
   [app.phrases]
   [app.config :refer [env]]))

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
