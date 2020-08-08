(ns nixy.jobs-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [nixy.state :as state]
   [nixy.job :as job]))

(defmethod job/setup :test-job-one [{:keys [state]}]
  state)

(defmethod job/definition :test-job-one [_]
  {:setup-fn :test-job-one
   :guide-fn :test-job-one
   :complete-fn :test-job-one
   :title "Test Job One"
   :required-cookies #{:test-cookie-one}
   :intro ""
   :help ""})

(deftest activate-new-jobs-test
  (let [check (fn [cookies]
                (as-> state/initial-state s
                  (assoc-in s [:cookies] cookies)
                  (job/activate-new-jobs s [:test-job-one])
                  (get-in s [:jobs])))]
    (testing "new jobs activated"
      (is (= #{:test-job-one} (:active (check #{:test-cookie-one}))))
      (is (= #{:test-job-one} (:all (check #{:tests-cookies-one})))))
    (testing "no new jobs activated"
      (is (= #{} (:active (check #{}))))
      (is (= #{} (:all (check #{})))))))
