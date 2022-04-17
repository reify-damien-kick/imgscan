(ns imgscan.routes.home
  (:require
   [imgscan.layout :as layout]
   [imgscan.db.core :as db]
   [clojure.java.io :as io]
   [imgscan.middleware :as middleware]
   [ring.util.http-response :as response]
   ;------------------------------------
   [encaje.core :refer [--]]))

(defn home-page [request]
  (-- layout/render request "home.html"
      {:images (db/get-images)}))

(defn about-page [request]
  (layout/render request "about.html"))

(defn create-image! [{:keys [params]}]
  (let [{:keys [imgfile]} params]
    (when (some->> (seq imgfile) .s (str "img/")
                   io/resource io/as-file .exists)
      (-- db/create-image!
          (let [detect (get params :detect true)]
            (assoc params :detect detect)))))
  (response/found "/"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/about" {:get about-page}]
   ["/imgscan" {:post create-image!}]])

