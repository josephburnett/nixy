(ns nixy.job
  "Jobs are concurrent tasks (quests) which a user may activate and
  complete. Jobs are activated by obtaining the required `cookies`. And
  jobs are completed by following the `guide` to obtain a desired
  application state. Along the way, jobs provide additional cookies
  which may activate other jobs.")

(defmulti definition
  "Provides the definition of a job."
  (fn [key] key))

(defmulti setup
  "Mutates `state` to setup the job. Return new `state`."
  (fn [{:keys [setup-fn]}] setup-fn))

(defmulti guide
  "Determines if `line` is recommended for this job given `state`. If
  it is, `guide` must return a map which includes `{:job true}`. Other
  keys are permitted to provide additional context."
  (fn [{:keys [guide-fn]}] guide-fn))

(defmulti cookies
  "Provides zero or more cookies give `state`. Called for each active
  job after running a command."
  (fn [{:keys [cookies-fn]}] cookies-fn))

(defmulti complete?
  "A predicate which indicates if the provided job is complete given
  `state`."
  (fn [{:keys [complete-fn]}] complete-fn))

(defmulti post-hook
  "An hook for the active job to do interesting things to the
  application state before returning control to the user."
  (fn [{:keys [post-hook-fn]}] post-hook-fn))
