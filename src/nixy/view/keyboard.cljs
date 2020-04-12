(ns nixy.view.keyboard
  (:require
   [nixy.guide :as guide]
   [nixy.view.shape :as shape]))

(def center-x 200)
(def center-y 540)
(def width 300)
(def height 100)

(defn- board [ctx state]
  (aset ctx "lineWidth" (* width 0.01))
  (aset ctx "fillStyle" "white")
  (shape/roundRect ctx
                   center-x
                   center-y
                   width
                   height
                   (* width 0.02)))

(defn- my-key [ctx state guide key ix iy]
  (let [key-center-x (+ (- center-x (/ width 2))  ; left side
                        (* width 0.06)            ; left margin
                        (* ix (* width 0.097)))   ; horizontal spacing
        key-center-y (+ (- center-y (/ height 2)) ; top
                        (* height 0.2)            ; top margin
                        (* iy (* height 0.3)))    ; vertical spacing
        key-text (case key
                   "\n" "↵"
                   key)
        key-color (cond
                    (contains? guide key) "black"
                    :else "grey")]
    (aset ctx "fillStyle" "white")
    (shape/roundRect ctx
                     key-center-x
                     key-center-y
                     (* width 0.07)               ; key width
                     (* height 0.23)              ; key height
                     (* width 0.015))             ; key bevel
    (aset ctx "font" "15px courier")
    (aset ctx "fillStyle" key-color)
    (.fillText ctx
               key-text
               (- key-center-x (* width 0.018))
               (+ key-center-y (* height 0.05)))))

(defn- row [ctx state guide keys iy]
  (doall (map #(my-key ctx state guide %1 %2 iy)
              keys
              (range))))

(defn draw [ctx state]
  (let [guide (guide/state->guide state)]
    (board ctx state)
    (doall (map #(row ctx state guide %1 %2)
                ["qwertyuiop"
                 "asdfghjkl\n"
                 "zxcvbnm"]
                (range)))))
