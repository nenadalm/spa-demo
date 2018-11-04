(ns material-ui-icons.core)

(def tags ["RemoveRedEye"
           "ExitToApp"
           "DesktopWindows"
           "List"
           "Edit"
           "People"
           "Delete"
           "Store"
           "Lock"
           "VpnKey"
           "FilterList"
           "LibraryBooks"
           "PlayArrow"
           "Pause"
           "Fullscreen"
           "FullscreenExit"
           "Close"
           "Folder"
           "FileUpload"])

(defn react-import [tag]
  `(def ~(symbol tag) (reagent.core/adapt-react-class (goog.object/getValueByKeys js/window
                                                                                  "MaterialUiIcons"
                                                                                  ~tag))))

(defmacro export []
  `(do
     ~@(map react-import tags)))
