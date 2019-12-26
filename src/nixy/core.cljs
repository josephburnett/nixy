(ns ^:figwheel-hooks nixy.core
  (:require
   [nixy.guide :as guide]
   [nixy.state :as state]
   [nixy.terminal :as terminal]
   [nixy.view :as view]
   [goog.events :as gevents]))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (view/render))

(defn on-key-down [e]
  (reset!
   state/app-state
   (terminal/press-key @state/app-state (.-key e)))
  (view/render))

(gevents/listen
 js/window
 goog.events.EventType.KEYDOWN
 on-key-down)

(view/render)
