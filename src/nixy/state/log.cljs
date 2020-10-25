(ns nixy.state.log
  "Functions for interacting with the system log.")

(defn append [state msg]
  (update-in state [:log] #(cons msg %)))
