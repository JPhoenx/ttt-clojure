(ns ttt-clojure.ring-adapter
  (:require [ttt-clojure.ttt-handlers :as h]
            [ring.util.response :as response]
            [ring.middleware.reload :as reload]
            [ring.middleware.params :as params]))

(defn- get-route [uri]
  (re-find #"\D+" uri))

(defn wrap-route [handler]
  (fn [request]
    (let [route (get-route (:uri request))]
          (handler (assoc request :route route)))))

(def get-my-handler
  (fn [request]
    ((get-in h/handlers
            [(:request-method request)
             (:route request)])
    request)))

(def app
  (-> get-my-handler
      (wrap-route)
      (params/wrap-params)
      (reload/wrap-reload)))