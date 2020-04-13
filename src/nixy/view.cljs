(ns nixy.view
  (:require
   [nixy.view.shape :as shape]
   [nixy.view.laptop :as laptop]
   [nixy.view.character :as character]
   [nixy.view.bubble :as bubble]
   [goog.dom :as gdom]))

(defn render [current-state]
  (let [canvas (gdom/getElement "app")
        ctx (.getContext canvas "2d")]
    (aset canvas "width" (get-in current-state [:view :width]))
    (aset canvas "height" (get-in current-state [:view :height]))
    (laptop/draw ctx current-state)
    (character/nixy ctx current-state)
    (bubble/draw ctx current-state)))
