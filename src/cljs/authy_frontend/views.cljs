(ns authy-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [authy-frontend.subs :as subs]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Hello from " @name]
     ]))