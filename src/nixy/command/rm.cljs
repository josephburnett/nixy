(ns nixy.command.rm
  "Rm permanently deletes files."
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :rm
  ;; Delete the file from `state` specified by name in `args`.
  [{:keys [state args]}]
  (let [fs (terminal/fs state)
        cwd (get-in state [fs :filesystem :cwd])
        dir (get-in state (concat [fs :filesystem :root] cwd))
        files (keys dir)
        filename (str/join (drop 1 args))]
    {:stdout []
     :state
     (assoc-in state (concat [fs :filesystem :root] cwd)
               (dissoc dir filename))}))

(defmethod command/args :rm
  ;; Accept any filename in the current working directory if the user
  ;; has write permission.
  [{:keys [state args]}]
  (let [fs (terminal/fs state)
        cwd (get-in state [fs :filesystem :cwd])
        files (keys (get-in state (concat [fs :filesystem :root] cwd)))
        exists (not (not-any? #(str/starts-with? (str " " % "\n") args) files))]
    (if (and (= "usr" (first cwd))
             exists)
      {:valid true} {})))
