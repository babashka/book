#!/usr/bin/env bb

(require '[babashka.curl :as curl])

(defn get-url [url]
  (println "Downloading url:" url)
  (curl/get url))

(defn write-html [file html]
  (println "Writing file:" file)
  (spit file html))

(let [[url file] *command-line-args*]
  (when (or (empty? url) (empty? file))
    (println "Usage: <url> <file>")
    (System/exit 1))
  (write-html file (:body (get-url url))))
