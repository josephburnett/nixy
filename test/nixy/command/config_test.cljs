(ns nixy.command.config-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [nixy.command.config :refer [parse write]]))

(deftest parse-test
  (is (= {"a" "b"} (parse "a=b")) "single entry")
  (is (= {"a" "b"} (parse " a = b ")) "ignore whitespace")
  (is (= {"a" "b"} (parse "\n\na=b\n\n")) "ignore newlines")
  (is (= {"a" "b"} (parse "#c=d\na=b")) "ignore comments")
  (is (= {"a" "b"} (parse "a=c\na=b")) "use last entry")
  (is (= {"a" "b"
          "c" "d"} (parse "a=b\nc=d")) "multiple entries"))

(deftest write-test
  (is (= "a=b" (write {"a" "b"})) "single entry")
  (is (= "a=b\nc=d" (write {"a" "b" "c" "d"})) "multiple entries"))
