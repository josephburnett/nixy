(ns nixy.command.ls
  (:require
   [nixy.command.dispatch :as dispatch]))

(defmethod dispatch/exec :ls [_ state _]
  (let [fs (get-in state [:terminal :fs])]
    (update-in
     state [:terminal :output]
     #(let [names (as-> state s
                    (get-in s (concat [fs :filesystem] (:cwd s)))
                    (keys s))]
        (concat % names)))))
