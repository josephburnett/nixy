(ns nixy.view.laptop
  "Laptop is the user's input device and the starting point of the
  Nixy game."
  (:require
   [nixy.state.terminal :as terminal]
   [nixy.guide :as guide]
   [nixy.view.shape :as shape]))

(def center-x 200)
(def center-y 400)
(def width 300)
(def height 400)

(defn- screen
  "Draw the screen."
  [ctx state]
  (let [prompt (terminal/prompt state)
        lines (cons prompt
                    (reverse (get-in state [:terminal :output])))]
    (aset ctx "fillStyle" "grey")
    (shape/roundRect ctx
                     center-x
                     (- center-y (* height 0.25))
                     (* width 0.95)
                     (* height 0.40)
                     (* width 0.02))
    (aset ctx "font" "15px courier")
    (aset ctx "fillStyle" "white")
    (doall (map #(.fillText ctx
                            %1
                            (- center-x (* width 0.4))      ; left margin
                            (- (- center-y (* height 0.1))
                               (* height 0.05 %2)))         ; line spacing
                lines
                (range)))))

(defn- top
  "Draw the top half of the laptop."
  [ctx state]
  (aset ctx "lineWidth" (* width 0.01))
  (aset ctx "fillStyle" "white")
  (shape/roundRect ctx
                   center-x
                   (- center-y (* height 0.25))
                   width
                   (* height 0.48)
                   (* width 0.02))
  (screen ctx state))
  
(defn- bottom
  "Draw the bottom half of the laptop."
  [ctx state]
  (aset ctx "lineWidth" (* width 0.01))
  (aset ctx "fillStyle" "white")
  (shape/roundRect ctx
                   center-x
                   (+ center-y (* height 0.25))
                   width
                   (* height 0.48)
                   (* width 0.02)))

(defn- single-key
  "Draw a single `key` on the keyboard at location `ix` by
  `iy`. Display the key's capabilities based on the provided `guide`."
  [ctx state guide key ix iy]
  (let [focused (= :laptop (:focus state))
        key-center-x (+ (- center-x (/ width 2))  ; left side
                        (* width 0.06)            ; left margin
                        (* ix (* width 0.097)))   ; horizontal spacing
        key-center-y (+ center-y                  ; top
                        (* height 0.1)            ; top margin
                        (* iy (* height 0.10)))   ; vertical spacing
        key-text (case key
                   "\n" "↵"
                   key)
        key-text-color (cond
                         (and focused (get-in guide [key :job])) "white"
                         :else "black")
        key-color (cond
                    (and focused (get-in guide [key :job])) "#e66"
                    (and focused (get-in guide [key :valid])) "yellow"
                    :else "white")]
    (aset ctx "fillStyle" key-color)
    (shape/roundRect ctx
                     key-center-x
                     key-center-y
                     (* width 0.07)               ; key width
                     (* height 0.07)              ; key height
                     (* width 0.015))             ; key bevel
    (aset ctx "font" "15px courier")
    (aset ctx "fillStyle" key-text-color)
    (.fillText ctx
               key-text
               (- key-center-x (* width 0.025))
               key-center-y)))

(defn- row
  "Draw a row of keys on the keyboard."
  [ctx state guide keys iy]
  (doall (map #(single-key ctx state guide %1 %2 iy)
              keys
              (range))))

(defn draw
  "Draw the user's laptop, showing the current terminal output and the
  keyboard with information from the guide."
  [ctx state]
  (let [guide (guide/state->guide state)]
    (top ctx state)
    (bottom ctx state)
    (doall (map #(row ctx state guide %1 %2)
                ["qwertyuiop"
                 "asdfghjkl\n"
                 "zxcv bnm.|"]
                (range)))))
