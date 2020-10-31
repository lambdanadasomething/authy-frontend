(ns authy-frontend.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [authy-frontend.events :as events]
   [authy-frontend.views :as views]
   [authy-frontend.config :as config]
   [authy-frontend.routes :as routes]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (routes/start-router)
  (dev-setup)
  (mount-root))
