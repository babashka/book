#!/usr/bin/env bb

(require '[babashka.process :as p])

(def main-page (str (or (System/getenv "BABASHKA_BOOK_MAIN")
                        "master")
                    ".html"))

#_:clj-kondo/ignore
@(p/$ asciidoctor src/book.adoc -o ~(str "gh-pages/" main-page ".html") -a docinfo=shared)

(binding [*out* *err*]
  (println "Done writing to" main-page))
