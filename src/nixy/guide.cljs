(ns nixy.guide)

(def levels
  [{:level 0
    :prereq [#(not (nil? (get-in % [:files "etc" "badfile"])))]
    :suggest ["cd etc"]}
   {:level 1
    :prereq [#(not (nil? (get-in % [:files "etc" "badfile"])))
             #(= ["etc"] (:cwd %))]
    :suggest ["rm badfile"]}])

(defn file-exists [& path]
  #(if-let [file (get-in (:filesystem %) path)]
     (contains? file :contents)))

(defn dir-exists [& path]
  #(if-let [file (get-in (:filesystem %) path)]
     (not (contains? file :contents))))
