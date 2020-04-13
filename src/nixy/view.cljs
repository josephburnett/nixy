(ns nixy.view
  (:require
   [nixy.view.shape :as shape]
   [nixy.view.character :as character]
   [nixy.view.laptop :as laptop]
   [nixy.view.output :as output]
   [goog.dom :as gdom]))

(defn draw-speech-bubble [ctx current-state]
  (aset ctx "lineWidth" 4)
  (.strokeRect ctx 70 3 120 50))

(defn render [current-state]
  (let [canvas (gdom/getElement "app")
        ctx (.getContext canvas "2d")]
    (aset canvas "width" (get-in current-state [:view :width]))
    (aset canvas "height" (get-in current-state [:view :height]))
    (draw-speech-bubble ctx current-state)
    (laptop/draw ctx current-state)
    (character/nixy ctx current-state)))
