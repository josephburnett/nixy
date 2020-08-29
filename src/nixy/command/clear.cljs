(ns nixy.command.clear
  "Clear removes all output from the terminal."
  (:require
   [nixy.command :as command]))

(defmethod command/exec :clear
  ;; Clear the terminal.
  [{:keys [state]}]
  {:stdout []
   :state
   (as-> state s
     (assoc-in s [:terminal :output] [])
     (assoc-in s [:terminal :line] []))})
