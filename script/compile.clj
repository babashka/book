#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[babashka.process :as p])

(def target-dir "gh-pages")

(def out-page (str (fs/file target-dir
                            (or (System/getenv "BABASHKA_BOOK_MAIN")
                                "master"))
                   ".html"))
(p/shell "asciidoctor src/book.adoc -a docinfo=shared -o" out-page)

(fs/copy "src/favicon.ico" target-dir {:replace-existing true})

(binding [*out* *err*]
  (println "Done writing to" out-page))
