(ns nixy.job.tutorial
  "The Tutorial job walks through the various unix commands."
  (:require
   [clojure.string :as str]
   [nixy.job :as job]))

(defmethod job/definition :tutorial [_]
  {:title "Tutorial"
   :setup-fn :nothing
   :guide-fn :tutorial
   :cookies-fn :tutorial
   :complete-fn :tutorial
   :required-cookies #{}
   :provides-cookies
   #{:tutorial-ssh :tutorial-cd}})

(defmethod job/guide :tutorial
  ;; 
  [{:keys [state line]}]
  (let [at? (complement #(contains? (:cookies state) %))]
    (cond
      (at? :tutorial-ssh)
      (if (str/starts-with? "ssh nixy\n" line)
        {:job true} {})
      (at? :tutorial-cd) {}
      :default {})))

(defmethod job/cookies :tutorial
  ;;
  [{:keys [state]}]
  (let [at? (complement #(contains? (:cookies state) %))]
    (cond
      (at? :tutorial-ssh)
      (do (print "granting ssh") #{:tutorial-ssh})
      (at? :tutorial-cd)
      (do (print "granting cd") #{:tutorial-cd})
      :default
      (do (print "no cookies for you") #{}))))

(defmethod job/complete? :tutorial [{:keys [state]}]
  (contains? (:cookies state) :tutorial-complete))
