(ns nixy.command.generic
  (:require
   [nixy.command :as command]))

(defmethod command/args :enter-only [{:keys [args]}]
  (if (= "\n" args) {:valid true} {}))
