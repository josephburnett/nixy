(ns nixy.command.generic
  "Generic dispatch functions for commands."
  (:require
   [nixy.command :as command]))

(defmethod command/args :enter-only
  ;; Accept no arguments.
  [{:keys [args]}]
  (if (= "\n" args) {:valid true} {}))
