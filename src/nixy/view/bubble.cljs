(ns nixy.view.bubble
  "The speech bubble is a top-level UI component which shows the words
  spoken by Nixy."
  (:require
   [nixy.view.shape :as shape]))

(def center-x 300)
(def center-y 50)
(def width 350)
(def height 80)

(defn draw
  "Give `state` draw the current speech bubble on the 2-dimensional
  canvas `ctx`."
  [ctx state]
  (when (= :dialog (:focus state))
    (let [r (* width 0.1)]
      (aset ctx "lineWidth" (* width 0.007))
      (aset ctx "strokeStyle" "black")
      (aset ctx "fillStyle" "yellow")
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
      (.stroke ctx)
      (.fill ctx)
      (let [dialog (:dialog state)]
        (when-not (empty? dialog)
          (aset ctx "font" "15px courier")
          (aset ctx "fillStyle" "black")
          (.fillText ctx
                     (first (:dialog state))
                     (- center-x (* width 0.35))
                     center-y))))))
               
