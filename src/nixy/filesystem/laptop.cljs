(ns nixy.filesystem.laptop)

(def fs
  {"bin"
   {"ssh"
    {:mod #{:x}
     :exec :ssh
     :args-pred :ssh-to-nixy}}})
