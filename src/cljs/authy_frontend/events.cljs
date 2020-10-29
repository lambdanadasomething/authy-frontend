(ns authy-frontend.events
  (:require
   [re-frame.core :as re-frame]
   [authy-frontend.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ["zxcvbn" :as passwordstrengthmeter]
   [day8.re-frame.http-fx]
   [ajax.formats :as f]
   [cljs.tools.reader.edn :as edn]
   ))

(defn gauge-password [password]
  (js->clj (passwordstrengthmeter password) :keywordize-keys true))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(def timers (atom {}))

(defn update-timers-purge [timers k purge-list]
  (update timers k (fn [x] (remove #(contains? (set purge-list) %) x))))

(defn update-timers-add [timers k id]
  (update timers k #(conj % id)))

(re-frame/reg-event-fx
 ::userid-input
 (fn-traced [{:keys [db]} [_ a]]
            {:db db
             :fx [[:timer :userid]]}))

(re-frame/reg-fx
 :timer
 (fn [k]
   (let [existings (get @timers k)]
     (dorun (for [x existings]
       (js/clearTimeout x)))
     (swap! timers update-timers-purge k existings)
     (let [new (js/setTimeout #(re-frame/dispatch-sync [::check-userid]) 3000)]
       (swap! timers update-timers-add k new)))))


(re-frame/reg-event-fx
 ::update-state
 (fn-traced [{:keys [db]} [_ keys new-value]]
            {:db (cond-> db
                   true (assoc-in keys new-value)
                   true (assoc :user-state :idle)
                   (= [:password] keys) (assoc :gauge (gauge-password new-value)))
             :dispatch [::userid-input]}))

;(assoc-in db keys new-value)

(re-frame/reg-event-fx
 ::check-userid
 (fn-traced [{:keys [db]} _]
            (let [{:keys [user-state epoch user-id]} db]
              (if (= user-state :idle)
                {:db (-> db
                         (assoc :user-state :loading)
                         (update :epoch inc))
                 :http-xhrio {:method :get
                              :uri (str "http://172.30.0.22:7070/authy/check-user-availability?userid=" user-id)
                              :response-format (f/raw-response-format)
                              :on-success  [::report-user-availability]}}
                {:db db}))))

(re-frame/reg-event-db
 ::report-user-availability
 (fn-traced [db [_ result]]
            (let [parsed (edn/read-string result)
                  available? (get parsed :available?)]
              (-> db
                  (assoc :user-state :finished)
                  (assoc :available? available?)))))
