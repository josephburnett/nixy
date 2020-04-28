(ns nixy.state.terminal
  (:require
   [clojure.string :as str]))

(defn prompt [state]
  (let [fs (get-in state [:terminal :fs])
        cwd (str/join "/" (get-in state [fs :filesystem :cwd]))]
    (str/join [(name fs) ":" cwd "> "])))

(defn append [state lines]
  (update-in state [:terminal :output]
             #(concat % lines)))
