(ns nixy.command.config
  (:require
   [clojure.string :as str]))

(defn parse [config]
  (->> config
       (str/split-lines)
       (filter #(not (str/starts-with? % "#")))
       (map #(str/split % "="))
       (filter #(= 2 (count %)))
       (map #(map str/trim %))
       (reduce (fn [config [k v]] (assoc config k v)) {})))

(defn write [config]
  (->> config
       (into [])
       (map (fn [[k v]] (str k "=" v)))
       (str/join "\n")))
