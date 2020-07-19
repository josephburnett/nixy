(ns nixy.command.ssh
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :ssh [{:keys [state args]}]
  (let [fs (keyword (str/join (drop 1 args)))]
    {:stdout []
     :state
     (assoc-in state [:terminal :fs] fs)}))

(defmethod command/args-pred :ssh-to-nixy [{:keys [state args]}]
  (str/starts-with? " nixy\n" args))
