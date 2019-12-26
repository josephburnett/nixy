(ns nixy.state)

(def ^:private initial-state
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
