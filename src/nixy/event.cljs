(ns nixy.event
  (:require
   [nixy.command :as command]
   [nixy.cookies :as cookies]
   [nixy.guide :as guide]
   [nixy.state.terminal :as terminal]
   [clojure.string :as str]))

(defn- exec-one [current-state command stdin]
  (let [fs (terminal/fs current-state)
        name (as-> current-state s
               (get-in s [fs :filesystem :root "bin"])
               (keys s)
               (filter #(str/starts-with? command %) s) 
               (first s))
        args (subs command (count name))
        file (get-in current-state [fs :filesystem :root "bin" name])]
    (command/exec (merge file {:state current-state
                               :args args
                               :stdin stdin}))))

(defn- exec-all [current-state commands]
  (reduce (fn [{:keys [state stdout]} command]
            (exec-one state command stdout))
          {:state current-state :stdout []}
          commands))

(defn- run-line [current-state]
  (let [echo (terminal/prompt current-state)
        commands (str/split (terminal/line current-state) "|")
        {:keys [state stdout]} (exec-all current-state commands)]
    (as-> state s
      (terminal/output-lines s [echo])  ; echo command
      (terminal/output-lines s stdout)  ; output to terminal
      (command/archive-line s)          ; add to history
      (cookies/grant-new-cookies s))))  ; grant cookies

(defn press-key [current-state key]
  (let [current-guide (guide/state->guide current-state)]
    (cond
      ;; Run a complete command
      (and (= "Enter" key)
           (contains? current-guide "\n"))
      (run-line current-state)
      ;; Pipe to a new command
      (and (= "|" key)
           (contains? current-guide "\n"))
      (terminal/append-key current-state "|")
      ;; Delete characters
      (= "Backspace" key)
      (terminal/drop-key current-state)
      ;; Add characters
      (contains? current-guide key)
      (terminal/append-key current-state key)
      ;; Ignore
      :default current-state)))
