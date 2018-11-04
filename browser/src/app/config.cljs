(ns app.config)

(def debug?
  ^boolean goog.DEBUG)

(def config (js->clj (goog.object/get js/window "appConfig") :keywordize-keys true))
