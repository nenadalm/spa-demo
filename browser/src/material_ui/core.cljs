(ns material-ui.core
  (:refer-clojure :exclude [List])
  (:require
   [reagent.core]
   [react])
  (:require-macros
   [material-ui.core :refer [export]]))

(export)

(def ^:private with-styles* (goog.object/getValueByKeys js/window "MaterialUi" "withStyles"))

(defn with-styles
  "Given a map of {classKey, styles} (or a function which takes a theme & returns such a map),
      returns the same map of {classKey, className}"
  [styles-or-fn body-fn]
  (let [hoc (with-styles*
              (if (fn? styles-or-fn)
                (comp clj->js styles-or-fn)
                (clj->js styles-or-fn)))
        body-component (reagent.core/reactify-component
                        (fn [{:keys [classes]}]
                          [body-fn (-> classes
                                       (js->clj :keywordize-keys true))]))]
    (react/createElement
     (hoc body-component))))
