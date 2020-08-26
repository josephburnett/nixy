(ns nixy.command)

;; Execute the command. Provided keys:
;; - state -- the current application state
;; - args  -- the arguments provided to the command
;; - stdin -- a list of lines output from a previous command
;; (as well as all keys from the job definition)
(defmulti exec
  (fn [{:keys [exec-fn]}] exec-fn))

;; Given a partially formed set of parameters, return a map tagging
;; the potential of the parameters. E.g. {:valid true}
;; Will be called for each valid key extending the current parameters
;; to build a map of what can be input next.
;; 
(defmulti args
  (fn [{:keys [args-fn]}] args-fn))

(defn archive-line [state]
  (let [line (get-in state [:terminal :line])
        fs (get-in state [:terminal :fs])
        history (cons line (get-in state [fs :history]))]
    (as-> state s
      (assoc-in s [:terminal :line] "")
      (assoc-in s [fs :history] history))))
