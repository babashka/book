#!/usr/bin/env bb

(require '[babashka.pods :as pods])
(pods/load-pod "pod-babashka-filewatcher")
(require '[pod.babashka.filewatcher :as fw])

(pods/load-pod "pod-babashka-etaoin")
(require '[pod.babashka.etaoin :as eta])

(require '[babashka.process :as p])

(defn asciidoc []
  (println "Compiling asciidoc")
  (-> (p/process ["script/compile.clj"] {:inherit true})
      p/check)
  (println "Done"))

(asciidoc)

(def browser (eta/chrome))
(def cwd (System/getProperty "user.dir"))

(require '[clojure.java.io :as io])
(def index (io/file cwd "gh-pages/index.html"))

(eta/go browser (str (.toURL index)))

(fw/watch "src" (fn [event]
                  (when (= :write (:type event))
                    (asciidoc)
                    (eta/refresh browser))))

@(promise)
