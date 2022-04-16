(ns imgscan.handler-test
  (:require
    [clojure.test :refer [deftest is testing use-fixtures]]
    [ring.mock.request :refer [request]]
    [imgscan.config :as config]
    [imgscan.handler :as handler]
    [imgscan.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'config/env #'handler/app-routes)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response ((handler/app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((handler/app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))
