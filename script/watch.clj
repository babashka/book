#!/usr/bin/env bb

(require '[babashka.pods :as pods])

(pods/load-pod 'org.babashka/filewatcher "0.0.1")
(require '[pod.babashka.filewatcher :as fw])

(require '[etaoin.api :as eta])

(defn asciidoc []
  (load-file "script/compile.clj"))

(asciidoc)

(def browser (eta/chrome))
(def cwd (System/getProperty "user.dir"))

(require '[clojure.java.io :as io])
(def index (io/file cwd (str "gh-pages/"
                             (or (System/getenv "BABASHKA_BOOK_MAIN")
                                 "master")
                             ".html")))

(eta/go browser (str (.toURL index)))

(fw/watch "src" (fn [event]
                  (when (= :write (:type event))
                    (asciidoc)
                    (eta/refresh browser))))

@(promise)
