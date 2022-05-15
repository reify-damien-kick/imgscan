(ns imgscan.goog
  (:require [clojure.java.io :as io])
  (:import [com.google.cloud.vision.v1 AnnotateImageRequest Feature
            Feature$Type Image ImageAnnotatorClient]
           [com.google.protobuf ByteString]
           [java.util ArrayList]))

(defn ->bytes [file]
  (with-open [in (io/input-stream file)
              out (java.io.ByteArrayOutputStream.)]
    (io/copy in out)
    (ByteString/copyFrom (.toByteArray out))))

(defn ->image [bytes]
  (.. Image newBuilder (setContent bytes) build))

(defn ->request [image]
  (let [labels (.. Feature newBuilder
                   (setType Feature$Type/LABEL_DETECTION) build)]
    (.. AnnotateImageRequest newBuilder
        (addFeatures labels) (setImage image) build)))

(defn ->requests [request]
  (doto (ArrayList.) (.add request)))

(defn ->response [requests]
  (with-open [client (ImageAnnotatorClient/create)]
    (. client (batchAnnotateImages requests))))

(defn ->responses [response]
  (.getResponsesList response))

(defn ->annotations [responses]
  (let [{errors true, annotations false}
        ,, (group-by #(.hasError %) responses)]
    {:annotations annotations, :errors errors}))

(defn ->labels [annotation]
  (->> (map #(.getDescription %) (.getLabelAnnotationsList annotation))
       (remove nil?)))

(defonce ^:dynamic *labels* {})

(defn labels [imgfile]
  (or (get *labels* imgfile)
      (let [{:keys [annotations]}
            ,, (-> imgfile ->bytes ->image ->request ->requests
                   ->response ->responses ->annotations)]
        (mapcat ->labels annotations))))
