(ns nixy.command.generic
  (:require
   [nixy.command.dispatch :as dispatch]))

(defmethod dispatch/args-pred :enter-only [_ args]
  (= "\n" args))