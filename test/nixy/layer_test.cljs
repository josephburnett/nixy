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

(def test-layers
  [{:name "beginning"
    :conditions
    [{:preds [nl/always-true]}]}
   {:name "middle"
    :conditions
    [{:preds [(nl/cwd "etc")]}
     {:preds [(nl/cwd "var")]}]}
   {:name "end"
    :conditions
    [{:preds [(complement (nl/file-exists "etc" "badfile"))]}]}])

(deftest select-conditions-test
  (let [given-layer (fn [name state]
                      (->> test-layers
                           (filter #(= name (:name %)))
                           first
                           (nl/select-conditions state)))
        with-state (fn [path value]
                     (assoc-in test-state path value))]
    (is (= 1 (count (given-layer "middle" (with-state [:cwd] ["etc"])))))
    (is (= 1 (count (given-layer "middle" (with-state [:cwd] ["var"])))))
    (is (= 0 (count (given-layer "middle" (with-state [:cwd] ["usr"])))))))
