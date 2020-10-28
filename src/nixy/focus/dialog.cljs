(ns nixy.focus.dialog
  "Event loops for dialog focus."
  (:require
   [nixy.event :as event]))

(defmethod event/press-key :dialog
  ;; Continue the dialog to completion.
  [state _]
  (if (empty? (:dialog state))
    ;; Focus back on the laptop.
    (as-> state s
      (assoc s :focus :laptop)
      (assoc-in s [:nixy :speech-bubble-text] []))
    ;; Advance to the next line of dialog.
    (as-> state s
      (update-in s [:nixy :speech-bubble-text] (first (:dialog s)))
      (update-in s [:dialog] rest))))
