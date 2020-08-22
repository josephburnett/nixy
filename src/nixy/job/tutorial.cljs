(ns nixy.job.tutorial
  (:require
   [nixy.job :as job]))

(defmethod job/definition :tutorial [_]
  {:title "Tutorial"
   :setup-fn :nothing
   :guide-fn :tutorial
   :complete-fn :tutorial
   :required-cookies #{}})

(defmethod job/guide :tutorial [{:keys [state line]}]
  ;; do something here
  )

(defmethod job/complete? :tutorial [{:keys [state]}]
  ;; check something here
  )
