(ns nixy.event
  (:require
   [nixy.command :as command]
   [nixy.cookies :as cookies]
   [nixy.job :as job]
   [nixy.job.all]
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

(def all-jobs nixy.job.all/all-jobs)

(defn- run-line [current-state]
  (let [echo (terminal/prompt current-state)
        commands (str/split (terminal/line current-state) "|")
        {:keys [state stdout]} (exec-all current-state commands)]
    (as-> state s
      (terminal/output-lines s [echo])   ; echo command
      (terminal/output-lines s stdout)   ; output to terminal
      (command/archive-line s)           ; add to history
      (cookies/grant-new-cookies s)      ; grant cookies
      (job/activate-new-jobs s all-jobs) ; activate jobs
      (job/complete-active-jobs s))))    ; complete jobs

(defn press-key [current-state key]
  (let [current-guide (guide/state->guide current-state)
        valid? #(get-in current-guide [% :valid])]
    (cond
      ;; Run a complete command
      (and (= "Enter" key)
           (valid? "\n"))
      (run-line current-state)
      ;; Pipe to a new command
      (and (= "|" key)
           (valid? "\n"))
      (terminal/append-key current-state "|")
      ;; Delete characters
      (= "Backspace" key)
      (terminal/drop-key current-state)
      ;; Add characters
      (valid? key)
      (terminal/append-key current-state key)
      ;; Ignore
      :default current-state)))
