(ns nixy.state
  (:require
   [nixy.filesystem.nixy :as nixy]))

(def ^:private initial-state
  {:filesystem nixy/fs
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
