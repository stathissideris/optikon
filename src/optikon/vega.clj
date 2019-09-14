(ns optikon.vega
  (:require [optikon.js :as js]))

(set! *warn-on-reflection* true)

(def ctx (js/context))

(js/eval ctx (js/source "resources/vega.js"))
(js/eval ctx (js/source "resources/vega-lite.js"))
(js/eval ctx (js/source "resources/render.js"))

(defn render-svg [in-filename out-filename]
  (->> (str "x = render(" (slurp in-filename) ")")
       (js/eval ctx)
       .asHostObject
       deref
       (spit out-filename)))
