(ns nixy.command.jobs
  "Jobs lists current, active and completed jobs."
  (:require
   [nixy.command :as command]))

(defmethod command/exec :jobs
  ;; List active jobs
  [{:keys [state]}]
  {:stdout (as-> state s
             (get-in s [:jobs :active])
             (map name s))
   :state state})
