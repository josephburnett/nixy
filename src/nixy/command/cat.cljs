(ns nixy.command.cat
  (:require
   [nixy.command :as command]
   [nixy.state.terminal :as terminal]
   [clojure.string :as str]))

(defmethod command/exec :cat [{:keys [state args]}]
  (let [fs (terminal/fs state)
        cwd (get-in state [fs :filesystem :cwd])
        filename (str/join (drop 1 args))
        file (get-in state (concat [fs :filesystem :root] cwd [filename]))
        content (str/split (:cat file) "\n")
        stdout (if (nil? content) "" content)]
    {:stdout stdout
     :state state}))

(defmethod command/args-pred :cat [{:keys [state args]}]
  (let [fs (terminal/fs state)
        cwd (get-in state [fs :filesystem :cwd])
        current-dir (get-in state (concat [fs :filesystem :root] cwd))]
    (as-> current-dir x
      (keys x)                                       ; all nodes
      (filter #(get-in current-dir [% :mod]) x)      ; only files
      (map #(str " " % "\n") x)                      ; add space and newline
      (true? (some #(str/starts-with? % args) x))))) ; any matches?
        
  
