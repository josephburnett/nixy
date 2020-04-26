(ns nixy.command.ls
  (:require
   [nixy.command :as command]))

(defmethod command/exec :ls [_ state _]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :cwd])]
    (update-in
     state [:terminal :output]
     #(let [names (as-> state s
                    (get-in s (concat [fs :filesystem] cwd))
                    (keys s))]
        (concat % names)))))
