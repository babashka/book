#!/usr/bin/env bb

;; generate a favicon.ico
;; requires inkscape and imagemagick
;; there are other tools that can do this but this gave good quality results
;; you might never need to run this again, but here if you need it

(require '[babashka.fs :as fs]
         '[babashka.process :as p]
         '[clojure.string :as str])

(def in-file "logo/favicon.svg")
(def out-file "src/favicon.ico")
(def work-dir "target/icon")

(when (fs/exists? work-dir)
  (fs/delete-tree work-dir))
(fs/create-dirs work-dir)

(def linux? (str/includes? (str/lower-case (System/getProperty "os.name"))
                           "nux"))

(def inkscape-runner (cond
                       (fs/which "inkscape")
                       (str (fs/which "inkscape"))

                       (and linux?
                            (fs/which "flatpak")
                            (zero? (-> (p/shell {:out "/dev/null"} "flatpak info org.inkscape.Inkscape")
                                       :exit)))
                       "flatpak run org.inkscape.Inkscape"

                       :else
                       (throw (ex-info "inkscape not found" {}))))

(let [sizes [16 32 48]]
  (doseq [s sizes
          :let [out-file (str (fs/file work-dir  (str s ".png")))]]
    (p/shell inkscape-runner "-w" s "-h" s "-o" out-file in-file))
  (apply p/shell {:dir work-dir}
         (-> ["convert"]
             (into (mapv #(str % ".png") sizes))
             (conj (fs/absolutize out-file)))))

(println "Created" out-file)
