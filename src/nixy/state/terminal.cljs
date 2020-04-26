(ns nixy.state.terminal
  (:require
   [clojure.string :as str]))

(defn prompt [state]
  (let [fs (get-in state [:terminal :fs])
        cwd (str/join "/" (get-in state [fs :cwd]))]
    (str/join [(name fs) ":" cwd "> "])))
