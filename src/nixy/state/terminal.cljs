(ns nixy.state.terminal
  (:require
   [clojure.string :as str]))

(defn prompt [state]
  (let [fs (get-in state [:terminal :fs])
        cwd (str/join "/" (get-in state [fs :filesystem :cwd]))
        line (get-in state [:terminal :line])]
    (str/join [(name fs) ":" cwd "> " line])))

(defn append [state lines]
  (update-in state [:terminal :output]
             #(concat % lines)))
