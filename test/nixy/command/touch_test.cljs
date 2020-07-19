(ns nixy.command.touch-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [nixy.fixtures :refer [merge-state]]
   [nixy.state :as state]
   [nixy.command :as command]
   [nixy.command.touch :as touch]))

(use-fixtures :each
  (merge-state
   {:nixy {:filesystem {:root {"dir" {}
                               "usr" {"existingfile" {}}}}}
    :terminal {:fs :nixy}}))

(def empty-file {:mod #{:r :w}
                 :cat ""})

(deftest touch-exec-test
  (let [check (fn [path filename]
                (as-> @state/app-state s
                  (assoc-in s [:nixy :filesystem :cwd] path)
                  (command/exec {:exec :touch :state s :args (cons " " filename)})
                  (:state s)
                  (get-in s (concat [:nixy :filesystem :root] path [filename]))))]
                  
    (testing "touch"
      (is (= empty-file (check [] "file")))
      (is (= empty-file (check ["dir"] "file"))))))

(deftest touch-args-pred-test
  (let [check (fn [path filename]
                (as-> @state/app-state s
                  (assoc-in s [:nixy :filesystem :cwd] path)
                  (command/args-pred {:args-pred :touch :state s :args filename})))]
    (testing "touch"
      (testing "in /"
        (is (= false (check [] " ")) "cannot create a file"))
      (testing "in /bin"
        (is (= false (check ["bin"] " ")) "cannot create a file"))
      (testing "in /usr"
        (is (= true (check ["usr"] " ")) "can provide parameters")
        (is (= true (check ["usr"] " file")) "can provide a filename")
        (is (= true (check ["usr"] " file\n")) "can create a file")
        (is (= false (check ["usr"] " existingfile\n")) "cannot overwrite another file")))))
