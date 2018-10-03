(ns app.db
  (:require
   [cljs.nodejs :as node]
   [taoensso.timbre :refer-macros [error]]
   [app.config :refer [env]]))

(def ^:private pg (node/require "pg"))
(def ^:private Pool (.-Pool pg))
(def ^:private pcs (node/require "pg-connection-string"))

(def ^:private type-timestamp 1114)

;; mark timestamp columns without timezone as columns containing time in UTC timezone
;; (current timezone by default)
(.setTypeParser (.-types pg) type-timestamp (fn [res]
                                              (if (nil? res)
                                                res
                                                (-> res
                                                    (clojure.string/replace " " "T")
                                                    (clojure.string/replace #"\..*" "Z")))))

(def ^:private pool (Pool. (.parse pcs (:db-connection-string @env))))
(.on pool "error" (fn [e _]
                    (error "idle client error" e)))

(defn- col->key [col]
  (-> col
      (clojure.string/replace "_" "-")
      keyword))

(defn- convert-row [row]
  (->> row
       (.entries js/Object)
       (map (fn [[k v]] [(col->key k) v]))
       (into {})))

(defn- convert-result [res]
  (into []
        (map convert-row)
        (.-rows res)))

(defn query
  "Executes query `q` with parameters `params`. Returns promise."
  ([v]
   (query pool v))
  ([client [q & params]]
   (-> (.query client q (to-array params))
       (.then convert-result))))

(defn transactional
  "Takes function `f` that accepts function that executes queries in transaction."
  [f]
  (-> (.connect pool)
      (.then (fn [client]
               (-> (.query client "BEGIN")
                   (.then #(f (partial query client)))
                   (.then (fn [result]
                            (-> (.query client "COMMIT")
                                (.then (fn [_]
                                         (.release client)
                                         result)))))
                   (.catch (fn [e]
                             (.query client "ROLLBACK")
                             (.release client e)
                             (throw e))))))))
