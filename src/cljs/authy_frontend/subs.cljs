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
