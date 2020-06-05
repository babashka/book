#!/usr/bin/env bb

(defn test-runner []
  (let [cmd ["clojure" "-A:kaocha"
             "-m" "kaocha.runner" "--watch"
             "--no-color"]
        pb (ProcessBuilder. cmd)
        proc (.start pb)
        out (.getInputStream proc)]
    (-> (Runtime/getRuntime)
        (.addShutdownHook (Thread. #(.destroy proc))))
    {:proc proc
     :out out}))

(require '[clojure.string :as str])

(defn process-line
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

(defn stderr
  "Prints line to stderr and returns line."
  [line]
  (binding [*out* *err*]
    (println line))
  line)

(require '[clojure.java.io :as io])

(defn watch
  "Starts test-runner and calls `callback` on EDN representation of output."
  [callback]
  (let [runner (test-runner)]
    (with-open [reader (io/reader (:out runner))]
      (let [lines (line-seq reader)
            lines (keep (comp process-line stderr) lines)]
        (run! callback lines)))))

(defn packet
  "Returns UDP packet"
  [data port]
  (let [bytes (.getBytes data)
        host (java.net.InetAddress/getByName "localhost")]
    (java.net.DatagramPacket. bytes (count bytes)
                              host
                              port)))

(defn send-data [data]
  (.send (java.net.DatagramSocket.)
         (packet data 1738))) ;; <1>

(defn callback [{:keys [:failures :errors]}]
  (if (or (pos? failures)
          (pos? errors))
    (send-data "red") ;; <2>
    (send-data "green")))

(watch callback)
