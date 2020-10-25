(ns nixy.filesystem.nixy)

(def fs
  {"bin"
   {"ls"
    {:mod #{:x}
     :exec-fn :ls
     :args-fn :enter-only}
    "cat"
    {:mod #{:x}
     :exec-fn :cat
     :args-fn :cat}
    "clear"
    {:mod #{:x}
     :exec-fn :clear
     :args-fn :enter-only}
    "cd"
    {:mod #{:x}
     :exec-fn :cd
     :args-fn :cd}
    "rm"
    {:mod #{:x}
     :exec-fn :rm
     :args-fn :rm}
    "touch"
    {:mod #{:x}
     :exec-fn :touch
     :args-fn :touch}
    "grep"
    {:mod #{:x}
     :exec-fn :grep
     :args-fn :grep}
    "jobs"
    {:mod #{:x}
     :exec-fn :jobs
     :args-fn :enter-only}}
   "usr" {}
   "etc"
   {"config"
    {:mod #{:rw}
     :cat "
eyes.left=normal
eyes.right=normal
"}}})
