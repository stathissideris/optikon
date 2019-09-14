(ns optikon.vega
  (:require [optikon.js :as js]))

(set! *warn-on-reflection* true)

(defn context []
  (let [ctx (js/context)]
    (js/eval ctx (js/source "resources/vega.js"))
    (js/eval ctx (js/source "resources/vega-lite.js"))
    (js/eval ctx (js/source "resources/render.js"))
    ctx))

(defn render-svg [in-filename out-filename]
  (let [ctx (context)]
    (->> (str "x = render(" (slurp in-filename) ")")
         (js/eval ctx)
         .asHostObject
         deref
         (spit out-filename))))
