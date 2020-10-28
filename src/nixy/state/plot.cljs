(ns nixy.state.plot
  "Standard sequences to move the plot forward."
  (:require
   [nixy.state.job :as job]
   [nixy.job.all]))

(def all-jobs nixy.job.all/all-jobs)

(defn advance [state]
  (as-> state s
    (job/grant-new-cookies s)          ; grant cookies
    (job/activate-new-jobs s all-jobs) ; activate jobs
    (job/complete-active-jobs s)       ; complete jobs
    (job/current-post-hook s)))        ; current job hook
