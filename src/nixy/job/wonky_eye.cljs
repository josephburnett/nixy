(ns nixy.job.wonky-eye
  (:require
   [nixy.job :as job]
   [nixy.command.config :as config]
   [clojure.string :as str]))

(defmethod job/definition :wonky-eye [_]
  {:setup-fn :wonky-eye
   :guide-fn :wonky-eye
   :complete-fn :wonky-eye
   :title "The Wonky Eye"
   :required-cookies #{:use-ssh :config}
   :intro "Boy do I have a job for you!"
   :help "
One of my eyes has gone wonky. 
Can you help me sort it out? 
I need you to change my left 
eye setting back to normal."
   })

(defmethod job/setup :wonky-eye [{:keys [state]}]
  (update-in
   state
   [:nixy :filesystem :root "etc" "config" :cat]
   #(as-> % c
      (config/parse c)
      (assoc c "eye.right" "wonky")
      (config/write c))))

(defmethod job/guide :wonky-eye [{:keys [state line]}]
  (if (str/starts-with? "config set eye.right normal\n" line)
    {:job true} {}))

(defmethod job/complete? :wonky-eye [{:keys [state]}]
  (as-> state s
    (get-in s [:nixy :filesystem :root "etc" "config" :cat])
    (config/parse s)
    (= "normal" (get s "eye.right"))))
