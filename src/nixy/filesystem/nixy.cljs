(ns nixy.filesystem.nixy
  (:require
   [nixy.command.generic]
   [nixy.command.ls]
   [nixy.command.clear]))

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
