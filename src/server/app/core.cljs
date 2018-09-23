(ns app.core
  (:require
   [spec-tools.core :as st]
   [macchiato.server :as http]
   [app.routes :refer [handler]]
   [app.phrases]))

;; https://github.com/metosin/spec-tools/pull/130
(extend-type st/Spec
  IKVReduce
  (-kv-reduce [coll f init]
    (reduce-kv f init (into {} coll))))

(defn server []
  (http/start {:handler handler
               :host "127.0.0.1"
               :port "3000"
               :on-success #(println "app started on 127.0.0.1:3000")}))

(set! *main-cli-fn* server)
