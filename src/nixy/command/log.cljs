(ns nixy.command.log
  "Log output the system log entries."
  (:require
   [nixy.command :as command]))

(defmethod command/exec :log
  ;; Output system log
  [{:keys [state]}]
  {:stdout (:log state)
   :state state})
