(ns ^:figwheel-hooks nixy.core
  (:require
   [nixy.command.all]
   [nixy.event :as event]
   [goog.events :as gevents]
   [nixy.focus.laptop]
   [nixy.focus.dialog]
   [nixy.guide :as guide]
   [nixy.job.all]
   [nixy.state :as state]
   [nixy.state.plot :as plot]
   [nixy.view :as view]))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (view/render @state/app-state))

(defn on-key-down [e]
  (reset!
   state/app-state
   (event/press-key @state/app-state (.-key e)))
  (view/render @state/app-state))

(gevents/listen
 js/window
 goog.events.EventType.KEYDOWN
 on-key-down)

(swap! state/app-state plot/advance)
(view/render @state/app-state)
