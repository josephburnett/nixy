(ns nixy.command.ls-test
  (:require
   [cljs.test :refer-macros [deftest is]]
   [nixy.state :as state]
   [nixy.command :as command]
   [nixy.command.ls :as ls]))

(deftest ls-exec-test
  (let [ls-in-dir #(as-> % x
                     (assoc-in state/initial-state [:nixy :cwd] x)
                     (command/exec {:exec :ls} x "")
                     (get-in x [:terminal :output]))]
    (is (= ["bin"] (ls-in-dir [])))))
