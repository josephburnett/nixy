(ns nixy.cookies.commands
  (:require
   [clojure.string :as str]))

(def preds
  [{:key :ssh-discover
    :pred #(if-let [last-line (last (get-in % [:terminal :history]))]
             (str/starts-with? last-line "ssh")
             false)
    :grats "You have discovered the ssh command!"
    :hint "Use ssh to connect to other computers. 
Type ssh and then the name of the computer you wish to connect to."}])
