(ns ^:figwheel-hooks nixy.layer)

(def always-true
  (fn [_] true))

(defn file-exists [& path]
  #(if-let [file (get-in (:root (:filesystem %)) path)]
     (contains? file :mod)))

(defn dir-exists [& path]
  #(if-let [file (get-in (:root (:filesystem %)) path)]
     (not (contains? file :mod))))

(defn cwd [& path]
  #(= (:cwd (:filesystem %)) path))

(defn select-conditions [state layer]
  (filter
   (fn [condition]
     (every? #(% state) (:preds condition)))
   (:conditions layer)))

(defn select-max-layer [state layers]
  (->> layers
       (filter #(not-empty (select-conditions state %)))
       last))
