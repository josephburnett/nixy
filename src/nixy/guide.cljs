(ns ^:figwheel-hooks nixy.guide
  (:require
   [nixy.job :as job]
   [nixy.command :as command]
   [nixy.state.terminal :as terminal]
   [clojure.string :as str]))

(def valid-keys
  ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l"
   "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x"
   "y" "z" " " "." "\n" "|"])

(defn potential-fn->guide
  "Build a guide by calling `potential-fn` for every possible
  extension of `s`."
  [potential-fn s]
  (reduce
   (fn [guide key]
     (assoc guide key (potential-fn (str/join (concat s key)))))
   {}
   valid-keys))

(defn- command-filename
  "Given `state` and a `partial-command` return the filename of the
  command, if any."
  [state partial-command]
  (as-> state s
    (terminal/fs s)
    (keys (get-in state [s :filesystem :root "bin"]))
    (filter #(str/starts-with? partial-command %) s)
    (first s)))

(defn- command-guide
  "Build a guide for the current line of user input in `state`. A
  guide is a map of keys to a map of their capabilities. E.g. if a
  key's capabilities include `:valid` equals `true` then it is a valid
  key for the user to press to continue their line of input. If the
  \\n key is marked as valid, the command is complete and ready to
  run."
  [state]
  (let [partial-command (last (str/split (terminal/line state) "|" -1))
        fs (terminal/fs state)]
    (if-let [filename (command-filename state partial-command)]
      ;; guide from command args
      (let [file (get-in state [fs :filesystem :root "bin" filename])
            args (subs partial-command (count filename))
            pot-fn #(let [args-guide (command/args (merge file {:state state
                                                                :args %}))]
                      (if (:valid args-guide)
                        (merge args-guide {:args true})
                        args-guide))]
        (potential-fn->guide pot-fn args))
      ;; guide from commands
      (let [commands (keys (get-in state [fs :filesystem :root "bin"]))
            pot-fn (fn [c] (if (->> commands
                                    (filter #(str/starts-with? % c))
                                    not-empty)
                            {:valid true :command true} {}))]
        (potential-fn->guide pot-fn partial-command)))))

(defn job-guide
  "Build a guide for the current job in `state` if there is one. A
  guide is a map of keys to a map of their capabilities. E.g. if a
  key's capabilities include `:job` equals `true` then it is a hint
  that the key leads toward completion of the job."
  [state]
  (if-let [current (get-in state [:jobs :current])]
    (let [job (get-in state [:jobs current])
          partial-line (terminal/line state)
          pot-fn #(job/guide (merge job {:state state
                                         :line %}))]
      (potential-fn->guide pot-fn partial-line))))

(defn state->guide
  "Build a guide for the given `state` by combining the guides for the
  current command and the current job. The guide will be used to
  constrain the user input as well as explain the constraints. And to
  provide hints toward accomplishing various goals."
  [state]
  (merge-with
   into
   (command-guide state)
   (job-guide state)))
