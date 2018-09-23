(ns app.routes
  (:require
   [reitit.ring :as ring]
   [reitit.core :as r]
   [reitit.coercion :as coercion]
   [reitit.ring.coercion :as rcoercion]
   [reitit.coercion.spec]
   [reitit.swagger :as swagger]
   [macchiato.middleware.resource]
   [macchiato.util.response :as response]
   [macchiato.middleware.restful-format :as rf]
   [cljs.nodejs :as node]
   [cljs.spec.alpha :as s]
   [spec-tools.spec :as spec]
   [spec-tools.core :as st]
   [phrase.alpha :as p]
   [app.config :as config]
   [spec-tools.swagger.core :as swagger-core]
   [app.routes.user :refer [routes] :rename {routes user-routes}]))

(def swagger-ui (node/require "swagger-ui-dist"))

(s/def :response.error.source/pointer string?)
(s/def :response.error/source (s/keys :req-un [:response.error.source/pointer]))
(s/def :response.error/code string?)
(s/def :response/error (s/keys :req-un [:response.error/code
                                        :response.error/source]))
(s/def :response/errors (s/coll-of :response/error))
(s/def :response/error-list (s/keys :req-un [:response/errors]))

(defn sync-handler->async-handler
  "Converts sync ring handler into async one."
  [handler]
  (fn [req res raise]
    (try
      (res (handler req))
      (catch :default e
        (raise e)))))

(defn url [req path]
  (str (name (:scheme req))
       "://"
       (get-in req [:headers "host"])
       path))

(defn extract-request-format [_ _]
  :json)

(defn extract-response-format [_ _]
  :json)

(def parameter-coercion
  (assoc coercion/default-parameter-coercion
         :body (coercion/map->ParameterCoercion {:in :body
                                                 :style :body
                                                 :keywordize? true
                                                 :open? false})))

(defn request-coercion-error? [e]
  (and (instance? ExceptionInfo e)
       (= (-> e .-data :type) ::coercion/request-coercion)))

(defn problem->error [problem]
  (let [error (p/phrase {} problem)
        path (str "/" (clojure.string/join "/" (map name (:path problem))))]
    {:code error
     :source {:pointer path}}))

(defn phrase-errors [e]
  (let [problems (-> e .-data :problems)]
    (map problem->error problems)))

(defn exception-middleware [handler]
  (fn [req res _]
    (let [raise (fn [e]
                  (prn e)
                  (if (request-coercion-error? e)
                    (res {:status 400
                          :body (phrase-errors e)})
                    (res {:status 500})))]
      (try
        (handler req res raise)
        (catch :default e
          (raise e))))))

(defn sync-exception-middleware [handler]
  (fn [req res raise]
    (try
      (handler req res raise)
      (catch :default e
        (raise e)))))

(defn create-router []
  (ring/router
   [["/swagger.json"
     {:get {:no-doc true
            :swagger {:info {:title "spa demo"}}
            :handler (sync-handler->async-handler (swagger/create-swagger-handler))}}]
    ["/config.js"
     {:get {:no-doc true
            :handler (fn [req res raise]
                       (let [config #js {:url (url req "/swagger.json")}]
                         (res {:status 200
                               :headers {"Content-Type" "application/javascript"}
                               :body (str "window.APP_CONFIG =" (.stringify js/JSON config))})))}}]
    user-routes]

   {::coercion/parameter-coercion parameter-coercion
    ::coercion/extract-request-format extract-request-format
    ::coercion/extract-response-format extract-response-format
    :data {:compile coercion/compile-request-coercers
           :coercion reitit.coercion.spec/coercion
           :middleware [rf/wrap-restful-format
                        exception-middleware
                        rcoercion/coerce-response-middleware
                        sync-exception-middleware ;; coerce-request-middleware can throw exception on invalid data
                        rcoercion/coerce-request-middleware]}}))

(defn create-handler []
  (ring/ring-handler
   (create-router)
   (ring/routes
    (-> (ring/create-default-handler)
        (macchiato.middleware.resource/wrap-resource (.absolutePath swagger-ui))
        (macchiato.middleware.resource/wrap-resource "resources/public")))))

(def handler (if config/debug?
               (fn [req res raise]
                 ((create-handler) req res raise))
               (create-handler)))
