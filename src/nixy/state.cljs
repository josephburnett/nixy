(ns nixy.state
  (:require
   [nixy.command.ls :as ls]))

(def ^:private initial-state
  {:filesystem
   {"bin"
    {"ls"
     {:mod #{:x}
      :exec ls/exec
      :args-pred ls/args-pred}}}

   :cwd []

   ;; TODO: update this on the 'resize' event.
   :view
   {:width 500
    :height 500}
   
   :character
   {:appearance
    {:blinking :true
     :color "#fff"}
    :speech-bubble-text []}

   :terminal
   {:line ""
    :output []}

   :errors []})

(def app-state (atom initial-state))
