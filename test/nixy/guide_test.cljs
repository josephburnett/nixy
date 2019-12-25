(ns nixy.guide-test
  (:require
   [clojure.string :as str]
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.guide :as ng]))

(deftest pred-args->guide-test
  (let [pred #(or (str/starts-with? " etc" %)
                  (str/starts-with? " var" %))
        given (partial ng/pred-args->guide :t pred)]
    (is (= (given "") {" " #{:t}}))
    (is (= (given " ") {"e" #{:t} "v" #{:t}}))
    (is (= (given " e") {"t" #{:t}}))
    (is (= (given " et") {"c" #{:t}}))
    (is (= (given " etc") {}))
    (is (= (given " v") {"a" #{:t}}))
    (is (= (given " va") {"r" #{:t}}))
    (is (= (given " var") {}))))

(deftest state->guide-test
  (let [state {:filesystem {"bin" {"cd" {:args #(str/starts-with? " a" %)}}}}
        given #(ng/state->guide (assoc-in state [:terminal :line] %))]
    (is (= (given "") {"c" #{:command}}))
    (is (= (given "c") {"d" #{:command}}))
    (is (= (given "cd") {" " #{:args}}))
    (is (= (given "cd ") {"a" #{:args}}))
    (is (= (given "cd a") {}))))
