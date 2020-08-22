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
  (let [check (fn [{:keys [required-cookies
                           have-cookies
                           all-jobs]
                    :or   {required-cookies #{}
                           have-cookies #{}
                           all-jobs #{}}}]
                (binding [test-job-required-cookies required-cookies
                          test-state-cookies        have-cookies
                          test-state-all-jobs       all-jobs]
                  (job/find-new (test-state) [:test-job])))]
    (testing "finds new jobs satisfying required cookies"
      (is (= #{:test-job} (check {:required-cookies #{:test-cookie}
                                  :have-cookies #{:test-cookie}}))      "one cookie")
      (is (= #{:test-job} (check {:required-cookies #{:tc-one :tc-two}
                                  :have-cookies #{:tc-one :tc-two}}))   "two cookies")
      (is (= #{:test-job} (check {:required-cookies #{:tc-one}
                                  :have-cookies #{:tc-one :tc-two}}))   "extra cookie"))
    (testing "does not find jobs already activated"
      (is (= #{} (check {:required-cookies #{:test-cookie}
                         :have-cookies #{:test-cookie}
                         :all-jobs #{:test-job}}))                      "already activated job"))
    (testing "does not find jobs not satisfying required cookies"
      (is (= #{} (check {:required-cookies #{:test-cookie}
                         :have-cookies #{:not-cookie}}))                "wrong cookie")
      (is (= #{} (check {:required-cookies #{:tc-one :tc-two}
                         :have-cookies #{:tc-one}}))                    "incomplete set of cookies")
      )))

;; (deftest activate-test
;;   (let [check (fn [{:keys [all-jobs
;;                            active-jobs]
;;                     :or   {all-jobs #{}
;;                            active-jobs #{}}}]
;;                 (let [s (deep-merge
;;                          test-app-state
;;                          {:jobs {:all all-jobs
;;                                  :active active-jobs}})]
;;                   (binding [test-setup #(assoc-in % [:test-key] true)]
                    
