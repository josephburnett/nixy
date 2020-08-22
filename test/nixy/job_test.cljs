(ns nixy.job-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.state :as state]
   [nixy.fixtures :refer [deep-merge null-cookies null-guide null-fn]]
   [nixy.job :as job]))

(def test-app-state state/initial-state)
(def ^:dynamic test-setup null-fn)
(def ^:dynamic test-guide null-guide)
(def ^:dynamic test-complete? (fn [_] false))
(def ^:dynamic test-required-cookies null-cookies)
(def ^:dynamic test-activated-cookies #{})

(defmethod job/definition :test-job [_]
  {:title "Test Job"
   :setup-fn :test-job
   :guide-fn :test-job
   :complete-fn :test-job
   :required-cookies test-required-cookies})

(defmethod job/setup :test-job [{:keys [state]}]
  (test-setup state))

(defmethod job/guide :test-job [{:keys [state line]}]
  (test-guide state line))

(defmethod job/complete? :test-job [{:keys [state]}]
  (test-complete? state))

(deftest find-new-test
  (let [check (fn [{:keys [required-cookies
                           have-cookies
                           all-jobs]
                    :or   {required-cookies #{}
                           have-cookies #{}
                           all-jobs #{}}}]
                (let [s (deep-merge
                         test-app-state
                         {:cookies have-cookies
                          :jobs {:all all-jobs}})]
                  (binding [test-required-cookies required-cookies]
                    (job/find-new s [:test-job]))))]
    (testing "finds new jobs satisfying required cookies"
      (is (= #{:test-job} (check {:required-cookies #{:test-cookie}
                                  :have-cookies #{:test-cookie}}))    "one cookie")
      (is (= #{:test-job} (check {:required-cookies #{:tc-one :tc-two}
                                  :have-cookies #{:tc-one :tc-two}}))  "two cookies")
      (is (= #{:test-job} (check {:required-cookies #{:tc-one}
                                  :have-cookies #{:tc-one :tc-two}}))  "extra cookie"))
    (testing "does not find jobs already activated"
      (is (= #{} (check {:required-cookies #{:test-cookie}
                         :have-cookies #{:test-cookie}
                         :all-jobs #{:test-job}}))                     "already activated job"))
    (testing "does not find jobs not satisfying required cookies"
      (is (= #{} (check {:required-cookies #{:test-cookie}
                         :have-cookies #{:not-cookie}}))               "wrong cookie")
      (is (= #{} (check {:required-cookies #{:tc-one :tc-two}
                         :have-cookies #{:tc-one}}))                   "incomplete set of cookies")
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
                    
