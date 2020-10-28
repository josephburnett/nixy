(ns nixy.focus.laptop
  "Event loops for laptop focus."
  (:require
   [nixy.command :as command]
   [nixy.event :as event]
   [nixy.state.job :as job]
   [nixy.state.plot :as plot]
   [nixy.job.all]
   [nixy.guide :as guide]
   [nixy.state.terminal :as terminal]
   [clojure.string :as str]))

(defn- exec-one
  "Given `current-state` run the given `command`. The command will be
  given `stdin` as input. The command will return a new `state` and
  `stdout`."
  [current-state command stdin]
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

(defn- exec-all
  "Given `current-state` and a list of `commands` execute each one in
  a pipeline, providing the `stdout` of each command to the `stdin` of
  the next command. The initial `stdin` is an empty collection. The
  final `state` is returned with the `stdout` of the last command."
  [current-state commands]
  (reduce (fn [{:keys [state stdout]} command]
            (exec-one state command stdout))
          {:state current-state :stdout []}
          commands))

(defn- run-line
  "Parses a line of user input from `current-state` into individual
  commands. All commands are executed in a pipeline and the results
  are echoed to the terminal. The line is then archived to
  history. Cookies are granted by active jobs. New jobs are
  activated based on cookies. And active jobs are checked for
  completion."
  [current-state]
  (let [echo (terminal/prompt current-state)
        commands (str/split (terminal/line current-state) "|")
        {:keys [state stdout]} (exec-all current-state commands)]
    (as-> state s
      (terminal/output-lines s [echo])   ; echo command
      (terminal/output-lines s stdout)   ; output to terminal
      (command/archive-line s)           ; add to history
      (plot/advance s))))                ; advance the plot

(defmethod event/press-key :laptop
  ;; Dispatch `key` based on `current-guide` constructed from the
  ;; given `state`."
  [current-state key]
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
