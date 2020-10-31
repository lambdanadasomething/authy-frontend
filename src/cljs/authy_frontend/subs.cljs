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
 ::current-route
 (fn [db]
   (get-in db [:matched-reitit :data :name])))

(re-frame/reg-sub
 ::login-error
 (fn [db]
   (:login-err db)))

(re-frame/reg-sub
 ::mfa-type
 (fn [db]
   (:mfa-type db)))

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