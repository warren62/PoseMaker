package pm.gui;

import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import pm.PoseMaker;
import pm.PropertyType;
import pm.controller.PoseMakerController;
import pm.data.DataManager;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;
import saf.ui.AppGUI;

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

    VBox backgroundColor;
    Label backgroundColorLabel;
    Button backgroundColorBtn;
    ColorPicker backgroundColorPicker;

    VBox fillColor;
    Label fillColorLabel;
    Button fillColorBtn;
    ColorPicker fillColorPicker;

    VBox outlineColor;
    Label outlineColorLabel;
    Button outlineColorBtn;
    ColorPicker outlineColorPicker;

    VBox outlineThickness;
    Label outlineThicknessLabel;
    Slider slider;

    HBox snapshotPane;
    Button snapshotBtn;

    Pane canvas;

    BorderPane workspaceSplitPane;

    Shape selectedRect;
    Shape selectedEllipse;
    ArrayList shapeList;

    double clickedX;
    double clickedY;

    boolean isDrawingShape;
    boolean selection;
    boolean deletion;
    boolean moveable;
//    boolean isDrawingEllipse;

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
        workspace = new Pane();
//

////        workspace = new HBox();
//        
//        leftPane = new VBox();
//        
//        selectedRect = new Rectangle();
//        selectedEllipse = new Ellipse();
//        shapeList = new ArrayList<>(); 
//        
//        
//	// KEEP THE GUI FOR LATER
        gui = app.getGUI();
//        
        PropertiesManager propsSingleton = PropertiesManager.getPropertiesManager();
//        
        poseMakerController = new PoseMakerController((PoseMaker) app);

        DataManager dataManager = (DataManager) app.getDataComponent();
        dataManager.setWorkspace(this);

        topBtns = new HBox();
        movePane = new HBox();
        backgroundColor = new VBox();
        outlineColor = new VBox();
        snapshotPane = new HBox();
//        canvas = new Pane();
        leftPane = new VBox();
        outlineThickness = new VBox();
        fillColor = new VBox();
        workspaceSplitPane = new BorderPane();
        slider = new Slider();
        outlineThicknessLabel = new Label("Outline Thickness");
        outlineColorLabel = new Label("Outline Color");
        backgroundColorLabel = new Label("Background Color");
        fillColorLabel = new Label("Fill Color");

        if (dataManager.getPane() != null) {
            canvas = dataManager.getPane();
        } else {
            canvas = new Pane();
        }

        removeBtn = gui.initChildButton(topBtns, PropertyType.REMOVE_ICON.toString(), PropertyType.REMOVE_TOOLTIP.toString(), false);
        removeBtn.setOnAction(e -> {
            poseMakerController.handleRemoveShapeRequest(canvas);
        });
        selectionBtn = gui.initChildButton(topBtns, PropertyType.SELECTION_TOOL_ICON.toString(), PropertyType.SELECTION_TOOL_TOOLTIP.toString(), false);

        selectionBtn.setOnAction(e -> {
            removeBtn.setDisable(false);
            moveUpBtn.setDisable(false);
            moveDownBtn.setDisable(false);
            poseMakerController.handleSelectionShapeRequest(canvas, outlineColorPicker.getValue(), fillColorPicker.getValue(), slider.getValue());
//            isDrawingShape = false;
        });
        rectangleBtn = gui.initChildButton(topBtns, PropertyType.RECT_ICON.toString(), PropertyType.RECT_TOOLTIP.toString(), false);
        rectangleBtn.setOnAction(e -> {
//            isDrawingShape = true;
            removeBtn.setDisable(true);
            moveUpBtn.setDisable(true);
            moveDownBtn.setDisable(true);
            poseMakerController.handleAddRectRequest(canvas, outlineColorPicker, fillColorPicker, slider);
        });
        ellipseBtn = gui.initChildButton(topBtns, PropertyType.ELLIPSE_ICON.toString(), PropertyType.ELLIPSE_TOOLTIP.toString(), false);
        ellipseBtn.setOnAction(e -> {
            poseMakerController.handleAddEllipseRequest(canvas, true, outlineColorPicker, fillColorPicker, slider);
//            isDrawingShape = true;
            removeBtn.setDisable(true);
            moveUpBtn.setDisable(true);
            moveDownBtn.setDisable(true);

        });
//        topBtns.getChildren().addAll(selectionBtn, ellipseBtn, rectangleBtn, removeBtn);
//        topBtns.setAlignment(Pos.CENTER);
//        
        moveUpBtn = gui.initChildButton(movePane, PropertyType.MOVE_UP_ICON.toString(), PropertyType.MOVE_UP_TOOLTIP.toString(), false);
        moveUpBtn.setOnAction(e -> {
            poseMakerController.handleMoveShapeUpRequest(canvas);
        });
        moveDownBtn = gui.initChildButton(movePane, PropertyType.MOVE_DOWN_ICON.toString(), PropertyType.MOVE_DOWN_TOOLTIP.toString(), false);
        moveDownBtn.setOnAction(e -> {
            poseMakerController.handleMoveShapeDownRequest(canvas);
        });

        movePane.setAlignment(Pos.CENTER);

