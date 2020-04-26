(ns nixy.fixtures
  (:require
   [nixy.state :as state]))

;; https://gist.github.com/danielpcox/c70a8aa2c36766200a95#gistcomment-2759497
(defn- deep-merge [a b]
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
