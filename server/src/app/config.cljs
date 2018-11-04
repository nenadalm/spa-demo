(ns app.config
  (:require
   [macchiato.env :as config]
   [mount.core :refer [defstate]]
   [cljs.spec.alpha :as s]
   [expound.alpha :as expound]))

(def debug? ^boolean goog.DEBUG)

(s/def :app.config/db-connection-string string?)
(s/def :app.config/host string?)
(s/def :app.config/port int?)
(s/def :app.config/sentry-dsn string?)
(s/def :app/config (s/keys :req-un [:app.config/db-connection-string
                                    :app.config/host
                                    :app.config/port
                                    :app.config/sentry-dsn]))

(defn load-config []
  (let [conf (config/env)]
    (if (s/valid? :app/config conf)
      conf
      (throw (js/Error. (str "Invalid config:\n" (expound/expound-str :app/config conf)))))))

(defstate env :start (load-config))
