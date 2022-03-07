(ns imgscan.util
  (:require [clojure.java.io :as io]
            [encaje.core :refer [-- -||]])
  (:import [java.io Closeable File]))

(defn temp-imgfile
  ([] (temp-imgfile "xxx"))
  ([prefix] (temp-imgfile prefix ".jpg"))
  ([prefix suffix]
   (-- File/createTempFile prefix suffix
       (-> (io/resource "img") .getPath File.))))
