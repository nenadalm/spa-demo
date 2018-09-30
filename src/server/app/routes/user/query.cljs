(ns app.routes.user.query
  (:require
   [sqlingvo.core :as sql]))

(defn create [{:keys [name]}]
  (fn [db]
    (sql/insert
        db
        :user
        [:name]
      (sql/values [[name]])
      (sql/returning :id :name))))

(defn list []
  (fn [db]
    (sql/select
        db
        [:id :name]
      (sql/from :user))))
