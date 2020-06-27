(ns nixy.filesystem.nixy)

(def fs
  {"bin"
   {"ls"
    {:mod #{:x}
     :exec :ls
     :args-pred :enter-only}
    "clear"
    {:mod #{:x}
     :exec :clear
     :args-pred :enter-only}
    "cd"
    {:mod #{:x}
     :exec :cd
     :args-pred :cd}
    "rm"
    {:mod #{:x}
     :exec :rm
     :args-pred :rm}
    "touch"
    {:mod #{:x}
     :exec :touch
     :args-pred :touch}}
   "usr"
   {}})
