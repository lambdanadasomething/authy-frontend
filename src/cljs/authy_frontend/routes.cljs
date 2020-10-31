(ns authy-frontend.routes
  (:require [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.frontend.controllers :as rfc]
            [reitit.core :as r]
            [re-frame.core :as re-frame]))

(def routes
  (rf/router 
   ["/"
    ["" {:name ::front-page}]
    ["login" {:name ::login-page}
     ["/mfa" {:name ::mfa-page}]]
    ["signup" {:name ::signup-page}]
    ["admin"
     ["/test" {:name ::admin-test-page}]]]))

(defn start-router []
  (rfe/start! routes 
              (fn [new-match]
                (re-frame/dispatch [:authy-frontend.events/navigate new-match]))
              {:use-fragment false}))

(defn trigger-route [name]
  (r/match-by-name routes name))
