(ns nixy.command.generic
  (:require
   [nixy.command :as command]))

(defmethod command/args-pred :enter-only [{:keys [args]}]
  (= "\n" args))
