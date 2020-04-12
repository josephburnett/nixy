(ns nixy.view.output
  (:require
   [nixy.view.shape :as shape]))

(def center-x 100)
(def center-y 500)
(def width 190)
(def height 100)

(defn draw [ctx state]
  (aset ctx "font" "30px courier")
  (.fillText ctx
             (get-in state [:terminal :line])
             (- center-x (/ width 2))
             (- center-y (/ height 2))))
