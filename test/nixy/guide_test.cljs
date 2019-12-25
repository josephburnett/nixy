(ns nixy.guide-test
  (:require
   [clojure.string :as str]
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.guide :as ng]))

(def test-state
  {:cwd ["etc"]
   :filesystem
   {"etc"
    {"badfile" {:mod #{:r}}}}})

(deftest file-exists-test
  (is ((ng/file-exists "etc" "badfile") test-state))
  (is (not ((ng/file-exists "etc" "goodfile") test-state)))
  (is (not ((ng/file-exists "etc") test-state))))

(deftest dir-exists-test
  (is (not ((ng/dir-exists "etc" "badfile") test-state)))
  (is (not ((ng/dir-exists "etc" "goodfile") test-state)))
  (is ((ng/dir-exists "etc") test-state)))

(deftest cwd-test
  (is (not ((ng/cwd) test-state)))
  (is ((ng/cwd "etc") test-state))
  (is (not ((ng/cwd "etc" "badfile")))))
             
(deftest pred-args->guide-test
  (let [pred #(or (str/starts-with? " etc" %)
                  (str/starts-with? " var" %))
        check (partial ng/pred-args->guide pred)]
    (is (= (check "") {" " {}}))
    (is (= (check " ") {"e" {} "v" {}}))
    (is (= (check " e") {"t" {}}))
    (is (= (check " et") {"c" {}}))
    (is (= (check " etc") {}))
    (is (= (check " v") {"a" {}}))
    (is (= (check " va") {"r" {}}))
    (is (= (check " var") {}))))

