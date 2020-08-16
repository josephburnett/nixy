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

(defn find-new [state jobs]
  (let [satisfied (filter #(let [j (definition %)]
                             (set/superset?
                              (:cookies state)
                              (:required-cookies j)))
                          jobs)]
    (set/difference satisfied (get-in state [:jobs :all]))))

(defn activate [state jobs]
  (as-> state s
    (reduce #(update-in %1 [:jobs :activated] conj #{%2}) s jobs)  ; all jobs every activated
    (reduce #(update-in %1 [:jobs :active] conj #{%2}) s jobs)     ; currently active jobs
    (reduce #(setup (merge (definition %2)                         ; setup jobs
                           {:state %1})) s jobs)
    (if (nil? (get-in s [:jobs :current]))                         ; maybe set current
      (assoc-in s [:jobs :current] (first jobs)) s)))

(defn find-complete [state]
  (filter #(let [j (definition %)]
             (complete? (merge j
                               {:state state})))
          (get-in state [:jobs :active])))

(defn complete [state jobs]
  (as-> state s
    (reduce
     (fn [state key] (update-in state [:jobs :active] #(disj % #{key})))  ; deactivate jobs
     s jobs)
    (reduce #(update-in %1 [:jobs :completed] conj #{%2}) s jobs)         ; complete jobs
    (if (nil? (get-in s [:jobs :current]))                                ; maybe set current
      (assoc-in s [:jobs :current] (first jobs)) s)))
