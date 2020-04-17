(ns ^:figwheel-hooks nixy.core
  (:require
   [nixy.guide :as guide]
   [nixy.state :as state]
   [nixy.view :as view]
   [nixy.command.exec :as exec]
   [goog.events :as gevents]))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (view/render @state/app-state))

(defn on-key-down [e]
  (reset!
   state/app-state
   (exec/press-key @state/app-state (.-key e)))
  (view/render @state/app-state))

(gevents/listen
 js/window
 goog.events.EventType.KEYDOWN
 on-key-down)

(view/render @state/app-state)
