(ns nixy.view.laptop
  (:require
   [nixy.guide :as guide]
   [nixy.view.shape :as shape]))

(def center-x 200)
(def center-y 400)
(def width 300)
(def height 400)

(defn- screen [ctx state]
  (aset ctx "fillStyle" "grey")
  (shape/roundRect ctx
                   center-x
                   (- center-y (* height 0.25))
                   (* width 0.95)
                   (* height 0.40)
                   (* width 0.02))
  (aset ctx "font" "30px courier")
  (aset ctx "fillStyle" "white")
  (.fillText ctx
             (get-in state [:terminal :line])
             (- center-x (* width 0.4))
             (- center-y (* height 0.35))))

(defn- top [ctx state]
  (aset ctx "lineWidth" (* width 0.01))
  (aset ctx "fillStyle" "white")
  (shape/roundRect ctx
                   center-x
                   (- center-y (* height 0.25))
                   width
                   (* height 0.48)
                   (* width 0.02))
  (screen ctx state))
  
(defn- bottom [ctx state]
  (aset ctx "lineWidth" (* width 0.01))
  (aset ctx "fillStyle" "white")
  (shape/roundRect ctx
                   center-x
                   (+ center-y (* height 0.25))
                   width
                   (* height 0.48)
                   (* width 0.02)))

(defn- single-key [ctx state guide key ix iy]
  (let [key-center-x (+ (- center-x (/ width 2))  ; left side
                        (* width 0.06)            ; left margin
                        (* ix (* width 0.097)))   ; horizontal spacing
        key-center-y (+ center-y                  ; top
                        (* height 0.1)            ; top margin
                        (* iy (* height 0.10)))   ; vertical spacing
        key-text (case key
                   "\n" "↵"
                   key)
        key-color (cond
                    (contains? guide key) "yellow"
                    :else "white")]
    (aset ctx "fillStyle" key-color)
    (shape/roundRect ctx
                     key-center-x
                     key-center-y
                     (* width 0.07)               ; key width
                     (* height 0.07)              ; key height
                     (* width 0.015))             ; key bevel
    (aset ctx "font" "15px courier")
    (aset ctx "fillStyle" "black")
    (.fillText ctx
               key-text
               (- key-center-x (* width 0.025))
               key-center-y)))

(defn- row [ctx state guide keys iy]
  (doall (map #(single-key ctx state guide %1 %2 iy)
              keys
              (range))))

(defn- center-line [ctx]
  (.moveTo ctx (- center-x width) center-y)
  (.lineTo ctx (+ center-x width) center-y)
  (.stroke ctx))

(defn draw [ctx state]
  (let [guide (guide/state->guide state)]
    (top ctx state)
    ;(center-line ctx)
    (bottom ctx state)
    (doall (map #(row ctx state guide %1 %2)
                ["qwertyuiop"
                 "asdfghjkl\n"
                 "zxcvbnm"]
                (range)))))