(ns nixy.job
  "Jobs are concurrent tasks (quests) which a user may activate and
  complete. Jobs are activated by obtaining the required cookies. And
  jobs are completed by following the `guide` to obtain a desired
  application state. Along the way, jobs provide additional cookies
  which may activate other jobs."
  (:require
   [clojure.set :as set]))

(defmulti definition
  "Provides the definition of a job."
  (fn [key] key))

(defmulti setup
  "Mutates `state` to setup the job."
  (fn [{:keys [setup-fn]}] setup-fn))

(defmulti guide
  "Determines if `line` is recommended for this job given `state`. If
  it is, `guide` must return a map which includes `{:job true}`. Other
  keys are permitted to provide additional context."
  (fn [{:keys [guide-fn]}] guide-fn))

;; Maybe provide cookies based on the current state. Provided cookies
;; may unlock other jobs.
(defmulti cookies
  (fn [{:keys [cookies-fn]}] cookies-fn))

;; Is this job complete?
(defmulti complete?
  (fn [{:keys [complete-fn]}] complete-fn))

;; Given a list of jobs (keys) find those which satisfy their required
;; cookies and have not been previously activated.
(defn find-jobs [state jobs]
  (let [satisfied (into #{}
                        (filter #(let [j (definition %)]
                                   (set/superset?
                                    (:cookies state)
                                    (:required-cookies j)))
                                jobs))]
    (set/difference satisfied (get-in state [:jobs :all]))))

;; Given a collection of jobs, run the setup function for each and
;; record as being activated. If no job was previously selected as
;; current, choose the first one.
(defn activate [state jobs]
  (as-> state s
    (reduce #(update-in %1 [:jobs :all] conj %2) s jobs)     ; all jobs ever activated
    (reduce #(update-in %1 [:jobs :active] conj %2) s jobs)  ; currently active jobs
    (reduce #(setup (merge (definition %2)                   ; setup jobs
                           {:state %1})) s jobs)
    (if (nil? (get-in s [:jobs :current]))                   ; maybe set current
      (assoc-in s [:jobs :current] (first jobs)) s)))

;; Find activated jobs which satisfy their complete? function.
(defn find-complete [state]
  (into #{}
        (filter #(let [j (definition %)]
                   (complete? (merge j
                                     {:state state})))
                (get-in state [:jobs :active]))))

;; Given a collection of jobs, record them as being complete. Select a
;; new job as current if there is one available.
(defn complete [state jobs]
  (as-> state s
    (reduce (fn [state key]
              (update-in state [:jobs :active] #(disj % key)))  ; deactivate jobs
            s jobs)
    (if (not-any? #(= (get-in s [:jobs :current]) %) jobs) s    ; maybe unset current
        (assoc-in s [:jobs :current] nil))
    (reduce #(update-in %1 [:jobs :complete] conj %2) s jobs)   ; complete jobs
    (if (nil? (get-in s [:jobs :current]))                      ; maybe set current
      (assoc-in s [:jobs :current]
                (first (get-in s [:jobs :active]))) s)))

(defn activate-new-jobs [state jobs]
  (let [new (find-jobs state jobs)]
    (activate state new)))

(defn complete-active-jobs [state]
  (let [comp (find-complete state)]
    (complete state comp)))

;; Find new cookies to grant by querying each active job.
(defn find-cookies [state]
  (let [all-cookies (reduce
                     (fn [all-cookies job]
                       (cookies (merge (definition job)
                                       {:state state})))
                     #{}
                     (get-in state [:jobs :active]))]
    (set/difference all-cookies (:cookies state))))

;; Grant a collection of cookies.
(defn grant [state new-cookies]
  (let [cookies (set/union (:cookies state) new-cookies)]
    (assoc-in state [:cookies] cookies)))

(defn grant-new-cookies [state]
  (let [new-cookies (find-cookies state)]
    (grant state new-cookies)))
