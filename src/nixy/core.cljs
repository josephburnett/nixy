(ns ^:figwheel-hooks nixy.core
  (:require
   [nixy.character :as character]
   [nixy.guide :as guide]
   [nixy.state :as state]
   [clojure.string :as str]
   [goog.events :as gevents]))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (character/render))

(defn append-state-terminal-line [current-state key]
  (update-in
   current-state [:terminal :line]
   #(str/join (concat % key))))

(defn on-key-down [e]
  (let [current-state @state/app-state
        current-guide (guide/state->guide current-state)
        key (.-key e)]
    (when (contains? current-guide key)
      (reset!
       state/app-state
       (append-state-terminal-line current-state key))
      (character/render))))

(gevents/listen
 js/window
 goog.events.EventType.KEYDOWN
 on-key-down)

(character/render)
