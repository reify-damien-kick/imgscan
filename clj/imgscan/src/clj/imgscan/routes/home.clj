(ns imgscan.routes.home
  (:require
   [imgscan.layout :as layout]
   [imgscan.db.core :as db]
   [clojure.java.io :as io]
   [imgscan.middleware :as middleware]
   [ring.util.http-response :as response]
   ;-------------------------------------
   [encaje.core :refer [--]]
   [imgscan.goog :as goog]
   [struct.core :as st]))

(defn home-page [{:keys [flash] :as request}]
  (-- layout/render request "home.html"
      (merge {:images (db/get-images)}
             (select-keys flash [:imgfile :detect :errors]))))

(defn about-page [request]
  (layout/render request "about.html"))

(def image-schema
  [[:id st/positive]
   [:imgfile st/required st/string]
   [:detect st/boolean]
   [:scanned st/string]])

(defn img [imgfile]
  (some->> (seq imgfile) .s (str "img/") io/resource io/as-file))

(defn validate-image [params]
  (if-let [errors (first (st/validate params image-schema))]
    errors
    (let [{:keys [imgfile]} params]
      (when-not (some->> (img imgfile) .exists)
        {:imgfile (str "Image file \"" imgfile "\" does not exist")}))))

(defn normalize-image [params]
  (let [detect' (get params :detect true)]
    (assoc params :detect detect')))

(defn update-image! [{:keys [imgfile detect] :as params}]
  (st/validate! params image-schema)
  (when detect
    ;; ToDo: Image detection DB ops should be done in a single
    ;; transaction
    (doseq [label (goog/labels (img imgfile))]
      (-- db/create-image-objects!
          {:image-id (:id params)
           :imgobject-id (->> (db/create-imgobject! {:label label})
                              first :id)}))
    (db/update-image! (select-keys params [:id]))))

(defn create-image! [{:keys [params]}]
  (if-let [errors (validate-image params)]
    (assoc (response/found "/") :flash (assoc params :errors errors))
    (let [params' (normalize-image params)
          [{:keys [id]}] (db/create-image! params')]
      (try
        (update-image! (assoc params' :id id))
        (response/found "/")
        (catch Exception ex
          (let [ex-msg [(str "FAIL: update-image! => " ex)]
                flash (assoc params :errors {:imgfile ex-msg})]
            (assoc (response/found "/") :flash flash)))))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/about" {:get about-page}]
   ["/image" {:post create-image!}]])
