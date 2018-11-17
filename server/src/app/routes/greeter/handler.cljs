(ns app.routes.greeter.handler
  (:require
   [cljs.spec.alpha :as s]))

(s/def :parameters.greeter/name string?)
(s/def :parameters/greeter (s/keys :req-un [:parameters.greeter/name]))

(def routes
  ["/greeter" {:get {:tags [:greeter]
                     :responses {200 {:body string?}}
                     :parameters {:query :parameters/greeter}
                     :handler (fn [req res raise]
                                (res {:status 200
                                      :headers {}
                                      :body (str "Hello " (get-in req [:parameters :query :name]) "!")}))}}])
