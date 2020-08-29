(ns nixy.command
  "Commands are functions which mutate application state. They are
  embedded in a filesystem as structures with dispatch keys for the
  `exec` and `args` functions. The `args` multimethod determines what
  parameters are valid for the command in order to produce a
  guide. The `exec` multimethod is reponsible for running the command
  with the given arguments, returning a new application
  state.

  Commands can take input from stdin and output to
  stdout. Commands can be pipelined together such that the output of
  one command becomes the input of the next. The first command begins
  with an empty input and the last command's output is written to the
  terminal.")

(defmulti exec
  "Given valid `args` and `stdin` from the previous command (if any),
  executes the command by modifying `state`."
  (fn [{:keys [exec-fn]}] exec-fn))

(defmulti args
  "Given `state` and a `line` of partial user input, determines the
  validity of `line`, returning a map. If the line is valid, the map
  will include the key `:valid` with a value `true`. Optionally may
  add other map keys to indicate the capabilities of the line and
  reasons thereof."
  (fn [{:keys [args-fn]}] args-fn))

(defn archive-line
  "Move the current line in `state` to history."
  [state]
  (let [line (get-in state [:terminal :line])
        fs (get-in state [:terminal :fs])
        history (cons line (get-in state [fs :history]))]
    (as-> state s
      (assoc-in s [:terminal :line] "")
      (assoc-in s [fs :history] history))))
