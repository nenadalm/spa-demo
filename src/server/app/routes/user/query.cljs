(ns app.routes.user.query
  (:refer-clojure :exclude [list])
  (:require
   [app.sql :as sql]
   [app.db]
   [app.db :as db]))

(defn create [{:keys [name]}]
  (-> (sql/build :insert-into :user
                 :columns [:name]
                 :values [[name]]
                 :returning [:id :name])
      sql/format
      db/query))

(defn list []
  (-> (sql/build :select [:id :name]
                 :from :user)
      sql/format
      db/query))
