(ns nixy.command.touch
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :touch [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :filesystem :cwd])
        filename (str/join (drop 1 args))]
    (assoc-in state
              (concat [fs :filesystem :root] cwd [filename])
              {:mod #{:r :w}
               :cat ""})))

(defmethod command/args-pred :touch [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :filesystem :cwd])
        files (keys (get-in state (concat [fs :filesystem :root] cwd)))
        filename (str/join (drop 1 args))
        valid (or (= " " args)
                  (re-matches #" [a-z]{1,8}\n{0,1}" args))
        unique (not-any? #(= (str % "\n") filename) files)]
    (and (= "usr" (first cwd))
         valid
         unique)))
