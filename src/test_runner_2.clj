#!/usr/bin/env bb

(defn test-runner []
  (let [cmd ["clojure" "-A:kaocha"
             "-m" "kaocha.runner" "--watch"
             "--no-color"] ;; <1>
        pb (ProcessBuilder. cmd)
        proc (.start pb)
        out (.getInputStream proc)] ;; <2>
    (-> (Runtime/getRuntime)
        (.addShutdownHook (Thread. #(.destroy proc))))
    {:proc proc
     :out out}))

(require '[clojure.string :as str])

(defn process-line ;; <3>
  "Returns EDN representation of test runner output."
  [line]
  (when (re-matches #"\d+ tests.*" line)
    (let [splits (str/split line #"[,.]\s*")
          keyvals (map (fn [s]
                         (let [[n k] (str/split s #" ")]
                           [(keyword k) (Integer. n)]))
                       splits)]
      (into {:failures 0 :errors 0} ;; defaults
            keyvals))))

(defn stderr ;; <4>
  "Prints line to stderr and returns line."
  [line]
  (binding [*out* *err*]
    (println line))
  line)

(require '[clojure.java.io :as io])

(defn watch ;; <5>
  "Starts test-runner and calls `callback` on EDN representation of output."
  [callback]
  (let [runner (test-runner)]
    (with-open [reader (io/reader (:out runner))]
      (let [lines (line-seq reader)
            lines (keep (comp process-line stderr) lines)]
        (run! callback lines)))))

(watch prn) ;; <6>
