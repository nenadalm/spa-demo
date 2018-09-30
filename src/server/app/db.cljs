(ns app.db
  (:require
   [cljs.nodejs :as node]
   [sqlingvo.core :as sql]
   [sqlingvo.node.async :as sdb :refer-macros [<? <!?]]
   [app.config :refer [env]])
  (:require-macros
   [cljs.core.async.macros :refer [go]]))

(def pg (node/require "pg"))

(def type-timestamp 1114)

;; mark timestamp columns without timezone as columns containing time in UTC timezone
;; (current timezone by default)
(.setTypeParser (.-types pg) type-timestamp (fn [res]
                                              (if (nil? res)
                                                res
                                                (-> res
                                                    (clojure.string/replace " " "T")
                                                    (clojure.string/replace #"\..*" "Z")))))

(defn clj->db [s]
  (clojure.string/replace (name s) "-" "_"))

(defn db->clj [s]
  (clojure.string/replace (name s) "_" "-"))

(def db (-> (sdb/db (:db-connection-string @env)
                    {:sql-name clj->db
                     :sql-identifier db->clj})
            (sdb/start)))

(defn pg-array
  "Converts `coll` into postgres array."
  [coll]
  (str "{" (clojure.string.join "," coll) "}"))

(defn query
  "`q` is a function with `db` argument returning sqlingvo query. `f` is a function accepting result of the query `q`."
  [q f]
  (go (f (<!? (q db)))))
