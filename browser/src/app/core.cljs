(ns ^:figwheel-hooks app.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [app.config :as config]
   [app.logger :as logger]
   [app.views :as views]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/app]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (logger/init)
  (dev-setup)
  (re-frame/dispatch-sync [:init])
  (mount-root))

(defn ^:after-load after-load []
  (mount-root))
