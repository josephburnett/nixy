(ns nixy.command)

(defmulti exec
  (fn [file state args] (:exec file)))

(defmulti args-pred
  (fn [file args] (:args-pred file)))

(defn archive-line [state]
  (let [line (get-in state [:terminal :line])
        history (cons line (get-in state [:terminal :history]))]
    (as-> state s
      (assoc-in s [:terminal :line] "")
      (assoc-in s [:terminal :history] history))))
