(ns app.db
  (:require
   [cljs.nodejs :as node]
   [taoensso.timbre :refer-macros [error]]
   [app.config :refer [env]])
  (:require-macros
   [cljs.core.async.macros :refer [go]]))

(def pg (node/require "pg"))
(def Pool (.-Pool pg))
(def pcs (node/require "pg-connection-string"))

(def type-timestamp 1114)

;; mark timestamp columns without timezone as columns containing time in UTC timezone
;; (current timezone by default)
(.setTypeParser (.-types pg) type-timestamp (fn [res]
                                              (if (nil? res)
                                                res
                                                (-> res
                                                    (clojure.string/replace " " "T")
                                                    (clojure.string/replace #"\..*" "Z")))))

(def pool (Pool. (.parse pcs (:db-connection-string @env))))
(.on pool "error" (fn [e _]
                    (error "idle client error" e)))

(defn col->key [col]
  (-> col
      (clojure.string/replace "_" "-")
      keyword))

(defn convert-row [row]
  (->> row
       (.entries js/Object)
       (map (fn [[k v]] [(col->key k) v]))
       (into {})))

(defn query
  "Executes query `q` with parameters `params`. Returns promise."
  [[q & params]]
  (-> (.query pool q (to-array params))
      (.then (fn [res]
               (into []
                     (map convert-row)
                     (.-rows res))))))
