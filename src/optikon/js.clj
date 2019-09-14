(ns optikon.js
  (:require [clojure.java.io :as io])
  (:import [org.graalvm.polyglot Context Value Source])
  (:refer-clojure :exclude [eval]))

(set! *warn-on-reflection* true)

(defn context []
  (-> (Context/newBuilder
       (into-array ["js"]))
      (.allowAllAccess true)
      (.build)))

(defn source [filename]
  (let [file (io/file filename)
        lang (Source/findLanguage file)]
    (-> (Source/newBuilder lang file) .build)))

(defn ^Value eval [^Context ctx code]
  (if (string? code)
    (.eval ctx "js" code)
    (.eval ctx code)))
