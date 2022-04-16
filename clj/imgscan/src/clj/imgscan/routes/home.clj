(ns imgscan.routes.home
  (:require
   [imgscan.layout :as layout]
   [imgscan.db.core :as db]
   #_[clojure.java.io :as io]
   [imgscan.middleware :as middleware]
   [ring.util.response]
   #_[ring.util.http-response :as response]
   ; --------------------------------------
   [encaje.core :refer [--]]))

(defn home-page [request]
  (-- layout/render request "home.html"
      {:images (db/get-images)}))

(defn about-page [request]
  (layout/render request "about.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/about" {:get about-page}]])

