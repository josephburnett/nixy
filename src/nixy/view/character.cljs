(ns nixy.view.character
  "Character is the top-level UI component which displays the Nixy
  character."
  (:require
   [nixy.view.shape :as shape]))

(def center-x 60)
(def center-y 100)
(def width 100)
(def height 170)

(defn- body
  "Draw Nixy's body."
  [ctx state]
  (aset ctx "lineWidth" (* width 0.03))
  (aset ctx "fillStyle" "white")
  (shape/roundRect ctx
                   center-x
                   center-y
                   width
                   height
                   (* width 0.2))
  (shape/roundRect ctx
                   center-x
                   (- center-y (* height 0.09))
                   (* width 0.8)
                   (* height 0.7)
                   (* width 0.1)))

(defn- eyes
  "Draw Nixy's eyes."
  [ctx state]
  (aset ctx "fillStyle" "blue")
  (shape/ellipse ctx
                 (- center-x (* width 0.12))
                 (- center-y (* height 0.18))
                 (* width 0.03)
                 (* height 0.07))
  (shape/ellipse ctx
                 (+ center-x (* width 0.12))
                 (- center-y (* height 0.18))
                 (* width 0.03)
                 (* height 0.07)))

(defn nixy
  "Draw Nixy's body based on `state` to the 2-dimensional canvas
  `ctx`."
  [ctx state]
  (body ctx state)
  (eyes ctx state))
