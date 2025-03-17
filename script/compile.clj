#!/usr/bin/env bb

(require '[babashka.process :as p])

(def out-page (str "gh-pages/"
                   (or (System/getenv "BABASHKA_BOOK_MAIN")
                       "master")
                   ".html"))
(p/shell "asciidoctor src/book.adoc -a docinfo=shared -o" out-page)

(binding [*out* *err*]
  (println "Done writing to" out-page))
