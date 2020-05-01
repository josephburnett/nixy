(ns nixy.command.ls-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [nixy.fixtures :refer [merge-state]]
   [nixy.state :as state]
   [nixy.command :as command]
   [nixy.command.ls :as ls]))

(use-fixtures :each
  (merge-state
   {:test-fs
    {:filesystem {:root {"file1" {:mod #{}}
                         "file2" {:mod #{}}
                         "emptydir" {}
                         "dir" {"file3" {:mod #{}}
                                "file4" {:mod #{}}}}}}
    :terminal
    {:fs :test-fs}}))

(deftest ls-exec-test
  (let [in-dir #(as-> % x
                  (assoc-in @state/app-state [:test-fs :filesystem :cwd] x)
                  (command/exec {:exec :ls} x "")
                  (get-in x [:terminal :output]))]
    (testing "ls"
      (testing "in root of filesystem"
        (is (= ["file1"
                "file2"
                "emptydir"
                "dir"
                "test-fs:> ls"]
               (in-dir []))))
      (testing "in empty directory"
        (is (= ["test-fs:emptydir> ls"]
               (in-dir ["emptydir"]))))
      (testing "in sub directory"
        (is (= ["file3"
                "file4"
                "test-fs:dir> ls"]
               (in-dir ["dir"])))))))
