(ns authy-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [authy-frontend.subs :as subs]
   [authy-frontend.events :as events]
   [free-form.re-frame :as free-form]
   ))

(def color-mapping
  {0 "bg-danger"
   1 "bg-danger"
   2 "bg-warning"
   3 "bg-success"
   4 "bg-success"})

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        values (re-frame/subscribe [::subs/value])
        errors (re-frame/subscribe [::subs/error])
        password-score (re-frame/subscribe [::subs/password-strength])
        password-percent (* (+ @password-score 1) 20)
        password-color (get color-mapping @password-score)]
    [:div.container
     [:div.row
      [:div.col
       [:h1 "Hello from hi " @name]
       [free-form/form @values @errors ::events/update-state
        [:form
         [:div.form-group
          [:label {:for :user-id} "User ID:"]
          [:input.form-control {:free-form/input       {:key :user-id}
                                :free-form/error-class {:key :text :error "error"}
                                :type                  :text
                                :id                    :user-id}]]
         [:div.form-group
          [:label {:for :password} "Password:"]
          [:input.form-control {:free-form/input       {:key :password}
                                :free-form/error-class {:key :text :error "error"}
                                :type                  :password
                                :id                    :password}]
          [:div.progress {:style {:margin-top "20px"}}
           [:div.progress-bar {:role "progressbar"
                               :style {:width (str password-percent "%")}
                               :class password-color}]]]]]]
      [:div.col]]
     ]))
