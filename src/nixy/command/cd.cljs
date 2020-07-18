(ns nixy.command.cd
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :cd [_ state args]
  (let [fs (terminal/fs state)
        cwd (get-in state [fs :filesystem :cwd])
        dir (str/join (drop 1 args))]
    {:stdout []
     :state
     (if (= ".." dir)
       (assoc-in state [fs :filesystem :cwd] (drop-last cwd))
       (assoc-in state [fs :filesystem :cwd] (concat cwd [dir])))}))

(defmethod command/args-pred :cd [_ state args]
  (let [fs (terminal/fs state)
        cwd (get-in state [fs :filesystem :cwd])
        current-dir (get-in state (concat [fs :filesystem :root] cwd))]
    (as-> current-dir x
        (keys x)                                        ; all nodes
        (filter #(not (get-in current-dir [% :mod])) x) ; only directories
        (if (= [] cwd) x (cons ".." x))                 ; (maybe) allow ..
        (map #(str " " % "\n") x)                       ; add space and newline
        (true? (some #(str/starts-with? % args) x)))))  ; any matches?
