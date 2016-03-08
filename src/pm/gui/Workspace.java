package pm.gui;


import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import properties_manager.PropertiesManager;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    
    PropertiesManager propsSingleton = PropertiesManager.getPropertiesManager();

    
    //Left side of gui.
    VBox leftPane;
    
    HBox topBtns;
    Button selecttionBtn;
    Button removeBtn;
    Button rectangleBtn;
    Button ellipseBtn;
    
    HBox movePane;
    Button moveUpBtn;
    Button moveDownBtn;
    
    HBox backgroundColor;
    Label backgroundColorLabel; 
    Button backgroundColorBtn;
    
    HBox fillColor;
    Label fillColorLabel; 
    Button fillColorBtn;
    
    HBox outlineColor;
    Label ooutlineColorLabel; 
    Button outlineColorBtn;
    
    HBox outlineThickness;
    Label outlineThicknessLabel; 
    Slider slider;
    
    HBox snapshotPane;
    Button snapshotBtn;
    
    Shape selectedRect;
    Shape selectedEllipse;
    ArrayList shapeList;
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
        
        selecttionBtn = gui.initChildButton(topBtns, CLASS_FILE_BUTTON, CLASS_FILE_BUTTON, true);
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
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {

    }
}
