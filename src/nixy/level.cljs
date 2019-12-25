(ns ^:figwheel-hooks nixy.level)

(defn file-exists [& path]
  #(if-let [file (get-in (:filesystem %) path)]
     (contains? file :mod)))

(defn dir-exists [& path]
  #(if-let [file (get-in (:filesystem %) path)]
     (not (contains? file :mod))))

(defn cwd [& path]
  #(= (:cwd %) path))

(def levels

  [{:level 0
    :prereq [(file-exists "etc" "badfile")]
    ; :before (modify state)
    ; :after  (modify state)
    :suggest ["cd etc"]
    :say ["I've got a bad file stuck in my filesystem."
          "You can find it in the /etc directory."]
    }

   {:level 1
    :prereq [(file-exists "etc" "badfile")
             (cwd "etc")]
    ; :before (modify state)
    ; :after  (modify state)
    :suggest ["rm badfile"]
    :say ["Please remove the bad file for me."]}])
