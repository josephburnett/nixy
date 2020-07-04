(ns nixy.operations.nixy-eyes)

(def op
  {:key :nixy-eyes
   :cookies [:use-ssh]
   :grats "You found a new operation!"
   :help "I need help with my eyeballs. 
Fix the config and reboot Nixy."
   :steps
   [{:key :fix-eye-config
     :help "Copy the config from /var to /etc."
     :guide "cp /var/config /etc/config"
     :pred #(= "good" (get-in [:nixy :filesystem :root "etc" "config" :cat] %))}
    {:key :reboot
     :help "Now reboot to pick up the config."
     :guide "reboot"
     :pred #(= "reboot" (last (get-in [:nixy :history] %)))}]})
