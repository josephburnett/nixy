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
