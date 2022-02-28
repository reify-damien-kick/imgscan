(ns imgscan.goog
  (:require [clojure.java.io :as io])
  (:import [com.google.cloud.vision.v1
            AnnotateImageRequest Feature Feature$Type Image
            ImageAnnotatorClient]
           [com.google.protobuf ByteString]
           [java.util ArrayList]))

(defn labels [imgfile]
  (with-open [client (ImageAnnotatorClient/create)]
    client))

(defn file->bytes [file]
  (with-open [in (-> file io/resource io/input-stream)
              out (java.io.ByteArrayOutputStream.)]
    (io/copy in out)
    (ByteString/copyFrom (.toByteArray out))))

(defn bytes->image [bytes]
  (.. Image newBuilder (setContent bytes) build))

(defn image->request [image]
  (let [featr (.. Feature newBuilder
                  (setType Feature$Type/LABEL_DETECTION)
                  build)]
    (.. AnnotateImageRequest newBuilder
        (addFeatures featr) (setImage image) build)))

(defn request->requests [request]
  (doto (ArrayList.) (.add request)))

(defn requests->response [requests]
  (with-open [client (ImageAnnotatorClient/create)]
    (.. client (batchAnnotateImages requests))))

(defn response->responses [response]
  (.. response getResponsesList))

(defn responses->annotations-errors [responses]
  )

(defn file->request [file]
  (-> file file->bytes bytes->image image->request))

(defn file->requests [file]
  (-> file file->request request->requests))

(defn file->response [file]
  (-> file file->requests requests->response))
