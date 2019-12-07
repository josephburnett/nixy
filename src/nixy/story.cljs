(ns nixy.story)

(def guide
  [{:level 0
    :prereq [#(not (nil? (get-in % [:files "etc" "badfile"])))]
    :suggest ["cd etc"]}
   {:level 1
    :prereq [#(not (nil? (get-in % [:files "etc" "badfile"])))
             #(= ["etc"] (:cwd %))]
    :suggest ["rm badfile"]}])
