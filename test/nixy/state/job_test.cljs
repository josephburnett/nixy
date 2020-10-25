(ns nixy.state.job-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.state :as state]
   [nixy.fixtures :refer [deep-merge null-cookies null-guide impossible-cookies null-setup]]
   [nixy.job :as job]
   [nixy.state.job :as state-job]))

(def ^:dynamic test-job-setup null-setup)
(def ^:dynamic test-job-guide null-guide)
(def ^:dynamic test-job-cookies impossible-cookies)
(def ^:dynamic test-job-complete? (fn [_] false))
(def ^:dynamic test-job-required-cookies null-cookies)

(defmethod job/definition :test-job [_]
  {:title "Test Job"
   :setup-fn :test-job
   :guide-fn :test-job
   :cookies-fn :test-job
   :complete-fn :test-job
   :required-cookies test-job-required-cookies})

(defmethod job/setup :test-job [{:keys [state]}]
  (test-job-setup state))

(defmethod job/guide :test-job [{:keys [state line]}]
  (test-job-guide state line))

(defmethod job/cookies :test-job [{:keys [state]}]
  (test-job-cookies state))

(defmethod job/complete? :test-job [{:keys [state]}]
  (test-job-complete? state))

(def ^:dynamic test-state-cookies #{})
(def ^:dynamic test-state-all-jobs #{})
(def ^:dynamic test-state-active-jobs #{})
(def ^:dynamic test-state-complete-jobs #{})
(def ^:dynamic test-state-current-job nil)

(defn test-state []
  (deep-merge
   state/initial-state
   {:cookies test-state-cookies
    :jobs {:all test-state-all-jobs
           :active test-state-active-jobs
           :complete test-state-complete-jobs
           :current test-state-current-job}}))

(deftest find-jobs-test
  (let [check (fn [{:keys [require have all]
                    :or   {require #{} have #{} all #{}}}]
                (binding [test-job-required-cookies require
                          test-state-cookies        have
                          test-state-all-jobs       all]
                  (state-job/find-jobs (test-state) [:test-job])))]
    (testing "finds new job satisfying required cookies"
      (is (= #{:test-job} (check {:require #{:test-cookie}
                                  :have    #{:test-cookie}}))      "one cookie")
      (is (= #{:test-job} (check {:require #{:tc-one :tc-two}
                                  :have    #{:tc-one :tc-two}}))   "two cookies")
      (is (= #{:test-job} (check {:require #{:tc-one}
                                  :have    #{:tc-one :tc-two}}))   "extra cookie"))
    (testing "does not find job already activated"
      (is (= #{}          (check {:require #{:test-cookie}
                                  :have    #{:test-cookie}
                                  :all     #{:test-job}}))         "already activated job"))
    (testing "does not find job not satisfying required cookies"
      (is (= #{}          (check {:require #{:test-cookie}
                                  :have    #{:not-cookie}}))       "wrong cookie")
      (is (= #{}          (check {:require #{:tc-one :tc-two}
                                  :have    #{:tc-one}}))           "incomplete set of cookies"))))

(deftest activate-test
  (let [check (fn [jobs]
                (binding [test-job-setup #(update-in % [:setup-count] inc)]
                  (as-> (assoc (test-state) :setup-count 0) s
                    (state-job/activate s jobs)
                    (:setup-count s))))]
    (testing "runs setup fn"
      (is (= 0              (check []))                   "no jobs to setup")
      (is (= 1              (check [:test-job]))          "one job to setup")
      (is (= 2              (check [:test-job
                       :test-job]))                       "two jobs to setup")))
  (let [check (fn [key jobs]
                (as-> (test-state) s
                  (state-job/activate s jobs)
                  (get-in s [:jobs key])))]
    (testing "adds to :jobs"
      (testing "adds to :all"
        (is (= #{}          (check :all []))              "no jobs to activate")
        (is (= #{:test-job} (check :all [:test-job]))     "one job to activate"))
      (testing "adds to :active"
        (is (= #{}          (check :active []))           "no jobs to activate")
        (is (= #{:test-job} (check :active [:test-job]))  "one job to activate")))))

(deftest find-complete-test
  (let [check (fn [{:keys [active current done]}]
                (binding [test-job-complete? #(:done %)
                          test-state-active-jobs active
                          test-state-current-job current]
                  (state-job/find-complete (assoc (test-state) :done done))))]
  (testing "finds nothing"
    (is (= #{}          (check {:active #{}
                                :current nil}))        "no active jobs")
    (is (= #{}          (check {:active #{:test-job}
                                :current :test-job}))  "not satisfactory"))
  (testing "finds satisfactory job"
    (is (= #{:test-job} (check {:active #{:test-job}
                                :current :test-job
                                :done true}))          "is current job")
    (is (= #{:test-job} (check {:active #{:test-job}
                                :current :other-job
                                :done true}))          "is not current job")
    (is (= #{:test-job} (check {:active #{:test-job}
                                :current nil
                                :done true}))          "there is no current job"))))

(deftest complete-test
  (let [check (fn [{:keys [key active current]}]
                (binding [test-state-active-jobs active
                          test-state-current-job current]
                  (as-> (test-state) s
                    (state-job/complete s [:test-job])
                    (get-in s [:jobs key]))))]
    (testing "adds to :jobs :complete"
      (is (= #{:test-job}  (check {:key :complete
                                   :active #{:test-job}
                                   :current :test-job}))    "current active job")
      (is (= #{:test-job}  (check {:key :complete
                                   :active #{:other-job}
                                   :current :other-job}))   "not an active job")
      (is (= #{:test-job}  (check {:key :complete
                                   :active #{}
                                   :current nil}))          "no active or current job"))
    (testing "removes from :jobs :active"
      (is (= #{}           (check {:key :active
                                   :active #{:test-job}
                                   :current :test-job}))    "current active job")
      (is (= #{:other-job} (check {:key :active
                                   :active #{:other-job}
                                   :current :other-job}))   "not an active job")
      (is (= #{}           (check {:key :active
                                   :active #{}
                                   :current nil}))          "no active or current job"))
    (testing "sets new current job"
      (is (= :other-job    (check {:key :current
                                   :active #{:test-job
                                             :other-job}
                                   :current :test-job}))    "current active job"))))

(deftest find-cookies-test
  (let [check (fn [{:keys [active have offer]}]
                (binding [test-state-active-jobs active
                          test-state-cookies have
                          test-job-cookies (fn [_] offer)]
                  (state-job/find-cookies (test-state))))]
    (testing "finds new cookies"
      (is (= #{:test-cookie} (check {:active #{:test-job}
                                     :have #{}
                                     :offer #{:test-cookie}}))  "one new cookie")
      (is (= #{:tc-one
               :tc-two}      (check {:active #{:test-job}
                                     :have #{}
                                     :offer #{:tc-one
                                              :tc-two}}))       "two new cookies")
      (is (= #{:tc-two}      (check {:active #{:test-job}
                                     :have #{:tc-one}
                                     :offer #{:tc-two}}))       "one of two cookies"))
    (testing "finds no new cookies"
      (is (= #{}             (check {:active #{:test-job}
                                     :have #{:test-cookie}
                                     :offer #{:test-cookie}}))  "one duplicate cookie")
      (is (= #{}             (check {:active #{:test-job}
                                     :have #{}
                                     :offer #{}}))              "no cookies")
      (is (= #{}             (check {:active #{:test-job}
                                     :have #{:tc-one
                                             :tc-two}
                                     :offer #{:tc-one
                                              :tc-two}}))       "none of two cookies")
      (is (= #{}             (check {:active #{}
                                     :have #{}
                                     :offer #{:test-cookie}}))  "not active"))))
 
