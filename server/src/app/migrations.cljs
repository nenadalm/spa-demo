(ns app.migrations
  (:require
   [macchiato.migrations.core :as migrations]
   [app.config :refer [env]]
   [cljs.nodejs :as node]
   [taoensso.timbre :refer-macros [error]]))

(defn migrate [f]
  (migrations/migrate
   {:migration-dir "migrations"
    :driver "pg"
    :schema-table "schemaversion"
    :connection-string (:db-connection-string @env)}
   :max
   f
   (fn [e]
     (error e"Failed executing migrations.")
     (.exit node/process 1))))
