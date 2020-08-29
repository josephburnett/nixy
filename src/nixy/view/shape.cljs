(ns nixy.view.shape
  "Shape drawing functions for the UI.")

;; https://stackoverflow.com/questions/1255512/how-to-draw-a-rounded-rectangle-on-html-canvas
(defn roundRect
  "Draw a rectangle centered at `cx` by `cy` with width `w`, height
  `h` and rounded corners with radius `r`."
  [ctx cx cy w h r]
  (let [x (- cx (/ w 2))
        y (- cy (/ h 2))]
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
    (.stroke ctx)
    (.fill ctx)))

(defn ellipse
  "Draw an ellipse centered at `x` by `y` with radius `xr` and `xy`
  respectively."
  [ctx x y xr yr]
  (.beginPath ctx)
  (.ellipse ctx x y xr yr 0 0 (* 2 Math.PI))
  (.fill ctx))
