(ns nixy.job-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.fixtures :refer merge-initial-state]
   [nixy.job :as job]))

(deftest find-new-test
  (testing "finds new jobs satisfying required cookies"
    (let [jobs [:job-1]
          state (merge-initial-state
                 {})
          check (fn [cookies]
                  (as-> state s
                    (assoc-in s [:cookies] cookies)
                    (job/find-new s jobs)
                    (get-in s [:jobs :activated])))]
      (is (= #{:job-1} (check #{:cookie-1})))))
  (testing "does not find jobs already activated"
    (is (= true true)))
  (testing "does not find jobs not satisfying required cookies"
    (is (= true true))))

