#!/usr/bin/env bb

(require '[babashka.process :as p])

(defn io-prepl []
  (let [cmd ["clojure" "-M"
             "-e" "(require '[clojure.core.server :as s])"
             "-e" "(s/io-prepl)"]
        proc (p/process cmd
                        {:shutdown p/destroy-tree})]
    proc))

(def prepl-process (io-prepl))

(require '[clojure.java.io :as io])

(def input-writer (io/writer (:in prepl-process)))     ;; <1>
(def output-reader (java.io.PushbackReader.
                    (io/reader (:out prepl-process)))) ;; <2>

(require '[clojure.edn :as edn])

(loop []
  (println "Type an expression to evaluate:")
  (when-let [v (read-line)]                               ;; <3>
    (binding [*out* input-writer]                         ;; <4>
      (println v))
    (let [next-val (edn/read {:eof ::EOF} output-reader)] ;; <5>
      (when-not (identical? ::EOF next-val)
        (println (:form next-val)                         ;; <6>
                 "evaluates to"
                 (:val next-val))
        (recur)))))
