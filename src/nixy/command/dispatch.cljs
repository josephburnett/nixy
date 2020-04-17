(ns nixy.command.dispatch)

(defmulti exec
  (fn [file state args] (:exec file)))
(defmulti args-pred
  (fn [file args] (:args-pred file)))
