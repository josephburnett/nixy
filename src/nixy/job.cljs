(ns nixy.job
  (:require
   [clojure.set :as set]))

;; Provide a map defining the job.
;; Required keys:
;; - title
;; - setup-fn (key)
;; - guide-fn (key)
;; - complete-fn (key)
;; - required-cookies (set of keys)
(defmulti definition
  (fn [key] key))

;; Setup the conditions of the job.  Must be idempotent.
(defmulti setup
  (fn [{:keys [setup-fn]}] setup-fn))

;; Map valid keys to their capabilities.
;; E.g. {"a" {:valid true} "b" {}}
(defmulti guide
  (fn [{:keys [guide-fn]}] guide-fn))

;; Is this job complete?
(defmulti complete?
  (fn [{:keys [complete-fn]}] complete-fn))

;; Given a list of jobs (keys) find those
;; which satisfy their required cookies
;; and have not been previously activated.
(defn find-new [state jobs]
  (let [satisfied (into #{}
                        (filter #(let [j (definition %)]
                                   (set/superset?
                                    (:cookies state)
                                    (:required-cookies j)))
                                jobs))]
    (set/difference satisfied (get-in state [:jobs :all]))))

;; Given a collection of jobs, run the setup function for each and
;; record as being activated. If no job was previously selected,
;; choose the first one.
(defn activate [state jobs]
  (as-> state s
    (reduce #(update-in %1 [:jobs :all] conj #{%2}) s jobs)     ; all jobs ever activated
    (reduce #(update-in %1 [:jobs :active] conj #{%2}) s jobs)  ; currently active jobs
    (reduce #(setup (merge (definition %2)                      ; setup jobs
                           {:state %1})) s jobs)
    (if (nil? (get-in s [:jobs :current]))                      ; maybe set current
      (assoc-in s [:jobs :current] (first jobs)) s)))

;; Find activated jobs which satisfy their complete? function.
(defn find-complete [state]
  (filter #(let [j (definition %)]
             (complete? (merge j
                               {:state state})))
          (get-in state [:jobs :active])))

;; Given a collection of jobs, record them as being complete. Select a
;; new job.
(defn complete [state jobs]
  (as-> state s
    (reduce
     (fn [state key] (update-in state [:jobs :active] #(disj % #{key})))  ; deactivate jobs
     s jobs)
    (reduce #(update-in %1 [:jobs :completed] conj #{%2}) s jobs)         ; complete jobs
    (if (nil? (get-in s [:jobs :current]))                                ; maybe set current
      (assoc-in s [:jobs :current] (first jobs)) s)))

(defn activate-new-jobs [state jobs]
  (let [new (find-new state jobs)]
    (activate state new)))

(defn complete-active-jobs [state]
  (let [comp (find-complete state)]
    (complete state comp)))
