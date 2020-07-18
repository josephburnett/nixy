(ns nixy.command.ssh
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :ssh [_ state args _]
  (let [fs (keyword (str/join (drop 1 args)))]
    {:stdout []
     :state
     (assoc-in state [:terminal :fs] fs)}))

(defmethod command/args-pred :ssh-to-nixy [_ state args]
  (str/starts-with? " nixy\n" args))
