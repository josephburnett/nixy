(ns nixy.filesystem.laptop)

(def fs
  {"bin"
   {"ssh"
    {:mod #{:x}
     :exec-fn :ssh
     :args-fn :ssh-to-nixy}}})
