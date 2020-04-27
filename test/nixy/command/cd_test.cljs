(ns nixy.command.cd-test
  (:require
   [cljs.test :refer-macros [deftest is]]
   [nixy.state :as state]
   [nixy.command :as command]
   [nixy.command.cd :as cd]))

(deftest cd-exec-test
  (let [cd-from-to (fn [from to]
                     (as-> from x
                       (assoc-in state/initial-state [:nixy :filesystem :cwd] x)
                       (command/exec {:exec :cd} x to)
                       (get-in x [:nixy :filesystem :cwd])))]
    (is (= ["bin"] (cd-from-to [] " bin")))
    (is (= [] (cd-from-to ["bin"] " ..")))))

(deftest cd-args-pred-test
  (let [in-dir (fn [dir args]
                 (as-> dir x
                   (assoc-in state/initial-state [:nixy :filesystem :cwd] x)
                   (command/args-pred {:args-pred :cd} x args)))]
    (is (= true (in-dir [] " ")))
    (is (= true (in-dir [] " b")))
    (is (= true (in-dir [] " bin")))
    (is (= true (in-dir [] " bin\n")))
    (is (= false (in-dir [] "..")))
    (is (= false (in-dir [] " wrong")))
    (is (= true (in-dir ["bin"] " ")))
    (is (= true (in-dir ["bin"] " .")))
    (is (= true (in-dir ["bin"] " ..")))
    (is (= true (in-dir ["bin"] " ..\n")))
    (is (= false (in-dir ["bin"] " wrong")))))
