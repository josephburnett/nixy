(ns nixy.cookies
  (:require
   [clojure.set :as set]
   [nixy.cookies.commands :as commands]))

(defn grant [state new-cookies]
  (let [cookies (set/union (:cookies state) new-cookies)]
    (assoc-in state [:cookies] cookies)))

(defn find-new [state cookie-preds]
  (let [active (get-in state [:jobs :active])
        ;; cookies (reduce
        ;;          (fn [cookies {:keys [pred key]}]
        ;;               (if (pred state)
        ;;                 (conj cookies key)
        ;;                 cookies))
        ;;          #{}
        ;;          cookie-preds)]

        ; TODO: query active jobs for cookies
        ;       jobs should say what cookies they are offering (rename)
        ;       this function will dedupe
        ]
    (set/difference cookies (:cookies state))))

(defn grant-new-cookies [state]
  (let [new-cookies (find-new state cookie-preds)
        new-state (grant state new-cookies)]
    (print (str "New cookies: " new-cookies))
    new-state))
