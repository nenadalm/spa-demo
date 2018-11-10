(ns app.routes.user.handler
  (:require
   [cljs.spec.alpha :as s]
   [app.routes.user.query :as q]))

(s/def :resource.user/id int?)
(s/def :resource.user/type string?)
(s/def :resource.user.attributes/name string?)
(s/def :resource.user/attributes (s/keys :req-un [:resource.user.attributes/name]))
(s/def :resource/user (s/keys :req-un [:resource.user/id
                                       :resource.user/type
                                       :resource.user/attributes]))

(s/def :response.user-list/data (s/coll-of :resource/user))
(s/def :response/user-list (s/keys :req-un [:response.user-list/data]))

(s/def :response.user/data :resource/user)
(s/def :response/user (s/keys :req-un [:response.user/data]))

(s/def :request.user-create/data (s/keys :req-un [:resource.user/type
                                                  :resource.user/attributes]))
(s/def :request/user-create (s/keys :req-un [:request.user-create/data]))

(defn user-row->response-data [row]
  {:id (:id row)
   :type "user"
   :attributes {:name (:name row)}})

(def routes
  ["/user" {:get {:responses {200 {:body :response/user-list}}
                  :handler (fn [req res raise]
                             (-> (q/list)
                                 (.then (fn [result]
                                          (res {:status 200
                                                :headers {}
                                                :body {:data (map user-row->response-data
                                                                  result)}})))))}
            :post {:responses {201 {:body :response/user}
                               400 {:body :response/error-list}}
                   :parameters {:body :request/user-create}
                   :handler (fn [req res raise]
                              (-> (q/create (get-in req [:parameters :body :data :attributes]))
                                  (.then (fn [result]
                                           ;; TODO: response gets stuck somewhere since reitit 0.2.4 (tested also with 0.2.6)
                                           (res
                                            {:status 201
                                             :headers {}
                                             :body {:data (first (map user-row->response-data
                                                                      result))}})))))}}])
