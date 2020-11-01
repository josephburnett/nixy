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

(defmethod job/guide :tutorial
  ;; Guide toward the next stage.
  [{:keys [state line]}]
  (cond
    (at? state :tutorial-ssh)
    (if (str/starts-with? "ssh nixy\n" line)
      {:job true} {})
    (at? state :tutorial-cd) {}
    :default {}))

(defmethod job/cookies :tutorial
  ;; Grant cookies for each stage.
  [{:keys [state]}]
  (cond
    (at? state :tutorial-start)
    #{:tutorial-start}
    (at? state :tutorial-ssh)
    #{:tutorial-ssh}
    (at? state :tutorial-cd)
    #{:tutorial-cd}
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
                 "You can send me commands over the network,"
                 "by typing on your laptop keyboard."
                 "But first you have to connect to me."
                 "Type \"ssh nixy\" on your keyboard,"
                 "and then press enter."])
    :default state))
      
                 
  
