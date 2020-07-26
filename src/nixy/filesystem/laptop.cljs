(ns nixy.filesystem.laptop)

(def fs
  {"bin"
   {"ssh"
    {:mod #{:x}
     :exec :ssh
     :args :ssh-to-nixy}}})
