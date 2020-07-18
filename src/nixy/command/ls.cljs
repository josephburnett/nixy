(ns nixy.command.ls
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]))

(defmethod command/exec :ls [_ state _ _]
  (let [fs (terminal/fs state)
        cwd (get-in state [fs :filesystem :cwd])
        names (as-> state s
                (get-in s (concat [fs :filesystem :root] cwd))
                (keys s)
                (into [] s))]
    {:state state :stdout names}))
