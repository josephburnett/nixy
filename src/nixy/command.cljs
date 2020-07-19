(ns nixy.command)

(defmulti exec
  (fn [{:keys [exec]}] exec))

(defmulti args-pred
  (fn [{:keys [args-pred]}] args-pred))

(defn archive-line [state]
  (let [line (get-in state [:terminal :line])
        fs (get-in state [:terminal :fs])
        history (cons line (get-in state [fs :history]))]
    (as-> state s
      (assoc-in s [:terminal :line] "")
      (assoc-in s [fs :history] history))))
