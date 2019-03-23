(ns optikon.core
  (:require [cheshire.core :as json])
  (:import [org.graalvm.polyglot Context Value])
  (:gen-class))

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

(defn -main [& args]
  (let [ctx (context-js)]
    (println "Sanity check:" (.asInt (eval-js ctx "1+1+1+1")))))

(comment
  (def ctx (context-js))
  (println "Sanity check:" (= 4 (.asInt (.eval ctx "js" "1+1+1+1"))))

  (defonce code
    (papply str
            (slurp "https://cdn.jsdelivr.net/npm/vega@5.3.0")
            (slurp "https://cdn.jsdelivr.net/npm/vega-lite@3.0.0-rc15")
            (slurp "https://cdn.jsdelivr.net/npm/vega-embed@4.0.0-rc1")))

  (eval-js ctx code)

  )


;; compile with:
;;
;; clojure -A:native-image && mv js optikon
