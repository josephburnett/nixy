(ns nixy.level-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.level :as nl]))

(def test-state
  {:cwd ["etc"]
   :filesystem
   {"etc"
    {"badfile" {:mod #{:r}}}}})

(deftest file-exists-test
  (is ((nl/file-exists "etc" "badfile") test-state))
  (is (not ((nl/file-exists "etc" "goodfile") test-state)))
  (is (not ((nl/file-exists "etc") test-state))))

(deftest dir-exists-test
  (is (not ((nl/dir-exists "etc" "badfile") test-state)))
  (is (not ((nl/dir-exists "etc" "goodfile") test-state)))
  (is ((nl/dir-exists "etc") test-state)))

(deftest cwd-test
  (is (not ((nl/cwd) test-state)))
  (is ((nl/cwd "etc") test-state))
  (is (not ((nl/cwd "etc" "badfile")))))
