(ns nixy.command.cd
  (:require
   [nixy.command :as command]
   [clojure.string :as str]))

(defmethod command/exec :cd [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :cwd])
        dir (drop 1 args)]
    (if (= ".." dir)
      (assoc-in state [fs :cwd] (drop-last cwd))
      (assoc-in state [fs :cwd] (concat cwd dir)))))

(defmethod command/args-pred :cd [_ state args]
  (let [fs (get-in state [:terminal :fs])
        cwd (get-in state [fs :cwd])
        nodes (keys (get-in state (concat [fs :filesystem] cwd)))
        dirs (filter #(not (:mod %)) nodes)
        valid-args (map #(str " " % "\n") dirs)]
    (some #(str/starts-with? args %) valid-args)))
