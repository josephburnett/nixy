(ns nixy.job-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.state :as state]
   [nixy.fixtures :refer [deep-merge null-cookies null-guide null-fn]]
   [nixy.job :as job]))

(def ^:dynamic test-job-setup null-fn)
(def ^:dynamic test-job-guide null-guide)
(def ^:dynamic test-job-complete? (fn [_] false))
(def ^:dynamic test-job-required-cookies null-cookies)

(defmethod job/definition :test-job [_]
  {:title "Test Job"
   :setup-fn :test-job
   :guide-fn :test-job
   :complete-fn :test-job
   :required-cookies test-job-required-cookies})

(defmethod job/setup :test-job [{:keys [state]}]
  (test-job-setup state))

(defmethod job/guide :test-job [{:keys [state line]}]
  (test-job-guide state line))

(defmethod job/complete? :test-job [{:keys [state]}]
  (test-job-complete? state))

(def ^:dynamic test-state-cookies #{})
(def ^:dynamic test-state-all-jobs #{})
(def ^:dynamic test-state-active-jobs #{})
(def ^:dynamic test-state-complete-jobs #{})

(defn test-state []
  (deep-merge
   state/initial-state
   {:cookies test-state-cookies
    :jobs {:all test-state-all-jobs
           :active test-state-active-jobs
           :complete test-state-complete-jobs}}))

(deftest find-new-test
  (let [check (fn [{:keys [require have all]
                    :or   {require #{} have #{} all #{}}}]
                (binding [test-job-required-cookies require
                          test-state-cookies        have
                          test-state-all-jobs       all]
                  (job/find-new (test-state) [:test-job])))]
    (testing "finds new job satisfying required cookies"
      (is (= #{:test-job} (check {:require #{:test-cookie}
                                  :have    #{:test-cookie}}))      "one cookie")
      (is (= #{:test-job} (check {:require #{:tc-one :tc-two}
                                  :have    #{:tc-one :tc-two}}))   "two cookies")
      (is (= #{:test-job} (check {:require #{:tc-one}
                                  :have    #{:tc-one :tc-two}}))   "extra cookie"))
    (testing "does not find job already activated"
      (is (= #{} (check {:require #{:test-cookie}
                         :have    #{:test-cookie}
                         :all     #{:test-job}}))                  "already activated job"))
    (testing "does not find job not satisfying required cookies"
      (is (= #{} (check {:require #{:test-cookie}
                         :have    #{:not-cookie}}))                "wrong cookie")
      (is (= #{} (check {:require #{:tc-one :tc-two}
                         :have    #{:tc-one}}))                    "incomplete set of cookies"))
    ))

(deftest activate-test
  (let [check (fn [jobs]
                (binding [test-job-setup #(update-in % [:setup-count] inc)]
                  (as-> (assoc (test-state) :setup-count 0) s
                    (job/activate s jobs)
                    (:setup-count s))))]
    (testing "runs setup fn"
      (is (= 0 (check []))           "no jobs to setup")
      (is (= 1 (check [:test-job]))  "one job to setup")
      (is (= 2 (check [:test-job
                       :test-job]))  "two jobs to setup")))
  (let [check (fn [jobs]
                (as-> (test-state) s
                  (job/activate s jobs)
                  (get-in s [:jobs :all])))]
    (testing "adds to all jobs"
      (is (= #{} (check []))                    "no jobs to activate")
      (is (= #{:test-job} (check [:test-job]))  "one job to activate"))))
                    
