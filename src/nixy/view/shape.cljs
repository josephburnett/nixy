(ns nixy.view.shape)

;; https://stackoverflow.com/questions/1255512/how-to-draw-a-rounded-rectangle-on-html-canvas
(defn roundRect [ctx x y w h r]
  (.beginPath ctx)
  (.moveTo ctx (+ x r) y)
  (.lineTo ctx (- (+ x w) r), y)
  (.quadraticCurveTo ctx (+ x w) y (+ x w) (+ y r))
  (.lineTo ctx (+ x w) (- (+ y h) r))
  (.quadraticCurveTo ctx (+ x w) (+ y h) (- (+ x w) r) (+ y h))
  (.lineTo ctx (+ x r) (+ y h))
  (.quadraticCurveTo ctx x (+ y h) x (- (+ y h) r))
  (.lineTo ctx x (+ y r))
  (.quadraticCurveTo ctx x y (+ x r) y)
  (.closePath ctx)
  (.fill ctx))
