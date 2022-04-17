(ns imgscan.routes.home
  (:require
   [imgscan.layout :as layout]
   [imgscan.db.core :as db]
   [clojure.java.io :as io]
   [imgscan.middleware :as middleware]
   [ring.util.http-response :as response]
   ;-------------------------------------
   [encaje.core :refer [--]]
   [struct.core :as st]))

(defn home-page [{:keys [flash] :as request}]
  (-- layout/render request "home.html"
      (merge {:images (db/get-images)}
             (select-keys flash [:imgfile :detect :errors]))))

(defn about-page [request]
  (layout/render request "about.html"))

(def image-schema
  [[:imgfile st/required st/string]
   [:detect st/boolean]])

(defn validate-image [params]
  (if-let [errors (first (st/validate params image-schema))]
    errors
    (let [{:keys [imgfile]} params]
      (assert (some? imgfile))     ; already validated
      (when-not (some->> (seq imgfile) .s (str "img/")
                         io/resource io/as-file .exists)
        {:imgfile (str "Image file '" imgfile "' does not exist")}))))

(defn create-image! [{:keys [params]}]
  (if-let [errors (validate-image params)]
    (assoc (response/found "/")
           :flash (assoc params :errors errors))
    (do (-- db/create-image!
            (let [detect (get params :detect true)]
              (assoc params :detect detect)))
        (response/found "/"))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/about" {:get about-page}]
   ["/imgscan" {:post create-image!}]])

