(ns authy-frontend.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::value
 (fn [db]
   db))

(re-frame/reg-sub
 ::error
 (fn [db]
   {}))

(re-frame/reg-sub
 ::password-strength
 (fn [db]
   (get-in db [:gauge :score])))

(re-frame/reg-sub
 ::password-feedback
 (fn [db]
   (get-in db [:gauge :feedback])))

(re-frame/reg-sub
 ::user-state
 (fn [db]
   (get db :user-state)))

(re-frame/reg-sub
 ::user-available?
 (fn [db]
   (get db :available?)))