(ns nixy.command.grep
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :grep [{:keys [state args stdin]}]
  (let [re (str/join (drop 1 args))
        matching-lines (filter #(not (nil? (re-find (re-pattern re) %)))
                               stdin)]
    {:stdout matching-lines
     :state state}))

(defmethod command/args-pred :grep [{:keys [args]}]
  (or (= " " args)
      (not (nil? (re-matches #" [a-z]{0,10}" args)))
      (not (nil? (re-matches #" [a-z]{1,10}\n" args)))))
