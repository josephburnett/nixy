(ns nixy.cookies
  (:require
   [clojure.set :as set]))

(defn grant [state new-cookies]
  (let [cookies (set/union (:cookies state) new-cookies)]
    (assoc-in state [:cookies] cookies)))

(defn new [state cookie-preds]
  (let [cookies (reduce
                 (fn [cookies {:keys [pred key]}]
                      (if (pred state)
                        (conj cookies key)
                        cookies))
                 #{}
                 cookie-preds)]
    (set/difference cookies (:cookies state))))
    
