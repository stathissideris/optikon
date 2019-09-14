(ns optikon.cli
  (:require [optikon.vega :as vega]))

(defn -main [in-filename out-filename]
  (vega/render-svg in-filename out-filename))
