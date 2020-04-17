(ns ^:figwheel-hooks nixy.guide
  (:require
   [nixy.command.dispatch :as dispatch]
   [clojure.string :as str]))

(def valid-keys
  ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l"
   "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x"
   "y" "z" " " "\n"])

(defn pred-args->guide [tag pred args]
  (reduce
   #(if (pred (str/join (concat args %2)))
      (assoc %1 %2 #{tag})
      %1)
   {}
   valid-keys))

(defn state->guide [state]
  (let [line (get-in state [:terminal :line])
        fs (get-in state [:terminal :fs])
        command-names (keys (get-in state [fs :filesystem "bin"]))]
    (if-let [filename (->> command-names
                           (filter #(str/starts-with? line %))
                           first)]
      ;; guide from file args predicate
      (let [fs (get-in state [:terminal :fs])
            file (get-in state [fs :filesystem "bin" filename])
            args (subs line (count filename))
            pred #(dispatch/args-pred file %)]
        (pred-args->guide :args pred args))
      ;; guide from list of executable files
      (let [pred (fn [l] (->> command-names
                              (filter #(str/starts-with? % l))
                              not-empty))]
        (pred-args->guide :command pred line)))))
