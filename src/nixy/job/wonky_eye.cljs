(ns nixy.job.wonky-eye
  (:require
   [nixy.command.config :as config]))

(def job
  {:key :wonky-eye
   :title "The Wonky Eye"
   :required-cookies [:use-ssh :config]
   :intro "Boy do I have a job for you!"
   :help "
One of my eyes has gone wonky. 
Can you help me sort it out? 
I need you to change my left 
eye setting back to normal."
   :setup (fn [state]
            (update-in
             state
             [:nixy :filesystem :root "etc" "config" :cat]
             #(as-> % c
                (config/parse c)
                (assoc c "eye.right" "wonky")
                (config/write c))))
   :guide (fn [state] "something")})
