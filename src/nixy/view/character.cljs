(ns nixy.view.character
  (:require
   [nixy.view.shape :as shape]))

(def center-x 60)
(def center-y 100)

(defn- body [ctx state]
  (aset ctx "lineWidth" 8)
  (aset ctx "fillStyle" "white")
  (shape/roundRect ctx center-x center-y 100 170 20)
  (shape/roundRect ctx center-x (- center-y 23) 80 100 10))


(defn- eyes [ctx state]
  (aset ctx "fillStyle" "blue")
  (shape/ellipse ctx (- center-x 20) (- center-y 30) 4 15)
  (shape/ellipse ctx (+ center-x 20) (- center-y 30) 4 15))

(defn nixy [ctx state]
  (body ctx state)
  (eyes ctx state))
