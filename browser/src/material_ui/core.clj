(ns material-ui.core)

(def tags ["Button"
           "Card"
           "CardActions"
           "CardContent"
           "CardHeader"
           "Backdrop"
           "CircularProgress"
           "CssBaseline"
           "Typography"
           "AppBar"
           "Avatar"
           "Badge"
           "BottomNavigation"
           "BottomNavigationAction"
           "ButtonBase"
           "CardMedia"
           "Checkbox"
           "Chip"
           "ClickAwayListener"
           "Dialog"
           "DialogActions"
           "DialogContent"
           "DialogContentText"
           "DialogTitle"
           "withMobileDialog"
           "Divider"
           "Drawer"
           "ExpansionPanel"
           "ExpansionPanelActions"
           "ExpansionPanelDetails"
           "ExpansionPanelSummary"
           "FormControl"
           "FormGroup"
           "FormLabel"
           "FormHelperText"
           "FormControlLabel"
           "Hidden"
           "Icon"
           "IconButton"
           "Input"
           "InputLabel"
           "InputAdornment"
           "Grid"
           "List"
           "ListItem"
           "ListItemAvatar"
           "ListItemIcon"
           "ListItemSecondaryAction"
           "ListItemText"
           "ListSubheader"
           "Menu"
           "MenuItem"
           "MenuList"
           "Modal"
           "ModalManager"
           "Paper"
           "Popover"
           "Portal"
           "LinearProgress"
           "Radio"
           "RadioGroup"
           "Select"
           "Snackbar"
           "SnackbarContent"
           "Stepper"
           "Step"
           "StepButton"
           "StepContent"
           "StepLabel"
           "MuiThemeProvider"
           "SvgIcon"
           "Switch"
           "Table"
           "TableBody"
           "TableCell"
           "TableFooter"
           "TableHead"
           "TablePagination"
           "TableRow"
           "TableSortLabel"
           "Tabs"
           "Tab"
           "TextField"
           "Toolbar"
           "Tooltip"
           "Slide"
           "Grow"
           "Fade"
           "Collapse"
           "Zoom"])

(defn react-import [tag]
  `(def ~(symbol tag) (reagent.core/adapt-react-class (goog.object/getValueByKeys js/window
                                                                                  "MaterialUi"
                                                                                  ~tag))))

(defmacro export []
  `(do
     ~@(map react-import tags)))

(defmacro with-let [bindings & body]
  (loop [[var-name style-expr :as bindings] bindings
         out `(reagent.core/as-element
               (do ~@body))]
    (if (empty? bindings)
      out
      (recur (drop 2 bindings)
             `[~'material-ui.core/with-styles
               ~style-expr
               (fn [~var-name] ~out)]))))
