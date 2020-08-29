(ns nixy.state.terminal
  "Functions for accessing terminal values in `state`."
  (:require
   [clojure.string :as str]))

(defn prompt
  "Produce a command prompt for the current filesystem and working
  directory given `state`."
  [state]
  (let [fs (get-in state [:terminal :fs])
        cwd (str/join "/" (get-in state [fs :filesystem :cwd]))
        line (get-in state [:terminal :line])]
    (str/join [(name fs) ":" cwd "> " line])))

(defn output-lines
  "Output `lines` to the terminal."
  [state lines]
  (update-in state [:terminal :output]
             #(concat % lines)))

(defn output-prompt
  "Output a command prompt to the terminal."
  [state]
  (output-lines state [(prompt state)]))

(defn append-key
  "Append `key` to the current user input in `state`."
  [state key]
  (update-in
   state [:terminal :line]
   #(str/join (concat % key))))

(defn drop-key
  "Drop a key from the end of the current use input in `state`."
  [state]
  (update-in
   state [:terminal :line]
   #(if (empty? %) "" (str/join (drop-last %)))))

(defn line
  "Return the current line of user input."
  [state]
  (get-in state [:terminal :line]))

(defn fs
  "Return the current filesystem keyword."
  [state]
  (get-in state [:terminal :fs]))
