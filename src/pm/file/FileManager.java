package pm.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
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
     * @param filePath Path (including file name/extension) to where to save the
     * data to.
     *
     * @throws IOException Thrown should there be an error writing out data to
     * the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {

        StringWriter sw = new StringWriter();

//        ArrayList<String> stringObjList = new ArrayList();
        DataManager dataManager = (DataManager) data;

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        Pane pane = dataManager.getPane();
        fillArrayWithShapes(pane, arrayBuilder);
        
        JsonArray nodesArray = arrayBuilder.build();

        String backgroundColor =  dataManager.getWorkspace().getBackgroundColor().toString().substring(2, 10);
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add("shape_list", nodesArray)
                .add("background_color", backgroundColor)
                .build();

        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath + ".json");
        pw.write(prettyPrinted);
        pw.close();

//        ObservableList<Node> list = pane.getChildren();
//        for(Node n : list) {
//            stringObjList.add();
//        }
    }

    public JsonObject makeRectangleObject(Rectangle r) {
        JsonObject jso = Json.createObjectBuilder()
                .add("shape", "rectangle")
                .add("x_coordinate",  "" + r.getX() + "")
                .add("y_coordinate", "" + r.getY() + "")
                .add("height", "" + r.getHeight() + "" )
                .add("width", "" + r.getWidth() + "")
                .add("stroke_width", "" + r.getStrokeWidth() + "")
                .add("stroke_fill", r.getStroke().toString())
                .add("fill", r.getFill().toString())
                .build();

        return jso;

    }

    public JsonObject makeEllipseObject(Ellipse e) {
        JsonObject jso = Json.createObjectBuilder()
                .add("shape", "ellipse")
                .add("x_coordinate", "" + e.getCenterX() + "")
                .add("y_coordinate", "" + e.getCenterY() + "")
                .add("radius_x", "" + e.getRadiusX() + "")
                .add("radius_y", "" + e.getRadiusY() + "")
                .add("stroke_width", "" + e.getStrokeWidth() + "")
                .add("stroke_fill", e.getStroke().toString())
                .add("fill", e.getFill().toString())
                .build();

        return jso;

    }
    
    public JsonObject makeBackgroundColorObject(Color color) {
        JsonObject jso = Json.createObjectBuilder()
                .add("color", color.toString())
                .build();
        return jso;
    }

    /**
     * This method loads data from a JSON formatted file into the data
     * management component and then forces the updating of the workspace such
     * that the user may edit the data.
     *
     * @param data Data management component where we'll load the file into.
     *
     * @param filePath Path (including file name/extension) to where to load the
     * data from.
     *
     * @throws IOException Thrown should there be an error reading in data from
     * the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {

        Pane newPane = new Pane();
        DataManager dataManager = (DataManager)data;
//	dataManager.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
	
	// LOAD THE TAG TREE
	JsonArray jsonShapeArray = json.getJsonArray("shape_list");
        JsonArray jsonColor = json.getJsonArray("color");
        
        
	for(int i = 0; i < jsonShapeArray.size(); i++) {
            JsonObject jsonShapeObjects = jsonShapeArray.getJsonObject(i);
            String shapeType = jsonShapeObjects.getString("shape");
            System.out.println(jsonShapeObjects);
            if(shapeType.equalsIgnoreCase("rectangle")) {
                Rectangle r = new Rectangle();
                System.out.println("test");
                String xCoord = jsonShapeObjects.getString("x_coordinate"); 
                 System.out.println("test");
                String yCoord = jsonShapeObjects.getString("y_coordinate");
                String height = jsonShapeObjects.getString("height");
                String width = jsonShapeObjects.getString("width");
                String strokeWidth = jsonShapeObjects.getString("stroke_width");
                String stroke = jsonShapeObjects.getString("stroke_fill").substring(2, 10);
                String fill = jsonShapeObjects.getString("fill").substring(2, 10);
                double xCoordDouble = Double.parseDouble(xCoord);
                double yCoordDouble = Double.parseDouble(yCoord);
                double heightDouble = Double.parseDouble(height);
                double widthDouble = Double.parseDouble(width);
                double strokeWidthDouble = Double.parseDouble(strokeWidth);
                r.setX(xCoordDouble);
                r.setY(yCoordDouble);
                r.setHeight(heightDouble);
                r.setWidth(widthDouble);
                r.setStrokeWidth(strokeWidthDouble);
                r.setStroke(Color.valueOf(stroke));
                r.setFill(Color.valueOf(fill));
//                Pane newPane = new Pane();
                 dataManager.getPane().getChildren().add(r);
//                dataManager.setPane(newPane);                
            }else if(shapeType.equalsIgnoreCase("ellipse")) {
                Ellipse e = new Ellipse();
                String xCoord = jsonShapeObjects.getString("x_coordinate");               
                String yCoord = jsonShapeObjects.getString("y_coordinate");
                String radiusX = jsonShapeObjects.getString("radius_x");
                String radiusY = jsonShapeObjects.getString("radius_y");
                String strokeWidth = jsonShapeObjects.getString("stroke_width");
                String stroke = jsonShapeObjects.getString("stroke_fill").substring(2, 10);
                String fill = jsonShapeObjects.getString("fill").substring(2, 10);
                double xCoordDouble = Double.parseDouble(xCoord);
                double yCoordDouble = Double.parseDouble(yCoord);
                double radiusXDouble = Double.parseDouble(radiusX);
                double radiusYDouble = Double.parseDouble(radiusY);
                double strokeWidthDouble = Double.parseDouble(strokeWidth);
                e.setCenterX(xCoordDouble);
                e.setCenterY(yCoordDouble);
                e.setRadiusX(radiusXDouble);
                e.setRadiusY(radiusYDouble);
                e.setStrokeWidth(strokeWidthDouble);
                e.setStroke(Color.valueOf(stroke));
                e.setFill(Color.valueOf(fill));
//                Pane newPane = new Pane();

                
                dataManager.getPane().getChildren().add(e);
//                dataManager.setPane(newPane);                
            }
        }
//        dataManager.setPane(newPane);
        
	
	
        dataManager.getWorkspace().reloadWorkspace();
        
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
     * This method exports the contents of the data manager to a Web page
     * including the html page, needed directories, and the CSS file.
     *
     * @param data The data management component.
     *
     * @param filePath Path (including file name/extension) to where to export
     * the page to.
     *
     * @throws IOException Thrown should there be an error writing out data to
     * the file.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {

    }

    /**
     * This method is provided to satisfy the compiler, but it is not used by
     * this application.
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
    

//    public void JsonIt(Pane pane) {
//        JsonObject obj = new JsonObject();
//    }
    public void fillArrayWithShapes(Pane root, JsonArrayBuilder arrayBuilder) {
        JsonObject jsonObject = null;
        for (int i = 0; i < root.getChildren().size(); i++) {
            if (root.getChildren().get(i) instanceof Rectangle) {
                
                Rectangle r = (Rectangle) root.getChildren().get(i);
                jsonObject = makeRectangleObject(r);
            } else if (root.getChildren().get(i) instanceof Ellipse) {
                Ellipse e = (Ellipse) root.getChildren().get(i);
                jsonObject = makeEllipseObject(e);
            }
            arrayBuilder.add(jsonObject);
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
    
    

//    private JsonArray makeAttributesJsonArray(HashMap<String, String> attributes) {
//        Set<String> keys = attributes.keySet();
//        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
//        for (String attributeName : keys) {
//            String attributeValue = attributes.get(attributeName);
//            JsonObject jso = Json.createObjectBuilder()
//                    .add(JSON_TAG_ATTRIBUTE_NAME, attributeName)
//                    .add(JSON_TAG_ATTRIBUTE_VALUE, attributeValue)
//                    .build();
//            arrayBuilder.add(jso);
//        }
//        JsonArray jA = arrayBuilder.build();
//        return jA;
//    }
}
