(ns nixy.state
  "Initial and current application state."
  (:require
   [nixy.filesystem.nixy :as nixy]
   [nixy.filesystem.laptop :as laptop]))

(def initial-state
  {:nixy {:filesystem {:root nixy/fs
                       :cwd []}
          :history []
          :appearance {:blinking :true
                       :color "#fff"}
          :speech-bubble-text []}
   :laptop {:filesystem {:root laptop/fs
                         :cwd []}
            :history []}
   :terminal {:fs :laptop
              :line ""
              :output []}
   :cookies #{}
   :jobs {:current nil
          :all #{}
          :active #{}
          :complete #{}}
   :log []})

(defn with-tutorial [state]
  (as-> state s
    (assoc-in s [:jobs :current] :tutorial)
    (update-in s [:jobs :all] merge :tutorial)
    (update-in s [:jobs :active] merge :tutorial)))

(def app-state (atom (with-tutorial initial-state)))
