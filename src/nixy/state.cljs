(ns nixy.state
  (:require
   [nixy.command.generic]
   [nixy.command.ls]
   [nixy.command.clear]))

(def ^:private initial-state
  {:filesystem
   {"bin"
    {"ls"
     {:mod #{:x}
      :exec :ls
      :args-pred :enter-only}
     "clear"
     {:mod #{:x}
      :exec :clear
      :args-pred :enter-only}}}
   :cwd []

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
