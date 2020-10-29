(ns authy-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [authy-frontend.subs :as subs]
   [authy-frontend.events :as events]
   [free-form.re-frame :as free-form]
   [reagent.dom.server :as rserv]))

(def color-mapping
  {0 "danger"
   1 "danger"
   2 "warning"
   3 "success"
   4 "success"})

(def wording-mapping
  {0 "Too weak"
   1 "Weak"
   2 "Medium"
   3 "Strong"
   4 "Perfect!"})

;; Original from bootstrap icons
;; <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-question-circle-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
;;<path fill-rule="evenodd" d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.496 6.033a.237.237 0 0 1-.24-.247C5.35 4.091 6.737 3.5 8.005 3.5c1.396 0 2.672.73 2.672 2.24 0 1.08-.635 1.594-1.244 2.057-.737.559-1.01.768-1.01 1.486v.105a.25.25 0 0 1-.25.25h-.81a.25.25 0 0 1-.25-.246l-.004-.217c-.038-.927.495-1.498 1.168-1.987.59-.444.965-.736.965-1.371 0-.825-.628-1.168-1.314-1.168-.803 0-1.253.478-1.342 1.134-.018.137-.128.25-.266.25h-.825zm2.325 6.443c-.584 0-1.009-.394-1.009-.927 0-.552.425-.94 1.01-.94.609 0 1.028.388 1.028.94 0 .533-.42.927-1.029.927z"/>
;;</svg>
(defn svg-help []
 [:svg {:width "1em" :height "1em" :viewBox "0 0 16 16" :class "bi bi-question-circle-fill" :fill "currentColor" :xmlns "http://www.w3.org/2000/svg"}
  [:path {:fill-rule "evenodd" :d "M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.496 6.033a.237.237 0 0 1-.24-.247C5.35 4.091 6.737 3.5 8.005 3.5c1.396 0 2.672.73 2.672 2.24 0 1.08-.635 1.594-1.244 2.057-.737.559-1.01.768-1.01 1.486v.105a.25.25 0 0 1-.25.25h-.81a.25.25 0 0 1-.25-.246l-.004-.217c-.038-.927.495-1.498 1.168-1.987.59-.444.965-.736.965-1.371 0-.825-.628-1.168-1.314-1.168-.803 0-1.253.478-1.342 1.134-.018.137-.128.25-.266.25h-.825zm2.325 6.443c-.584 0-1.009-.394-1.009-.927 0-.552.425-.94 1.01-.94.609 0 1.028.388 1.028.94 0 .533-.42.927-1.029.927z"}]]
)

(defn render-password-help [{:keys [warning suggestions]}]
  (rserv/render-to-static-markup
   [:span [:p warning] (vec (cons :ul (for [x suggestions] [:li x])))]))

;Run this at beginning (. (js/$ "#passwordhelp") tooltip)

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        values (re-frame/subscribe [::subs/value])
        errors (re-frame/subscribe [::subs/error])
        password-score (re-frame/subscribe [::subs/password-strength])
        password-feedback (re-frame/subscribe [::subs/password-feedback])
        password-percent (* (+ @password-score 1) 20)
        password-color (str "bg-" (get color-mapping @password-score))
        strength-color (str "text-" (get color-mapping @password-score))
        _ (. (js/$ "#passwordhelp") tooltip)
        user-state (re-frame/subscribe [::subs/user-state])
        available? (re-frame/subscribe [::subs/user-available?])]
    [:div.container
     [:div.row
      [:div.col
       [:h1 "Hello from hi " @name]
       [free-form/form @values @errors ::events/update-state
        [:form
         [:div.form-group
          [:label {:for :user-id} "User ID:"]
          [:div.clearfix
           [:input.form-control (cond->
                                 {:free-form/input       {:key :user-id}
                                  :free-form/error-class {:key :text :error "error"}
                                  :type                  :text
                                  :id                    :user-id}
                                  (= @user-state :finished) (assoc :class (if @available? "is-valid" "is-invalid")))]
           (if (= @user-state :loading)
             [:div.spinner-border.text-secondary.float-right {:role "status"} [:span.sr-only "Loading..."]])]]
         [:div.valid-feedback "Hey man"]
         [:div.form-group
          [:label {:for :password} "Password:"]
          [:input.form-control {:free-form/input       {:key :password}
                                :free-form/error-class {:key :text :error "error"}
                                :type                  :password
                                :id                    :password}]
          [:div.progress {:style {:margin-top "20px"}}
           [:div.progress-bar {:role "progressbar"
                               :style {:width (str password-percent "%")}
                               :class password-color}]]
          [:div {:style {:margin-top "10px"}}
           [:span "Password Strength: "]
           [:span {:class strength-color} (get wording-mapping @password-score)]
           [:span#passwordhelp {:data-toggle "tooltip" :data-placement "right" :data-html true :title (render-password-help @password-feedback)} [svg-help]]]]]]]
      [:div.col]]
     ]))
