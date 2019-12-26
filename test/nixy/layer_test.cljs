(ns nixy.layer-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.layer :as nl]))

(def test-state
  {:cwd ["etc"]
   :filesystem
   {"etc"
    {"badfile" {:mod #{:r}}}}})

(deftest file-exists-test
  (let [given #((apply nl/file-exists %) test-state)]
    (is (given ["etc" "badfile"]))
    (is (not (given ["etc" "goodfile"])))
    (is (not (given ["etc"])))))

(deftest dir-exists-test
  (let [given #((apply nl/dir-exists %) test-state)]
    (is (not (given ["etc" "badfile"])))
    (is (not (given ["etc" "goodfile"])))
    (is (given ["etc"]))))

(deftest cwd-test
  (let [given #((apply nl/cwd %) test-state)]
    (is (not (given [])))
    (is (given ["etc"]))
    (is (not (given ["etc" "badfile"])))))
