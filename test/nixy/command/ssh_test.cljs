(ns nixy.command.ssh-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [nixy.fixtures :refer [merge-state]]
   [nixy.state :as state]
   [nixy.command :as command]
   [nixy.command.ssh :as ssh]))

(use-fixtures :each
  (merge-state
   {:one {}
    :two {}
    :terminal {:fs :one}}))

(deftest ssh-exec-test
  (let [check (fn [source dest]
                (as-> @state/app-state s
                  (assoc-in s [:terminal :fs] source)
                  (command/exec {:exec :ssh} s (str " " dest "\n"))
                  (get-in s [:terminal :fs])))]
    (testing "ssh"
      (is (= :two (check "one" "two")) "from one to two")
      (is (= :one (check "two" "one")) "from two to one"))))
