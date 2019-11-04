(ns ^:figwheel-hooks nixy.core
  (:require
   [goog.dom :as gdom]))

(defn multiply [a b] (* a b))

(def initial-state
  {:file-system
   {}
   
   :character
   {:appearance
    {:blinking :true
     :color "#fff"}
    :speech-bubble-text []}

   :terminal
   {:line-chars []
    :line-guide {}
    :line-history []}})

(defonce app-state (atom initial-state))

(defn get-app-element []
  (gdom/getElement "app"))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
