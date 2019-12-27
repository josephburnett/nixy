(ns nixy.command.ls)

(defn exec [current-state]
  (update-in
   current-state [:terminal :output]
   #(let [names (as-> current-state s
                  (get-in s (concat [:filesystem] (:cwd s)))
                  (keys s))]
      (concat % names))))

(defn args-pred [args]
  (= "\n" args))
