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

(def input-writer (io/writer (:in prepl-process)))         ;; <1>

(future
  (loop []
    (when-let [v (read-line)]                              ;; <2>
      (binding [*out* input-writer]                        ;; <3>
        (println v))
      (recur))))

(require '[clojure.edn :as edn])

(def output-reader (java.io.PushbackReader.
                    (io/reader (:out prepl-process))))     ;; <4>

@(future
   (loop []
     (println "Type an expression to evaluate:")
     (let [next-val (edn/read {:eof ::EOF} output-reader)] ;; <5>
       (when-not (identical? ::EOF next-val)
         (println (:form next-val)                         ;; <6>
                  "evaluates to"
                  (:val next-val))
         (recur)))))
