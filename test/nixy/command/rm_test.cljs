(ns nixy.command.rm-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [nixy.fixtures :refer [merge-state]]
   [nixy.state :as state]
   [nixy.command :as command]
   [nixy.command.rm :as rm]))

(use-fixtures :each
  (merge-state
   {:test-fs
    {:filesystem
     {:root
      {"usr"
       {"file" {}}}}}
    :terminal
    {:fs :test-fs}}))

(deftest rm-exec-test
  (let [check (fn [in-dir rm-args]
                (as-> @state/app-state s
                  (assoc-in s [:test-fs :filesystem :cwd] in-dir)
                  (command/exec {:exec :rm} s rm-args)
                  (get-in s (concat [:test-fs :filesystem :root] in-dir))))]
    (testing "rm a file"
      (is (= {} (check ["usr"] " file"))))))
      
