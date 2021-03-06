(ns nixy.command.touch
  "Touch creates empty files."
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :touch
  ;; Create the files in `state` specified in `args`.
  [{:keys [state args]}]
  (let [fs (terminal/fs state)
        cwd (get-in state [fs :filesystem :cwd])
        filename (str/join (drop 1 args))]
    {:stdout []
     :state
     (assoc-in state (concat [fs :filesystem :root] cwd [filename])
               {:mod #{:r :w}
                :cat ""})}))

(defmethod command/args :touch
  ;; Accept as `args` any valid filename between 1 and 8 characters
  ;; which is not already taken a file.
  [{:keys [state args]}]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :filesystem :cwd])
        files (keys (get-in state (concat [fs :filesystem :root] cwd)))
        filename (str/join (drop 1 args))
        valid (or (= " " args)
                  (not (nil? (re-matches #" [a-z]{1,8}\n{0,1}" args))))
        unique (not-any? #(= (str % "\n") filename) files)]
    (if (and (= "usr" (first cwd))
             valid
             unique)
      {:valid true} {})))
