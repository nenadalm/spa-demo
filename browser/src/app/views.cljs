(ns app.views
  (:require
   [material-ui.core :as ui]))

(defn app []
  [:<>
   [ui/CssBaseline]
   [:div "app"]])
