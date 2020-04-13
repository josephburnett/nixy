(ns nixy.view.bubble
  (:require
   [nixy.view.shape :as shape]))

(def center-x 250)
(def center-y 50)
(def width 200)
(def height 80)

(defn draw [ctx state]
  (let [r (* width 0.1)]
    (aset ctx "lineWidth" (* width 0.007))
    (aset ctx "strokeStyle" "black")
    (aset ctx "fillStyle" "white")
    (.beginPath ctx)
    (.moveTo ctx
             (- center-x (* width 0.5))
             center-y)
    (.lineTo ctx
             (- center-x (* width 0.4))
             (- center-y (* height 0.1)))
    (.lineTo ctx
             (- center-x (* width 0.4))
             (- center-y (- (* height 0.5) r)))
    (.quadraticCurveTo ctx
                       (- center-x (* width 0.4))
                       (- center-y (* height 0.5))
                       (- center-x (- (* width 0.4) r))
                       (- center-y (* height 0.5)))
    (.lineTo ctx
             (+ center-x (- (* width 0.5) r))
             (- center-y (* height 0.5)))
    (.quadraticCurveTo ctx
                       (+ center-x (* width 0.5))
                       (- center-y (* height 0.5))
                       (+ center-x (* width 0.5))
                       (- center-y (- (* height 0.5) r)))
    (.lineTo ctx
             (+ center-x (* width 0.5))
             (+ center-y (- (* height 0.5) r)))
    (.quadraticCurveTo ctx
                       (+ center-x (* width 0.5))
                       (+ center-y (* height 0.5))
                       (+ center-x (- (* width 0.5) r))
                       (+ center-y (* height 0.5)))
    (.lineTo ctx
             (- center-x (- (* width 0.4) r))
             (+ center-y (* height 0.5)))
    (.quadraticCurveTo ctx
                       (- center-x (* width 0.4))
                       (+ center-y (* height 0.5))
                       (- center-x (* width 0.4))
                       (+ center-y (- (* height 0.5) r)))
    (.lineTo ctx
             (- center-x (* width 0.4))
             (+ center-y (* height 0.1)))
    (.lineTo ctx
             (- center-x (* width 0.5))
             center-y)
    (.stroke ctx)))
