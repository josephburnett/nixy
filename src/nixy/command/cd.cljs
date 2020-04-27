(ns nixy.command.cd
  (:require
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :cd [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :filesystem :cwd])
        dir (str/join (drop 1 args))]
    (if (= ".." dir)
      (assoc-in state [fs :filesystem :cwd] (drop-last cwd))
      (assoc-in state [fs :filesystem :cwd] (concat cwd [dir])))))

(defmethod command/args-pred :cd [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :filesystem :cwd])]
    (as-> fs x
        (keys (get-in state (concat [x :filesystem :root] cwd))) ; nodes
        (filter #(not (:mod %)) x)                               ; directories
        (if (= [] cwd) x (cons ".." x))                          ; (maybe) allow ..
        (map #(str " " % "\n") x)                                ; add space and newline
        (true? (some #(str/starts-with? % args) x)))))           ; any matches?
