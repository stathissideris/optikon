(ns optikon.vega
  (:require [optikon.js :as js]
            [cheshire.core :as json]
            [clojure.edn :as edn]
            [clojure.string :as str]))

(set! *warn-on-reflection* true)

(defn context []
  (let [ctx (js/context)]
    (js/eval ctx (js/source "resources/vega.js"))
    (js/eval ctx (js/source "resources/vega-lite.js"))
    (js/eval ctx (js/source "resources/render.js"))
    ctx))

(defn render-svg [in-filename out-filename]
  (let [ctx   (context)
        input (slurp in-filename)
        input (if (str/ends-with? in-filename ".edn")
                (-> input edn/read-string json/generate-string)
                input)]
    (->> (str "render(" input ")")
         (js/eval ctx)
         .asHostObject
         .get
         (spit out-filename))))
