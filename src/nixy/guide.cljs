(ns nixy.guide)

(defn file-exists [& path]
  #(if-let [file (get-in (:filesystem %) path)]
     (contains? file :contents)))

(defn dir-exists [& path]
  #(if-let [file (get-in (:filesystem %) path)]
     (not (contains? file :contents))))

(defn cwd [& path]
  #(= (:cwd %) path))

(def levels
  [{:level 0
    :prereq [(file-exists "etc" "badfile")]
    :suggest ["cd etc"]}
   {:level 1
    :prereq [(file-exists "etc" "badfile")
             (cwd "etc")]
    :suggest ["rm badfile"]}])
