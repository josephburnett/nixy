(ns nixy.state)

(def ^:private initial-state
  {:filesystem
   {"bin"
    {"ls"
     {:mod #{:x}
      :exec (fn [current-state]
              (update-in
               current-state [:terminal :output]
               #(let [names (as-> current-state s
                              (get-in s (concat [:filesystem] (:cwd s)))
                              (keys s))]
                  (concat % names))))
      :args-pred #(= "\n" %)}}}

   :cwd []

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
    :output []}

   :errors []})

(def app-state (atom initial-state))
