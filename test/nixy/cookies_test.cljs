(ns nixy.cookies-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.cookies :as cookies]))

(deftest grant-test
  (let [check (fn [state new-cookies]
                (as-> state s
                  (cookies/grant s new-cookies)
                  (:cookies s)))]
    (testing "grant"
      (is (= #{:a} (check {:cookies #{}} #{:a})) "first cookie")
      (is (= #{:a :b} (check {:cookies #{:a}} #{:b})) "second cookie")
      (is (= #{:a :b} (check {:cookies #{:a :b}} #{:b})) "duplicate cookie")
      (is (= #{:a :b :c} (check {:cookies #{:a}} #{:b :c})) "multiple cookies"))))

(deftest find-new-test
  (let [check cookies/find-new]
    (is (= #{} (check #{} [])) "no preds")
    (is (= #{} (check #{} [{:key :a :pred (fn [_] false)}])) "false")
    (is (= #{:a} (check #{} [{:key :a :pred (fn [_] true)}])) "true, new")
    (is (= #{:a} (check #{:a} [{:key :a :pred (fn [_] true)}])) "true, not new")
    (is (= #{:a :b} (check #{:c} [{:key :a :pred (fn [_] true)}
                                  {:key :b :pred (fn [_] true)}])) "multiple new")))
