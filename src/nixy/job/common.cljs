(ns nixy.job.common
  (:require
   [nixy.job :as job]))

(defmethod job/setup :nothing [{:keys [state]}] state)
