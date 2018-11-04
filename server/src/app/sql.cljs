(ns app.sql
  (:require
   [honeysql.core :as sql]
   [honeysql.helpers :refer [build-clause]]
   [honeysql-postgres.format]))

(defmethod build-clause :default [name & args]
  (throw (js/Error. (str "No builder registered for clause '" name "'"))))

(defmethod build-clause :returning [_ m columns]
  (assoc m :returning columns))

(def build sql/build)

(defn format [m]
  (sql/format m
              :parameterizer :postgresql
              :quoting :ansi))

