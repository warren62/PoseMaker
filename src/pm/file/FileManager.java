package pm.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import pm.data.DataManager;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class FileManager implements AppFileComponent {

    
    
    static final String JSON_SHAPES_ARRAY_NAME = "shapes";
    static final String JSON_RECTANGLE_NAME = "rectangle";
    static final String JSON_ELLIPSE_NAME = "ellipse";
    static final String JSON_SHAPE_ATTRIBUTES = "attributes";
    static final String JSON_TAG_ATTRIBUTE_NAME = "attribute_name";
    static final String JSON_TAG_ATTRIBUTE_VALUE = "attribute_value";
    static final String JSON_TAG_LEGAL_PARENTS = "legal_parents";
    static final String JSON_TAG_HAS_CLOSING_TAG = "has_closing_tag";
    static final String JSON_PANE_SHAPES = "pane_shapes";
    static final String JSON_TAG_NUMBER_OF_CHILDREN = "number_of_children";
    static final String JSON_TAG_NODE_INDEX = "node_index";
    static final String JSON_TAG_PARENT_INDEX = "parent_index";
    static final String JSON_CSS_CONTENT = "css_content";
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
    
    // THIS IS THE TEMP PAGE FOR OUR SITE
    public static final String INDEX_FILE = "index.html";
    public static final String CSS_FILE = "home.css";
    public static final String PATH_CSS = "./temp/css/";
    public static final String TEMP_CSS_PATH = PATH_CSS + CSS_FILE;
    public static final String PATH_TEMP = "./temp/";
    public static final String TEMP_PAGE = PATH_TEMP + INDEX_FILE;
    /**
     * This method is for saving user work, which in the case of this
     * application means the data that constitutes the page DOM.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {

        StringWriter sw = new StringWriter();
        
        DataManager dataManager = (DataManager)data;
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        Pane pane = dataManager.getPane();
        
        
    }
      
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {

    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * This method exports the contents of the data manager to a 
     * Web page including the html page, needed directories, and
     * the CSS file.
     * 
     * @param data The data management component.
     * 
     * @param filePath Path (including file name/extension) to where
     * to export the page to.
     * 
     * @throws IOException Thrown should there be an error writing
     * out data to the file.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {

    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// NOTE THAT THE Web Page Maker APPLICATION MAKES
	// NO USE OF THIS METHOD SINCE IT NEVER IMPORTS
	// EXPORTED WEB PAGES
    }
    
//    public JsonObject getShapeJson(ObservableList<Shape> shapeList) {
//        JsonArrayBuilder shapeFillJson = Json.createArrayBuilder();
//        for(Shape s : shapeList) {
//            
//        }
//        
//    }
    
    public String getJsonString(Shape[] shapeList) {
//        ObservableList<Node> shapeList = pane.getChildren();
        String jsonString = "";
        String listName = "\"shapes\"";
        String shape = "\"shape\"";
        String shapeType = "";
        String fields = "\"fields\"";
        String attributeName = "\"attribute_name\"";
        String attributeValue = "\"attribute_value\"";
        String fillColor = "\"fill color\"";
        String strokeColor = "\"stroke color\"";
        String strokeWidth = "\"stroke width\"";
        String fillColorValue = "";
        String strokeColorValue = "";
        String strokeWidthValue = "";
        for(Shape s : shapeList) {
            fillColorValue = s.getFill().toString();
            strokeColorValue = s.getStroke().toString();
            strokeWidthValue = Double.toString(s.getStrokeWidth());
            if(s instanceof Rectangle) {
                shapeType = "\"rectangle\"";
            }else if(s instanceof Ellipse) {
                shapeType = "\"ellipse\"";
            }
            jsonString = "{ \n"  + "\""+listName+"\"" + ": [\n" 
                    + "{" + "\""+shape+"\"" + ":" + "\""+shapeType +"\"" + ",\n"
                    + "\""+fields +"\"" + ": [" +"\n { \n" + "\""+ attributeName +"\"" + ":" + fillColor + ", \n"
                    + attributeValue + ":" + fillColorValue + "\n"
                    + " }, \n"
                    + " {\n"
                    + attributeName + ":" + strokeColor +", \n"
                    + attributeValue + ":" + strokeColorValue + "\n"
                    + " }, \n"
                    + " {\n"
                    + attributeName + ":" + strokeWidth +", \n"
                    + attributeValue + ":" + strokeWidthValue + "\n"
                    + " }, \n"
                    + " {\n"
                    + "\n }";
        }
        return jsonString;
    }
    
    
//    public void JsonIt(Pane pane) {
//        JsonObject obj = new JsonObject();
//    }
    
    public void fillArrayWithShapes(Pane root, JsonArrayBuilder arrayBuilder) {
        for(int i = 0; i < root.getChildren().size(); i++) {
            root.getChildren().get(i);
        }
        
    }
    
    private JsonArray buildJsonArray(ArrayList<String> data) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (String d : data) {
           jsb.add(d);
        }
        JsonArray jA = jsb.build();
        return jA;
    }
    
    private JsonArray makeAttributesJsonArray(HashMap<String,String> attributes) {
	Set<String> keys = attributes.keySet();
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	for (String attributeName : keys) {
	    String attributeValue = attributes.get(attributeName);
	    JsonObject jso = Json.createObjectBuilder()
                    
		.add(JSON_TAG_ATTRIBUTE_NAME, attributeName)
		.add(JSON_TAG_ATTRIBUTE_VALUE, attributeValue)
		.build();
	    arrayBuilder.add(jso);
	}
	JsonArray jA = arrayBuilder.build();
	return jA;
    }
    
    
    
}
