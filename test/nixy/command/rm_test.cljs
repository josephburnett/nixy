(ns nixy.command.rm-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [nixy.fixtures :refer [merge-state]]
   [nixy.state :as state]
   [nixy.command :as command]
   [nixy.command.rm :as rm]))

(use-fixtures :each
  (merge-state
   {:test-fs
    {:filesystem
     {:root
      {"usr"
       {"file" {}}
       "bin" {}}}}
    :terminal
    {:fs :test-fs}}))

(deftest rm-exec-test
  (let [check (fn [in-dir rm-args]
                (as-> @state/app-state s
                  (assoc-in s [:test-fs :filesystem :cwd] in-dir)
                  (command/exec {:exec :rm} s rm-args)
                  (:state s)
                  (get-in s (concat [:test-fs :filesystem :root] in-dir))))]
    (testing "rm a file"
      (is (= {} (check ["usr"] " file"))))))
      
(deftest rm-args-pred-test
  (let [check (fn [in-dir rm-args]
                (as-> @state/app-state s
                  (assoc-in s [:test-fs :filesystem :cwd] in-dir)
                  (command/args-pred {:args-pred :rm} s rm-args)))]
    (testing "rm in usr"
      (is (= false (check ["usr"] "\n")) "must provide a filename")
      (is (= true (check ["usr"] " ")))
      (is (= true (check ["usr"] " f")))
      (is (= true (check ["usr"] " fi")))
      (is (= true (check ["usr"] " fil")))
      (is (= true (check ["usr"] " file")))
      (is (= true (check ["usr"] " file\n")))
      (is (= false (check ["usr"] " nofile")) "file must exist"))
    (testing "rm is bin"
      (is (= false (check ["bin"] " ")) "bin is off limits"))))

