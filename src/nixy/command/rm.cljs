(ns nixy.command.rm
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :rm [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :filesystem :cwd])
        dir (get-in state (concat [fs :filesystem :root] cwd))
        files (keys dir)
        filename (str/join (drop 1 args))]
    (assoc-in
     (terminal/append state [(terminal/prompt state)])
     (concat [fs :filesystem :root] cwd)
     (dissoc dir filename))))

(defmethod command/args-pred :rm [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :filesystem :cwd])
        files (keys (get-in state (concat [fs :filesystem :root] cwd)))
        exists (not (not-any? #(str/starts-with? (str " " % "\n") args) files))]
    (and (= "usr" (first cwd))
         exists)))
