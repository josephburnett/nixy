(ns nixy.state.terminal
  (:require
   [clojure.string :as str]))

(defn prompt [state]
  (let [fs (get-in state [:terminal :fs])
        cwd (str/join "/" (get-in state [fs :filesystem :cwd]))
        line (get-in state [:terminal :line])]
    (str/join [(name fs) ":" cwd "> " line])))

(defn output-lines [state lines]
  (update-in state [:terminal :output]
             #(concat % lines)))

(defn output-prompt [state]
  (output-lines state [(prompt state)]))

(defn append-key [state key]
  (update-in
   state [:terminal :line]
   #(str/join (concat % key))))

(defn drop-key [state]
  (update-in
   state [:terminal :line]
   #(if (empty? %) "" (str/join (drop-last %)))))

(defn line [state]
  (get-in state [:terminal :line]))

(defn fs [state]
  (get-in state [:terminal :fs]))
