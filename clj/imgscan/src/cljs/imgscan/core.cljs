(ns imgscan.core)

(-> (.getElementById js/document "content")
    (.-innerHTML)
    (set! "Hello, nurse!"))
