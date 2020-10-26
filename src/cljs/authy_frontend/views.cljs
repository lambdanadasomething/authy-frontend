(ns authy-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [authy-frontend.subs :as subs]
   [authy-frontend.events :as events]
   [free-form.re-frame :as free-form]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        values (re-frame/subscribe [::subs/value])
        errors (re-frame/subscribe [::subs/error])]
    [:div
     [:h1 "Hello from hi " @name]
     [free-form/form @values @errors ::events/update-state
      [:form.form-horizontal
       [:div.form-group {:free-form/error-class {:key :email :error "has-error"}}
        [:label.col-sm-2.control-label {:for :email} "Email"]
        [:div.col-sm-10 [:input.form-control {:free-form/input {:key :email}
                                              :type            :email
                                              :id              :email}]
         [:div.text-danger {:free-form/error-message {:key :email}} [:p]]]]]]
     ]))
