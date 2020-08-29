(ns nixy.command.grep
  "Grep reads from `stdin` and outputs to `stdout` only lines which
  match a search string."
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :grep
  ;; Search for the search pattern `args` in `stdin`. Matching lines are
  ;; output to `stdout` and `state` is passed along unmodified
  ;; (side-effect free).
  [{:keys [state args stdin]}]
  (let [re (str/join (drop 1 args))
        matching-lines (filter #(not (nil? (re-find (re-pattern re) %)))
                               stdin)]
    {:stdout matching-lines
     :state state}))

(defmethod command/args :grep
  ;; Accept a search string between 1 and 10 characters.
  [{:keys [args]}]
  (if (or (= " " args)
          (not (nil? (re-matches #" [a-z]{0,10}" args)))
          (not (nil? (re-matches #" [a-z]{1,10}\n" args))))
    {:valid true} {}))
