(ns ^:figwheel-hooks nixy.core
  (:require
   [nixy.guide :as ng]
   [clojure.string :as str]
   [goog.dom :as gdom]
   [goog.events :as gevents]))

(defn multiply [a b] (* a b))

(def initial-state
  {:filesystem
   {"bin"
    {"cd"
     {:mod #{:x}
      :x #(print "cd" %)
      :args #(contains? #{" " " a"} %)}}}

   ;; TODO: update this on the 'resize' event.
   :view
   {:width 500
    :height 500}
   
   :character
   {:appearance
    {:blinking :true
     :color "#fff"}
    :speech-bubble-text []}

   :terminal
   {:line ""
    :guide {}}})

(def app-state (atom initial-state))

; TODO: resize based on width and height
(defn draw-nixy [ctx state]
  (aset ctx "lineWidth" 5)
  (.strokeRect ctx 3 3 50 50))

(defn draw-speech-bubble [ctx state]
  (aset ctx "lineWidth" 4)
  (.strokeRect ctx 70 3 120 50))

(defn draw-keyboard [ctx state]
  (aset ctx "lineWidth" 3)
  (.strokeRect ctx 3 70 200 100))

(defn render []
  (let [canvas (gdom/getElement "app")
        ctx (.getContext canvas "2d")
        state @app-state]
    (aset canvas "width" (get-in state [:view :width]))
    (aset canvas "height" (get-in state [:view :height]))
    (draw-nixy ctx state)
    (draw-speech-bubble ctx state)
    (draw-keyboard ctx state)))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (render))

(defn append-state-terminal-line [state key]
  (as-> state s
    (assoc-in s [:terminal :line] (str/join (concat (get-in s [:terminal :line]) key)))
    (assoc-in s [:terminal :guide] (ng/state->guide s))))

;; force initial calculation of guide
(reset! app-state (append-state-terminal-line @app-state ""))

(defn on-key-down [e]
  (let [key (.-key e)
        state @app-state]
    (when (contains? (get-in state [:terminal :guide]) key)
      (reset! app-state (append-state-terminal-line state key)))
    (print (get-in @app-state [:terminal :line]))))

(gevents/listen
 js/window
 goog.events.EventType.KEYDOWN
 on-key-down)
                 
