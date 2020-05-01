(ns nixy.command.ls
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]))

(defmethod command/exec :ls [_ state _]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :filesystem :cwd])
        names (as-> state s
                (get-in s (concat [fs :filesystem :root] cwd))
                (keys s))]
    (terminal/append
     state
     (concat names
             [(str (terminal/prompt state) "ls")]))))
