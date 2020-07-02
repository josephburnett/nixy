(ns nixy.state
  (:require
   [nixy.filesystem.nixy :as nixy]
   [nixy.filesystem.laptop :as laptop]))

(def initial-state
  {:nixy {:filesystem {:root nixy/fs
                       :cwd []}
          :appearance {:blinking :true
                       :color "#fff"}
          :speech-bubble-text []}
   :laptop {:filesystem {:root laptop/fs
                         :cwd []}}
   :terminal {:fs :laptop
              :line ""
              :output []}
   :cookies #{}})

(def app-state (atom initial-state))
