(ns nixy.job.tutorial
  "The Tutorial job walks through the various unix commands."
  (:require
   [clojure.string :as str]
   [nixy.job :as job]
   [nixy.state.dialog :as dialog]))

(defmethod job/definition :tutorial [_]
  {:title "Tutorial"
   :setup-fn :nothing
   :guide-fn :tutorial
   :cookies-fn :tutorial
   :complete-fn :tutorial
   :post-hook-fn :tutorial
   :required-cookies #{}
   :provides-cookies #{:tutorial-ssh :tutorial-cd}})

(defn at?
  "At or before given cookie because it is not present."
  [state k]
  (not (contains? (:cookies state) k)))

(defn after?
  "After given cookies because it is present."
  [state k]
  (contains? (:cookies state) k))

(defmethod job/guide :tutorial
  ;; Guide toward the next stage.
  [{:keys [state line]}]
  (cond
    (and (after? state :tutorial-explain-keyboard)
         (at? state :tutorial-jobs))
    (if (str/starts-with? "jobs\n" line)
      {:job true} {})
    :default {}))

(defmethod job/cookies :tutorial
  ;; Grant cookies for each stage.
  [{:keys [state]}]
  (cond
    (at? state :tutorial-start)
    #{:tutorial-start}
    (at? state :tutorial-ssh)
    (if (= "ssh nixy" (first (get-in state [:nixy :history])))
      #{:tutorial-ssh} #{})
    (at? state :tutorial-explain-keyboard)
    (if (= 2 (count (get-in state [:nixy :history])))
      #{:tutorial-explain-keyboard} #{})
    (at? state :tutorial-jobs)
    (if (= "jobs" (first (get-in state [:nixy :history])))
      #{:tutorial-jobs} #{})
    :default
    #{}))

(defmethod job/complete? :tutorial [{:keys [state]}]
  ;; Check for last cookie.
  (contains? (:cookies state) :tutorial-complete))

(defmethod job/post-hook :tutorial [{:keys [state]}]
  ;; Add dialog.
  (cond
    (at? state :tutorial-ssh)
    (dialog/add state
                ["Hi, my name is Nixy!"
                 "I am a unix computer."
                 "I am connected to the network,"
                 "just like your laptop."
                 "You can send me commands"
                 "over the network by typing"
                 "on your laptop keyboard."
                 "But first you must connect to me."
                 "Type \"ssh nixy\" on your keyboard,"
                 "and then press enter."])
    (at? state :tutorial-explain-keyboard)
    (dialog/add state
                ["Nice job! Now you are"
                 "connected to me, nixy."
                 "Use your keyboard to type"
                 "a command. Anything will do."
                 "The highlighted keys"
                 "will guide you."])
    (at? state :tutorial-jobs)
    (dialog/add state
                ["You are really getting"
                 "the hang of this!"
                 "Now, I have a job for you."
                 "I need your help to fix"
                 "a glitch in my system."
                 "But first, let's make sure"
                 "you know how to manage a job."
                 "Type \"jobs\"."
                 "The red keys will guide you."])
    (at? state :tutorial-explain-jobs)
    (dialog/add state
                ["Bravo!"
                 "As you can see,"
                 "your current job is the tutorial."])
    :default state))
      
                 
  
