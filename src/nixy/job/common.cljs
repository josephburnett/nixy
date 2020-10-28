(ns nixy.job.common
  (:require
   [nixy.job :as job]))

(defmethod job/setup :nothing [{:keys [state]}] state)

(defmethod job/post-hook :nothing [{:keys [state]}] state)
