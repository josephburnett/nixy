(ns nixy.job
  (:require
   [clojure.set :as set]))

(defmulti definition
  (fn [key] key))

(defmulti setup
  (fn [{:keys [setup-fn]}] setup-fn))

(defmulti guide
  (fn [{:keys [guide-fn]}] guide-fn))

(defmulti complete?
  (fn [{:keys [complete-fn]}] complete-fn))

(defn activate-new-jobs [state jobs]
  (let [satisfied (filter #(let [j (definition %)]
                             (set/superset?
                              (:cookies state)
                              (:required-cookies j)))
                          jobs)
        new (set/difference satisfied (get-in state [:jobs :all]))]
    (if (empty? new)
      state
      (as-> state s
        (do (print "new jobs " new) s)
        (reduce #(update-in %1 [:jobs :activated] conj #{%2}) s new)  ; idempotent
        (reduce #(update-in %1 [:jobs :active] conj #{%2}) s new)     ; activate jobs
        (reduce #(setup (merge (definition %2)                       ; setup jobs
                                   {:state %1})) s new)
        (if (nil? (get-in s [:jobs :current]))                        ; maybe set current
          (assoc-in s [:jobs :current] (first new)) s)))))

(defn complete-active-jobs [state]
  (let [completed (filter #(let [j (definition %)]
                             (complete? (merge j
                                               {:state state})))
                          (get-in state [:jobs :active]))]
    (if (empty? completed)
      state
      (as-> state s
        (do (print "completed jobs " completed) s)
        (reduce
         (fn [state key] (update-in state [:jobs :active] #(disj % #{key})))  ; deactivate jobs
         s completed)
        (reduce #(update-in %1 [:jobs :completed] conj #{%2}) s completed)    ; complete jobs
        (if (nil? (get-in s [:jobs :current]))                                ; maybe set current
          (assoc-in s [:jobs :current] (first completed)) s)))))
