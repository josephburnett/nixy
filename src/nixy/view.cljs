(ns nixy.view
  (:require
   [nixy.state :as state]
   [goog.dom :as gdom]))

; TODO: resize based on width and height
(defn draw-nixy [ctx current-state]
  (aset ctx "lineWidth" 5)
  (.strokeRect ctx 3 3 50 50))

(defn draw-speech-bubble [ctx current-state]
  (aset ctx "lineWidth" 4)
  (.strokeRect ctx 70 3 120 50))

(defn draw-keyboard [ctx current-state]
  (aset ctx "lineWidth" 3)
  (.strokeRect ctx 3 70 200 100))

(defn draw-output [ctx current-state]
  (aset ctx "font" "10px courier")
  (.fillText ctx (get-in current-state [:terminal :line]) 10 90))

(defn render []
  (let [canvas (gdom/getElement "app")
        ctx (.getContext canvas "2d")
        current-state @state/app-state]
    (aset canvas "width" (get-in current-state [:view :width]))
    (aset canvas "height" (get-in current-state [:view :height]))
    (draw-nixy ctx current-state)
    (draw-speech-bubble ctx current-state)
    (draw-keyboard ctx current-state)
    (draw-output ctx current-state)))
