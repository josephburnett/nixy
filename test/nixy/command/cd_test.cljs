(ns nixy.command.cd-test
  (:require
   [cljs.test :refer-macros [deftest is use-fixtures]]
   [nixy.fixtures :refer [merge-state]]
   [nixy.state :as state]
   [nixy.command :as command]
   [nixy.command.cd :as cd]))

(use-fixtures :each
  (merge-state
   {:terminal {:fs :nixy}}))

(deftest cd-exec-test
  (let [cd-from-to (fn [from to]
                     (as-> from x
                       (assoc-in @state/app-state [:nixy :filesystem :cwd] x)
                       (command/exec {:exec-fn :cd :state x :args to})
                       (:state x)
                       (get-in x [:nixy :filesystem :cwd])))]
    (is (= ["bin"] (cd-from-to [] " bin")))
    (is (= [] (cd-from-to ["bin"] " ..")))))

(deftest cd-args-pred-test
  (let [in-dir (fn [dir args]
                 (as-> dir x
                   (assoc-in @state/app-state [:nixy :filesystem :cwd] x)
                   (:valid (command/args {:args-fn :cd :state x :args args}))))]
    (is (= true (in-dir [] " ")))
    (is (= true (in-dir [] " b")))
    (is (= true (in-dir [] " bin")))
    (is (= true (in-dir [] " bin\n")))
    (is (= nil (in-dir [] "..")))
    (is (= nil (in-dir [] " wrong")))
    (is (= true (in-dir ["bin"] " ")))
    (is (= true (in-dir ["bin"] " .")))
    (is (= true (in-dir ["bin"] " ..")))
    (is (= true (in-dir ["bin"] " ..\n")))
    (is (= nil (in-dir ["bin"] " wrong")))
    (is (= nil (in-dir ["bin"] " c")) "cannot cd into a file")))
