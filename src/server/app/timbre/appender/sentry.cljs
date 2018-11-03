(ns app.timbre.appender.sentry
  (:require
   ["@sentry/node" :as sentry]))

(def ^:private timbre->sentry-levels
  {:trace  "debug"
   :debug  "debug"
   :report "info"
   :info   "info"
   :warn   "warning"
   :error  "error"
   :fatal  "fatal"})

(defn sentry-appender [dsn]
  (.init sentry #js {:dsn dsn})
  {:enabled? true
   :min-level :warn
   :rate-limit nil
   :fn (fn [data]
         (let [{:keys [level msg_ context ?err]} data
               event (merge {:message (force msg_)
                             :level (get timbre->sentry-levels level)}
                            (when context {:extra context}))]
           (if ?err
             (.captureException sentry ?err)
             (.captureMessage sentry (:message event) (:level event)))))})
