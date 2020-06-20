(ns nixy.command.touch-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [nixy.fixtures :refer [merge-state]]
   [nixy.state :as state]
   [nixy.command :as command]
   [nixy.command.touch :as touch]))

(use-fixtures :each
  (merge-state
   {:nixy {:filesystem {:root {"dir" {}}}}
    :terminal {:fs :nixy}}))

(def empty-file {:mod #{:r :w}
                 :cat ""})

(deftest touch-exec-test
  (let [check (fn [path filename]
                (as-> @state/app-state s
                  (assoc-in s [:nixy :filesystem :cwd] path)
                  (command/exec {:exec :touch} s (cons " "filename))
                  (get-in s (concat [:nixy :filesystem :root] path [filename]))))]
                  
    (testing "touch"
      (is (= empty-file (check [] "file")))
      (is (= empty-file (check ["dir"] "file"))))))
