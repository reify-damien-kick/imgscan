(ns imgscan.core
  (:require #_[reagent.core :as r]
            [reagent.dom :as dom]))

(dom/render [:h1 "Hello, nurse!"] (.getElementById js/document "content"))
