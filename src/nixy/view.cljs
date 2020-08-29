(ns nixy.view
  "Rendering of the current application state."
  (:require
   [nixy.view.shape :as shape]
   [nixy.view.laptop :as laptop]
   [nixy.view.character :as character]
   [nixy.view.bubble :as bubble]
   [goog.dom :as gdom]))

;; TODO: resize with the window
(def width 500)
(def height 600)

(defn render
  "Render the top-level components of the UI on a 2-dimensional
  canvas. The components are the user's laptop, the Nixy character and
  her speech bubble."
  [current-state]
  (let [canvas (gdom/getElement "app")
        ctx (.getContext canvas "2d")]
    (aset canvas "width" width)
    (aset canvas "height" height)
    (laptop/draw ctx current-state)
    (character/nixy ctx current-state)
    (bubble/draw ctx current-state)))
