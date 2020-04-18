(ns nixy.event
  (:require
   [nixy.command :as command]
   [nixy.guide :as guide]
   [clojure.string :as str]))

(defn- append-state-terminal-line [current-state key]
  (update-in
   current-state [:terminal :line]
   #(str/join (concat % key))))

(defn- run-command [current-state]
  (let [line (get-in current-state [:terminal :line])
        fs (get-in current-state [:terminal :fs])]
    (if-let [name (as-> current-state s
                    (get-in s [fs :filesystem "bin"])
                    (keys s)
                    (filter #(str/starts-with? line %) s) 
                    (first s))]
      ;; Run the command with current state and arguments
      (let [args (subs line (count name))
            file (get-in current-state [fs :filesystem "bin" name])]
        (as-> current-state s
          (command/exec file s args)         ; run command
          (assoc-in s [:terminal :line] ""))) ; reset line
      ;; Command not found
      (print (str "command not found: " line)))))

(defn press-key [current-state key]
  (let [current-guide (guide/state->guide current-state)]
    (cond
      ;; Run a complete command
      (and (= "Enter" key)
           (contains? current-guide "\n"))
      (run-command current-state)
      ;; Delete characters
      (= "Backspace" key)
      (update-in
       current-state [:terminal :line]
       #(if (empty? %) "" (str/join (drop-last %))))
      ;; Add characters
      (contains? current-guide key)
      (append-state-terminal-line current-state key)
      ;; Ignore
      :default current-state)))
