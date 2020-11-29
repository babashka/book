#!/usr/bin/env bb

(require '[babashka.process :as p])

(defn io-prepl []
  (let [cmd ["clojure" "-M"
             "-e" "(require '[clojure.core.server :as s])"
             "-e" "(s/io-prepl)"]                           ;; <1>
        proc (p/process cmd                                 ;; <2>
                        {:inherit true                      ;; <3>
                         :shutdown p/destroy-tree})]        ;; <4>
    proc))

@(io-prepl)                                                 ;; <5>
