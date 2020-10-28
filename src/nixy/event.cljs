(ns nixy.event
  "Events are handled by the element in focus.")

(defmulti press-key
  "Handle a key press."
  (fn [state _] (:focus state)))
