(ns optikon.core
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [org.graalvm.polyglot Context Value Source]))

;; https://www.graalvm.org/sdk/javadoc/org/graalvm/polyglot/Source.html
;; https://www.graalvm.org/docs/reference-manual/embed/
;; https://vega.github.io/vega/docs/api/view/

(set! *warn-on-reflection* true)

(defmacro papply [fun & args]
  (let [arg-names (take (count args) (repeatedly gensym))]
    `(let ~(vec (mapcat (fn [n arg] [n `(future ~arg)]) arg-names args))
       (apply ~fun ~@(map (fn [arg] `(deref ~arg)) arg-names)))))

(defn ^Value eval-js [^Context context ^String code]
  (.eval context "js" code))

(defn context-js []
  (-> (Context/newBuilder
       (into-array ["js"]))
      (.allowAllAccess true)
      (.build)))

(defn source [filename]
  (let [file (io/file filename)
        lang (Source/findLanguage file)]
    ;;(println lang "detected")
    (-> (Source/newBuilder lang file) .build)))

(defn -main [filename]
  (let [^Context ctx (context-js)]
    (println (str (.eval ctx (source filename))))))

(comment
  (def ctx (context-js))
  (println "Sanity check:" (= 4 (.asInt (.eval ctx "js" "1+1+1+1"))))

  (defonce code
    (papply str
            (slurp "https://cdnjs.cloudflare.com/ajax/libs/vega/5.6.0/vega.js")
            (slurp "https://cdnjs.cloudflare.com/ajax/libs/vega-lite/3.4.0/vega-lite.js")
            (slurp "https://cdnjs.cloudflare.com/ajax/libs/vega-embed/5.1.2/vega-embed.min.js")))

  (eval-js ctx code)

  (do
    (def ctx (context-js))
    ;;(.eval ctx (source "/Users/sideris/devel/third-party/vega-lite/build/vega-lite.js"))
    (.eval ctx (source "resources/vega-lite.js"))
    (.eval ctx (source "resources/vega.js"))
    (val->clj (eval-js ctx "Object.keys(vegaLite)"))
    (val->clj (eval-js ctx "vegaLite.version"))
    (val->clj (eval-js ctx "Object.keys(vega)"))

    (val->clj (eval-js ctx (str "vegaLite.compile(" (str/replace (slurp "test-resources/bar.json") "\n" " ") ");")))
    (val->clj (eval-js ctx (str "vegaLite.compile({})")))


    ;; https://github.com/vega/vega/tree/master/packages/vega-scenegraph
    ;; https://github.com/vega/vega/blob/master/packages/vega-scenegraph/test/svg-string-renderer-test.js
    ;; https://github.com/vega/vega/blob/master/packages/vega-cli/src/render.js

    (sort (into [] (val->clj (eval-js ctx "Object.keys(vega)"))))

    (val->clj (eval-js ctx (str "vega.resetSVGClipId();"
                                "new vega.SVGStringRenderer()"
                                ".initialize(null, 400, 300)"
                                ".render(" (slurp "test-resources/bar.json") ")"
                                ".svg();")))

    (.eval ctx (source "resources/render.js"))
    (val->clj (eval-js ctx (str "render(" (slurp "test-resources/bar.json") ")")))
    ))

(comment
  (def ctx (context-js))

  (do
    (.eval ctx (source "resources/vega.js"))
    (.eval ctx (source "resources/vega-lite.js"))
    (.eval ctx (source "resources/render.js"))
    )

  (eval-js ctx (str "x = render(" (slurp "test-resources/bar.vg.json") ")"))
  (eval-js ctx (str "x = render(" (slurp "test-resources/bar-lite.vg.json") ")"))
  (eval-js ctx "x")
  ;;WORKS!
  (spit "/tmp/svg2.svg" @(.asHostObject (eval-js ctx "x")))
  )

(defmulti val->clj
  (fn [^Value x]
    ;;(prn (bean x))
    (cond (.isString x)         :string
          (.hasArrayElements x) :array
          (.hasMembers x)       :map
          :else                 :other)))

(defmethod val->clj :string
  [^Value x]
  (.asString x))

(defmethod val->clj :map
  [^Value x]
  (.as x java.util.Map))

(defmethod val->clj :array
  [^Value x]
  (.as x java.util.List))

(defmethod val->clj :other
  [x]
  x)
