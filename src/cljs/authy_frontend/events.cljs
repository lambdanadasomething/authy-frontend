(ns authy-frontend.events
  (:require
   [re-frame.core :as re-frame]
   [authy-frontend.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ["zxcvbn" :as passwordstrengthmeter]
   ))

(defn gauge-password [password]
  (js->clj (passwordstrengthmeter password) :keywordize-keys true))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::update-state
 (fn-traced [db [_ keys new-value]]
            (cond-> db
              true (assoc-in keys new-value)
              (= [:password] keys) (assoc :gauge (gauge-password new-value)))))

;(assoc-in db keys new-value)
