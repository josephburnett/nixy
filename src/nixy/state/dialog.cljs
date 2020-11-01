(ns nixy.state.dialog)

(defn add
  "Add dialog and switch into dialog mode."
  [state d]
  (as-> state s
    (assoc s :dialog d)
    (assoc s :focus :dialog)))
