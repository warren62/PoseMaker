package pm.data;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import pm.controller.PoseMakerController;
import pm.file.FileManager;
import pm.gui.Workspace;
import saf.components.AppDataComponent;
import saf.AppTemplate;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */



public class DataManager implements AppDataComponent {
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    
    ArrayList<Shape> shapes;
    
    HashMap<String, Shape> hashShapes;
    
    Pane canvas;
    
    AppTemplate app;
    
    Workspace workspace;
    
    PoseMakerController controller;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;
        
        shapes = new ArrayList();
        hashShapes = new HashMap();
        
        FileManager fileManager = (FileManager) app.getFileComponent();
        
    }
    
    public ArrayList getShape() {
        return shapes;
    }
    
    public HashMap getHashShape() {
        return hashShapes;
    }
    
    public Pane getPane() {
        return canvas;
    }
    
    public void setPane(Pane canvas) {
        this.canvas = canvas;
    }

    public void setWorkspace(Workspace w) {
        workspace = w;
    }
    
    public Workspace getWorkspace() {
        return workspace;
    }
    
    public void setController(PoseMakerController c) {
        controller = c;
    }
    
    public PoseMakerController getController() {
        return controller;
    }
    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void reset() {

        getPane().getChildren().clear();
        
    }
}
