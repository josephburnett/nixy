(ns nixy.command.ssh
  "Ssh connects the terminal to another filesystem on a remote
  system."
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :ssh
  ;; Connect to the system specified in `args`.
  [{:keys [state args]}]
  (let [fs (keyword (str/join (drop 1 args)))]
    {:stdout []
     :state
     (assoc-in state [:terminal :fs] fs)}))

(defmethod command/args :ssh-to-nixy
  ;; Accept only the nixy system as `args`.
  [{:keys [state args]}]
  (if (str/starts-with? " nixy\n" args)
    {:valid true} {}))
