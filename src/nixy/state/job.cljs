(ns nixy.state.job
  "Functions for interacting with state jobs."
  (:require
   [clojure.set :as set]
   [nixy.job :as job]
   [nixy.state.log :as log]))

(defn find-jobs
  "Finds jobs which have satisfied their required cookies given
  `state` and a list of `jobs` to consider."
  [state jobs]
  (let [satisfied (into #{}
                        (filter #(let [j (job/definition %)]
                                   (set/superset?
                                    (:cookies state)
                                    (:required-cookies j)))
                                jobs))]
    (set/difference satisfied (get-in state [:jobs :all]))))

(defn activate
  "Given a collection of `jobs` and `state`, runs the setup function
  for each and record as being activated. If no job was previously
  selected as current, choose the first one."
  [state jobs]
  (as-> state s
    (reduce #(update-in %1 [:jobs :all] conj %2) s jobs)     ; all jobs ever activated
    (reduce #(update-in %1 [:jobs :active] conj %2) s jobs)  ; currently active jobs
    (reduce #(job/setup (merge (job/definition %2)           ; setup jobs
                           {:state %1})) s jobs)
    (if (nil? (get-in s [:jobs :current]))                   ; maybe set current
      (assoc-in s [:jobs :current] (first jobs)) s)))

(defn find-complete
  "Finds activated jobs which satisfy their complete? function given
  `state`."
  [state]
  (into #{}
        (filter #(let [j (job/definition %)]
                   (job/complete? (merge j
                                     {:state state})))
                (get-in state [:jobs :active]))))

(defn complete
  "Given `state` and a collection of `jobs`, record them as being
  complete. Select a new job as current if there is one available."
  [state jobs]
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

(defn activate-new-jobs
  "Find new `jobs` given `state` and activate them."
  [state jobs]
  (let [new (find-jobs state jobs)]
    (activate state new)))

(defn complete-active-jobs
  "Given `state` check active jobs for completion, and move them to a
  completed state."
  [state]
  (let [comp (find-complete state)]
    (complete state comp)))

(defn find-cookies
  "Find new cookies to grant by querying each active job in `state`."
  [state]
  (let [all-cookies (reduce
                     (fn [all-cookies j]
                       (job/cookies (merge (job/definition j)
                                       {:state state})))
                     #{}
                     (get-in state [:jobs :active]))]
    (set/difference all-cookies (:cookies state))))

(defn grant
  "Grant a collection of `new-cookies` in `state`."
  [state new-cookies]
  (let [cookies (set/union (:cookies state) new-cookies)]
    (assoc-in state [:cookies] cookies)))

(defn grant-new-cookies
  "Find new cookies and grant them in `state`."
  [state]
  (let [new-cookies (find-cookies state)]
    (as-> state s
      (reduce log/append s (map #(str "granted cookie: " (name %)) new-cookies))
      (grant s new-cookies))))

