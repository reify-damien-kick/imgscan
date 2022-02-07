(ns imgscan.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [imgscan.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[imgscan started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[imgscan has shut down successfully]=-"))
   :middleware wrap-dev})
