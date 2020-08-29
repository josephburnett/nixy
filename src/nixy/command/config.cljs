(ns nixy.command.config
  "Config gets and sets key, value pairs in the system configuration
  file."
  (:require
   [clojure.string :as str]))

(defn parse
  "Parse a `config` file. The format is one entry per line. Each entry
  is a key, value pair separated by '='. Output is a string, string
  map."
  [config]
  (->> config
       (str/split-lines)
       (filter #(not (str/starts-with? % "#")))
       (map #(str/split % "="))
       (filter #(= 2 (count %)))
       (map #(map str/trim %))
       (reduce (fn [config [k v]] (assoc config k v)) {})))

(defn write
  "Write a string, string `config` map the same format expected by
  `parse`."
  [config]
  (->> config
       (into [])
       (map (fn [[k v]] (str k "=" v)))
       (str/join "\n")))
