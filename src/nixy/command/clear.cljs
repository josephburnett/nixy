(ns nixy.command.clear
  (:require
   [nixy.command.dispatch :as dispatch]))

(defmethod dispatch/exec :clear [_ state _]
  (as-> state s
    (assoc-in s [:terminal :output] [])
    (assoc-in s [:terminal :line] [])))
