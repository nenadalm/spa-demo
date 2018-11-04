(ns app.phrases
  (:require
   [phrase.alpha :refer-macros [defphraser]]))

(defphraser string?
  [_ _]
  "error.string")

