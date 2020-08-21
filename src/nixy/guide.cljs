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

;; Build a guide by calling potential-fn for every
;; possible extension of s.
(defn potential-fn->guide [potential-fn s]
  (reduce
   (fn [guide key]
     (assoc guide key (potential-fn (str/join (concat s key)))))
   {}
   valid-keys))

;; Filename of current command or nil.
(defn- command-filename [state partial-command]
  (as-> state s
    (terminal/fs s)
    (keys (get-in state [s :filesystem :root "bin"]))
    (filter #(str/starts-with? partial-command %) s)
    (first s)))

;; Build a guide for the current command.
(defn- command-guide [state]
  (let [partial-command (last (str/split (terminal/line state) "|" -1))
        fs (terminal/fs state)]
    (if-let [filename (command-filename state partial-command)]
      ; guide from command args
      (let [file (get-in state [fs :filesystem :root "bin" filename])
            args (subs partial-command (count filename))
            pot-fn #(let [args-guide (command/args (merge file {:state state
                                                                :args %}))]
                      (if (:valid args-guide)
                        (merge args-guide {:args true})
                        args-guide))]
        (potential-fn->guide pot-fn args))
      ; guide from commands
      (let [commands (keys (get-in state [fs :filesystem :root "bin"]))
            pot-fn (fn [c] (if (->> commands
                                    (filter #(str/starts-with? % c))
                                    not-empty)
                            {:valid true :command true} {}))]
        (potential-fn->guide pot-fn partial-command)))))

;; Build a guide for the current job.
(defn job-guide [state]
  (if-let [current (get-in state [:jobs :current])]
    (let [job (get-in state [:jobs current])
          partial-line (terminal/line state)
          pot-fn #(job/guide (merge job {:state state
                                         :line %}))]
      (potential-fn->guide pot-fn partial-line))))

(defn state->guide [state]
  (merge-with
   into
   (command-guide state)
   (job-guide state)))
