(ns nixy.command.generic
  (:require
   [nixy.command :as command]))

(defmethod command/args-pred :enter-only [_ _ args]
  (= "\n" args))
