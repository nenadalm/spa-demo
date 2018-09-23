(ns app.routes.user
  (:require
   [cljs.spec.alpha :as s]))

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

(def routes
  ["/user" {:get {:responses {200 {:body :response/user-list}}
                  :handler (fn [req res raise]
                             (res
                              {:status 200
                               :headers {}
                               :body {:data [{:id 1
                                              :type :user
                                              :attributes {:name "Stan Smith"}}
                                             {:id 2
                                              :type :user
                                              :attributes {:name "Steve Smith"}}]}}))}
            :post {:responses {201 {:body :response/user}
                               400 {:body :response/error-list}}
                   :parameters {:body :request/user-create}
                   :handler (fn [req res raise]
                              (res
                               {:status 201
                                :headers {}
                                :body (update (get-in req [:parameters :body])
                                              :data
                                              #(assoc % :id 3))}))}}])

