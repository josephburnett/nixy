(ns nixy.filesystem.laptop)

(def fs
  {"bin"
   {"ls"
    {:mod #{:x}
     :exec :ls
     :args-pred :enter-only}
    "clear"
    {:mod #{:x}
     :exec :clear
     :args-pred :enter-only}}})