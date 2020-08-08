(ns nixy.guide-test
  (:require
   [clojure.string :as str]
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [nixy.fixtures :refer [merge-state]]
   [nixy.state :as state]
   [nixy.guide :as ng]
   [nixy.command :as command]))

(use-fixtures :each
  (merge-state
   {:terminal {:fs :nixy}}))

(deftest potential-fn->guide-test
  (let [pot-fn #(if (or (str/starts-with? " etc\n" %)
                        (str/starts-with? " var\n" %))
                  #{:t} #{})
        check (fn [line must-have]
                (let [guide (ng/potential-fn->guide pot-fn line)
                      expect (merge guide must-have)]
                  (= guide expect)))]
    (is (= true (check "" {" " #{:t}})))
    (is (= true (check " " {"e" #{:t} "v" #{:t}})))
    (is (= true (check " e" {"t" #{:t}})))
    (is (= true (check " et" {"c" #{:t}})))
    (is (= true (check " etc" {})))
    (is (= true (check " v" {"a" #{:t}})))
    (is (= true (check " va" {"r" #{:t}})))
    (is (= true (check " var" {})))))

(defmethod command/args :guide-test
  [{:keys [args]}]
  (if (str/starts-with? " a\n" args)
    {:valid true} {}))

(deftest state->guide-test
  (let [state (assoc-in @state/app-state
                        [:nixy :filesystem :root "bin"]
                        {"cd" {:args-fn :guide-test}})
        check (fn [line must-have]
                (let [guide (ng/state->guide (assoc-in state [:terminal :line] line))
                      expect (merge guide must-have)]
                  (= guide expect)))]
    (is (= true (check "" {"c" #{:command}})))
    (is (= true (check "c" {"d" #{:command}})))
    (is (= true (check "cd" {" " #{:args}})))
    (is (= true (check "cd " {"a" #{:args}})))
    (is (= true (check "cd a" {"\n" #{:args}})))))
