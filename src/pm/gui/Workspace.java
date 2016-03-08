package pm.gui;


import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import pm.PoseMaker;
import pm.PropertyType;
import pm.controller.PoseMakerController;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import saf.ui.AppGUI;
//import saf.ui.AppGUI;
//import saf.AppTemplate;
//import saf.components.AppWorkspaceComponent;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {
    
    static final String CLASS_EDIT_TOOLBAR = "edit_toolbar";
    static final String CLASS_COLOR_CHOOSER_PANE = "color_chooser_pane";
    static final String CLASS_TAG_BUTTON = "tag_button";
    static final String CLASS_MAX_PANE = "max_pane";
    static final String CLASS_BORDERED_PANE = "bordered_pane";
    static final String CLASS_HEADING_LABEL = "heading_label";
    static final String CLASS_SUB_HEADING_LABEL = "subheading_label";
    static final String CLASS_PROMPT_LABEL = "prompt_label";
    static final String CLASS_PROMPT_TEXT_FIELD = "prompt_text_field";
    static final String CLASS_RENDER_CANVAS = "render_canvas";
    static final String EMPTY_TEXT = "";
    static final int BUTTON_WIDTH = 75;

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    
    PoseMakerController poseMakerController;
    
    

    
    //Left side of gui.
    VBox leftPane;
    
    HBox topBtns;
    Button selectionBtn;
    Button removeBtn;
    Button rectangleBtn;
    Button ellipseBtn;
    
    HBox movePane;
    Button moveUpBtn;
    Button moveDownBtn;
    
    HBox backgroundColor;
    Label backgroundColorLabel; 
    Button backgroundColorBtn;
    ColorPicker backgroundColorPicker;
    
    HBox fillColor;
    Label fillColorLabel; 
    Button fillColorBtn;
    
    HBox outlineColor;
    Label ooutlineColorLabel; 
    Button outlineColorBtn;
    ColorPicker outlineColorPicker;
    
    HBox outlineThickness;
    Label outlineThicknessLabel; 
    Slider slider;
    
    HBox snapshotPane;
    Button snapshotBtn;
    
    Canvas canvas;
    
    SplitPane workspaceSplitPane;
    
    Shape selectedRect;
    Shape selectedEllipse;
    ArrayList shapeList;
    
    boolean isDrawingRect;
    boolean isDrawingEllipse;
    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public Workspace(AppTemplate initApp) throws IOException {
	// KEEP THIS FOR LATER
	app = initApp;

        workspace = new HBox();
        
        leftPane = new VBox();
        
        selectedRect = new Rectangle();
        selectedEllipse = new Ellipse();
        shapeList = new ArrayList<>(); 
        
        
	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
        
        PropertiesManager propsSingleton = PropertiesManager.getPropertiesManager();
        
        poseMakerController = new PoseMakerController((PoseMaker) app);
        
        removeBtn = gui.initChildButton(topBtns, PropertyType.REMOVE_ICON.toString(), PropertyType.REMOVE_TOOLTIP.toString(), true);
        removeBtn.setMaxWidth(BUTTON_WIDTH);
	removeBtn.setMinWidth(BUTTON_WIDTH);
	removeBtn.setPrefWidth(BUTTON_WIDTH);
        removeBtn.setAlignment(Pos.CENTER);
        selectionBtn = gui.initChildButton(topBtns, PropertyType.SELECTION_TOOL_ICON.toString(), PropertyType.SELECTION_TOOL_TOOLTIP.toString(), true);
        selectionBtn.setMaxWidth(BUTTON_WIDTH);
	selectionBtn.setMinWidth(BUTTON_WIDTH);
	selectionBtn.setPrefWidth(BUTTON_WIDTH);
        selectionBtn.setAlignment(Pos.CENTER);
        rectangleBtn = gui.initChildButton(topBtns, PropertyType.RECT_ICON.toString(), PropertyType.RECT_TOOLTIP.toString(), true);
        rectangleBtn.setMaxWidth(BUTTON_WIDTH);
	rectangleBtn.setMinWidth(BUTTON_WIDTH);
	rectangleBtn.setPrefWidth(BUTTON_WIDTH);
        rectangleBtn.setAlignment(Pos.CENTER);
        ellipseBtn = gui.initChildButton(topBtns, PropertyType.ELLIPSE_ICON.toString(), PropertyType.ELLIPSE_TOOLTIP.toString(), true);
        ellipseBtn.setMaxWidth(BUTTON_WIDTH);
	ellipseBtn.setMinWidth(BUTTON_WIDTH);
	ellipseBtn.setPrefWidth(BUTTON_WIDTH);
        ellipseBtn.setAlignment(Pos.CENTER);
        topBtns.getChildren().addAll(selectionBtn, ellipseBtn, rectangleBtn, removeBtn);
        topBtns.setAlignment(Pos.CENTER);
        
        moveUpBtn = gui.initChildButton(movePane, PropertyType.REMOVE_ICON.toString(), PropertyType.REMOVE_TOOLTIP.toString(), true);
        moveUpBtn.setMaxWidth(BUTTON_WIDTH);
	moveUpBtn.setMinWidth(BUTTON_WIDTH);
	moveUpBtn.setPrefWidth(BUTTON_WIDTH);
        moveUpBtn.setAlignment(Pos.CENTER);
        moveDownBtn = gui.initChildButton(movePane, PropertyType.REMOVE_ICON.toString(), PropertyType.REMOVE_TOOLTIP.toString(), true);
        moveDownBtn.setMaxWidth(BUTTON_WIDTH);
	moveDownBtn.setMinWidth(BUTTON_WIDTH);
	moveDownBtn.setPrefWidth(BUTTON_WIDTH);
        moveDownBtn.setAlignment(Pos.CENTER);
        movePane.getChildren().addAll(moveUpBtn, moveDownBtn);
        movePane.setAlignment(Pos.CENTER);
        
        backgroundColorPicker = new ColorPicker();
        backgroundColor.getChildren().add(backgroundColorPicker);
        backgroundColor.setAlignment(Pos.CENTER);
        
        outlineColorPicker = new ColorPicker();
        outlineColor.getChildren().add(outlineColorPicker);
        outlineColor.setAlignment(Pos.CENTER);
        
        outlineThickness = new HBox();
        outlineThickness.getChildren().addAll(outlineThicknessLabel, slider);
        
        snapshotBtn = gui.initChildButton(snapshotPane, PropertyType.SNAPSHOT_ICON.toString(), PropertyType.SNAPSHOT_TOOLTIP.toString(), true);
        snapshotPane.getChildren().add(snapshotBtn);
        
        leftPane.getChildren().addAll(topBtns, movePane, backgroundColor, outlineColor, snapshotPane);
        
        workspaceSplitPane = new SplitPane();
	workspaceSplitPane.getItems().add(leftPane);
	workspaceSplitPane.getItems().add(canvas);
        
        workspace = new Pane();
        workspace.getChildren().add(workspaceSplitPane);
    }
    
    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    @Override
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
        topBtns.getStyleClass().add(CLASS_EDIT_TOOLBAR);
        backgroundColor.getStyleClass().add(CLASS_COLOR_CHOOSER_PANE);
        outlineColor.getStyleClass().add(CLASS_COLOR_CHOOSER_PANE);
        removeBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        selectionBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        rectangleBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        ellipseBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        moveUpBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        moveDownBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        leftPane.getStyleClass().addAll(CLASS_MAX_PANE, CLASS_BORDERED_PANE);
        canvas.getStyleClass().addAll(CLASS_RENDER_CANVAS, CLASS_BORDERED_PANE);
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {

    }
}
