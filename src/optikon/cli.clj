(ns optikon.cli
  (:require [optikon.vega :as vega])
  (:gen-class))

(def dummy (atom nil))

(defn -main [in-filename out-filename]
  (reset! dummy 1)
  (vega/render-svg in-filename out-filename))
