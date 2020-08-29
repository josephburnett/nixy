(ns nixy.command.ls
  "Ls lists the files and directories in the current working 
   directory."
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]))

(defmethod command/exec :ls
  ;; List the files in the current working directory given `state` and
  ;; output the names one per line to `stdout`.
  [{:keys [state]}]
  (let [fs (terminal/fs state)
        cwd (get-in state [fs :filesystem :cwd])
        names (as-> state s
                (get-in s (concat [fs :filesystem :root] cwd))
                (keys s)
                (into [] s))]
    {:state state :stdout names}))
