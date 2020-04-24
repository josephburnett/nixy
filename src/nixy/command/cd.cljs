(ns nixy.command.cd
  (:require
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :cd [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :cwd])
        dir (str/join (drop 1 args))]
    (if (= ".." dir)
      (assoc-in state [fs :cwd] (drop-last cwd))
      (assoc-in state [fs :cwd] (concat cwd [dir])))))

(defmethod command/args-pred :cd [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :cwd])]
    (as-> fs x
        (keys (get-in state (concat [x :filesystem] cwd))) ; nodes
        (filter #(not (:mod %)) x)                         ; directories
        (if (= [] cwd) x (cons ".." x))                    ; allow ..
        (map #(str " " % "\n") x)                          ; add space and newline
        (true? (some #(str/starts-with? % args) x)))))     ; any matches?
