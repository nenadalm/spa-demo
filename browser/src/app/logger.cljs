(ns app.logger
  (:require
   [sentry]
   [app.config :refer [config]]))

(defn- sentry-error [e]
  (cond
    (string? e) (.captureMessage sentry e)
    :else (.captureException sentry e)))

(defn init []
  (let [dsn (get-in config [:logging :sentryPublicDsn])]
    (when (not-empty dsn)
      (.init sentry #js {:dsn dsn})
      (.addEventListener js/window "error" (fn [e] (sentry-error (.-error e)))))))