//        
        backgroundColorPicker = new ColorPicker();
        backgroundColorPicker.setOnAction(e -> {
            poseMakerController.handleBackgroundColorRequset(canvas, backgroundColorPicker);
        });
        backgroundColor.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);
        backgroundColor.setAlignment(Pos.CENTER);

        outlineColorPicker = new ColorPicker();
        outlineColor.getChildren().addAll(outlineColorLabel, outlineColorPicker);
        outlineColorPicker.setOnAction(e -> {
            poseMakerController.handleColorChange(fillColorPicker.getValue(), outlineColorPicker.getValue(), slider.getValue());
        });
        outlineColor.setAlignment(Pos.CENTER);

        fillColorPicker = new ColorPicker();
        fillColor.getChildren().addAll(fillColorLabel, fillColorPicker);
        fillColorPicker.setOnAction(e -> {
            poseMakerController.handleColorChange(fillColorPicker.getValue(), outlineColorPicker.getValue(), slider.getValue());
        });
        fillColor.setAlignment(Pos.CENTER);

        slider.setOnMousePressed(e -> {
            poseMakerController.handleColorChange(fillColorPicker.getValue(), outlineColorPicker.getValue(), slider.getValue());

        });
        outlineThickness.getChildren().addAll(outlineThicknessLabel, slider);

        snapshotBtn = gui.initChildButton(snapshotPane, PropertyType.SNAPSHOT_ICON.toString(), PropertyType.SNAPSHOT_TOOLTIP.toString(), false);
        snapshotBtn.setOnAction(e -> {
            poseMakerController.handleSnapshotRequest(canvas);
        });

//        canvas.setOnMousePressed(e -> {
//            poseMakerController.handleCanvasClickedRequest(canvas, true, outlineColorPicker, fillColorPicker, slider);
//        });
//        canvas.setOnMouseReleased(e -> {
//            poseMakerController.handleCanvasClickReleasedRequest(clickedX, clickedY);
//        });
//        canvas.setOnMouseDragged(e -> {
//            poseMakerController.handleCanvasDraggedRequest(clickedX, clickedY);
//        });
        leftPane.getChildren().addAll(topBtns, movePane, backgroundColor, outlineColor, fillColor, outlineThickness, snapshotPane);

        canvas.setPrefSize(1800, 1800);

//        canvas.getChildren().add(poseMakerController.getRectGroup());
//        canvas.setMinWidth(10000);
//        canvas.minHeight(10000);
        workspaceSplitPane.setLeft(leftPane);
        workspaceSplitPane.setRight(canvas);

//
//        gui.getAppPane().setLeft(leftPane);
//        gui.getAppPane().setRight(canvas);
        workspace.getChildren().add(workspaceSplitPane);
//        
        workspaceActivated = false;

        dataManager.setPane(canvas);
        dataManager.setController(poseMakerController);

        
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
        topBtns.getStyleClass().add(CLASS_BORDERED_PANE);
        movePane.getStyleClass().add(CLASS_BORDERED_PANE);
        backgroundColor.getStyleClass().add(CLASS_BORDERED_PANE);
        outlineColor.getStyleClass().add(CLASS_BORDERED_PANE);
        outlineThickness.getStyleClass().add(CLASS_BORDERED_PANE);
        fillColor.getStyleClass().add(CLASS_BORDERED_PANE);
        snapshotPane.getStyleClass().add(CLASS_BORDERED_PANE);
        removeBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        selectionBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        rectangleBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        ellipseBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        moveUpBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        moveDownBtn.getStyleClass().add(CLASS_TAG_BUTTON);
        leftPane.getStyleClass().add(CLASS_MAX_PANE);
        canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
        outlineThicknessLabel.getStyleClass().add(CLASS_HEADING_LABEL);
        outlineColorLabel.getStyleClass().add(CLASS_HEADING_LABEL);
        fillColorLabel.getStyleClass().add(CLASS_HEADING_LABEL);
        backgroundColorLabel.getStyleClass().add(CLASS_HEADING_LABEL);
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
        ObservableList<Node> list = canvas.getChildren();
        for(Node n : list) {
            n.setOnMousePressed(e -> {
                poseMakerController.selectShape((Shape) n, slider.getValue(), outlineColorPicker.getValue(), fillColorPicker.getValue());
            });
        }
    }

    public Color getBackgroundColor() {
        return backgroundColorPicker.getValue();
    }
}
