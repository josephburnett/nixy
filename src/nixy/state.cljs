(ns nixy.state
  (:require
   [nixy.filesystem.nixy :as nixy]
   [nixy.filesystem.laptop :as laptop]))

(def ^:private initial-state
  {:nixy {:filesystem nixy/fs
          :cwd []
          :appearance {:blinking :true
                       :color "#fff"}
          :speech-bubble-text []}
   :laptop {:filesystem laptop/fs
            :cwd []}
   :terminal {:fs :nixy
              :line ""
              :output []}})

(def app-state (atom initial-state))
