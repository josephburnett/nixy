(ns nixy.cookies.commands
  (:require
   [clojure.string :as str]))

(def preds
  [{:key :use-ssh
    :pred #(let [fs (get-in % [:terminal :fs])]
             (not (= :laptop fs)))
    :grats "You have discovered the ssh command!"
    :help "Use ssh to connect to other computers. 
Type ssh and then the name of the computer you wish to connect to."}])
