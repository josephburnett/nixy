(ns nixy.fixtures
  (:require
   [nixy.guide :as ng]
   [nixy.state :as state]))

;; https://gist.github.com/danielpcox/c70a8aa2c36766200a95#gistcomment-2759497
(defn deep-merge [a b]
  (if (map? a)
    (merge-with deep-merge a b)
    b))

(defn merge-state [s]
  (fn [f]
    (reset! state/app-state
            (deep-merge state/initial-state s))
    (f)
    (reset! state/app-state
            state/initial-state)))

(defn merge-initial-state [s]
  (deep-merge state/initial-state s))

;; Impossible required cookies.
(def impossible-cookies [:impossible-to-get])

;; Do nothing guide.
(def null-guide (ng/potential-fn->guide (fn [_] {}) ""))

;; Provide no cookies.
(defn null-cookies [_] #{})

;; Do nothing setup function.
(defn null-setup [state] state)
