(ns nixy.command)

(defmulti exec
  (fn [{:keys [exec-fn]}] exec-fn))

(defmulti args
  (fn [{:keys [args-fn]}] args-fn))

(defn archive-line [state]
  (let [line (get-in state [:terminal :line])
        fs (get-in state [:terminal :fs])
        history (cons line (get-in state [fs :history]))]
    (as-> state s
      (assoc-in s [:terminal :line] "")
      (assoc-in s [fs :history] history))))
