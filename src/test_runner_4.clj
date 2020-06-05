#!/usr/bin/env bb

(require '[clojure.java.io :as io])

(def working-dir (first *command-line-args*)) ;; <1>
(def runner-args (rest *command-line-args*))  ;; <2>

(let [pb (doto (ProcessBuilder.
                (into ["clojure" "-A:kaocha" "-m" "kaocha.runner"] runner-args)) ;; <2>
           (.inheritIO) ;; <3>
           (.directory (io/file working-dir))) ;; <1>
      _ (doto (.environment pb) ;; <4>
          (.put "CI" "false"))
      proc (.start pb)]
  (-> (Runtime/getRuntime)
      (.addShutdownHook (Thread. #(.destroy proc))))
  (System/exit (.waitFor proc))) ;; <5>

