(ns nixy.terminal
  (:require
   [nixy.guide :as guide]
   [clojure.string :as str]))

(defn- append-state-terminal-line [current-state key]
  (update-in
   current-state [:terminal :line]
   #(str/join (concat % key))))

(defn press-key [current-state key]
  (let [current-guide (guide/state->guide current-state)]
    (cond
      ;; Run a complete command
      (and (= "Enter" key)
           (contains? current-guide ""))
      current-state ; TODO: run the command
      ;; Delete characters
      (= "Backspace" key)
      (update-in
       current-state [:terminal :line]
       #(if (= "" %) "" (str/join (drop-last %))))
      ;; Add characters
      (contains? current-guide key)
      (append-state-terminal-line current-state key)
      ;; Ignore
      :default current-state)))
