/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pm.controller;

import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import pm.PoseMaker;

/**
 *
 * @author Steve
 */
public class PoseMakerController {

    PoseMaker app;
//    double clickedX;
//    double clickedY;
//    double draggedX, draggedY;
//    Group rectangleGroup = new Group();
    Rectangle rect;
    Rectangle selectedRect;
    Ellipse ellipse;
    Ellipse selectedEllipse;
    String rectId;
    String ellipseId;
    

    public PoseMakerController(PoseMaker initApp) {
        // KEEP IT FOR LATER
        app = initApp;
    }

    public void handleAddRectRequest(Pane pane, ColorPicker outlinePicker, ColorPicker fillPicker, Slider slider) {

        pane.setCursor(Cursor.CROSSHAIR);
        
        pane.setOnMouseClicked(e -> {
            
//            rect = new Rectangle(e.getX(), e.getY(), 0, 0);   
            rect = paintRect(e.getX(), e.getY(), 0, 0, slider.getValue(), outlinePicker.getValue(), fillPicker.getValue());
           
            pane.getChildren().add(rect);
        });
        pane.setOnMouseDragged(e -> {
            rect.setWidth(e.getX() - rect.getX());
            rect.setHeight(e.getY() - rect.getY());
        });
        pane.setOnMouseReleased(e -> {
            rect = null;
        });
        
    }
    
    public void handleAddEllipseRequest(Pane pane, ColorPicker outlinePicker, ColorPicker fillPicker, Slider slider ) {

        pane.setCursor(Cursor.CROSSHAIR);
        
        pane.setOnMouseClicked(e -> {
            
//            ellipse = new Ellipse(e.getX(), e.getY(), 0, 0);
            ellipse = paintEllipse(e.getX(), e.getY(), 0, 0, slider.getValue(), outlinePicker.getValue(), fillPicker.getValue());
            pane.getChildren().add(ellipse);
        });
        pane.setOnMouseDragged(e -> {
            ellipse.setRadiusX(e.getX() - ellipse.getRadiusX());
            ellipse.setRadiusY(e.getY() - ellipse.getRadiusY());
        });
        pane.setOnMouseReleased(e -> {
            ellipse = null;
        });
        
    }

    public void handleRemoveShapeRequest(Pane pane) {

        ObservableList<Node> list = pane.getChildren();
        for(Node node : list) {
//            if(node.getId().equals(rectId) || node.getId().equals(ellipseId) 
//                    && getSelectedRect().getId().equals(rectId) || 
//                    getSelectedEllipse().getId().equals(ellipseId)) {
//                list.remove(node);
//            }

               if(node.equals(selectedRect) || node.equals(selectedEllipse)) {
                   list.remove(node);
               }
            
        }
        
        
    }

    public void handleSelectionShapeRequest() {

        Shape shape;
        
    }

    public void handleMoveShapeRequest() {

    }

    public void handleBackgroundColorChangeRequest() {

    }

    public void handleOutlineColorChangeRequest() {

    }

    public void handleOutlineThicknessChangeRequest() {

    }

    public void handleSnapshotRequest() {

    }

//    public void handleCanvasClickedRequest(double clickedX, double clickedY) {
//
//        this.clickedX = clickedX;
//        this.clickedY = clickedY;
//        
//        
//    }
//    
//     public void handleCanvasDraggedRequest(double draggedX, double draggedY) {
//
//        this.draggedX = draggedX;
//        this.draggedY = draggedY;
//        paintRect();
//        
//        
//    }
//    
//     public void handleCanvasClickReleasedRequest(double clickedX, double clickedY) {
//
//       paintRect();
//        
//        
//    }
//    
    

    public Rectangle paintRect(double clickedX, double clickedY, double draggedX, double draggedY, double sliderValue, Color borderColor, Color fillColor) {
       Rectangle r = new Rectangle(clickedX, clickedY, draggedX, draggedY);
       r.setFill(fillColor);
       r.setStroke(borderColor);
       r.setStrokeWidth(sliderValue);
       rectId = r.getId();
       r.setOnMousePressed(e -> {
           r.setStroke(Color.YELLOW);
           r.setStrokeWidth(10);
           selectedRect = r;
           
       });
       return r;
    }
    
    public Ellipse paintEllipse(double clickedX, double clickedY, double draggedX, double draggedY, double sliderValue, Color borderColor, Color fillColor) {
       Ellipse el = new Ellipse(clickedX, clickedY, draggedX, draggedY);
       el.setFill(fillColor);
       el.setStroke(borderColor);
       el.setStrokeWidth(sliderValue);
       ellipseId = el.getId();
       el.setOnMousePressed(e -> {
           el.setStroke(Color.YELLOW);
           el.setStrokeWidth(10);
           selectedEllipse = el;
       });
       return el;
    }
    
    public Rectangle getSelectedRect() {
        return selectedRect;
    }
    
     public Ellipse getSelectedEllipse() {
        return selectedEllipse;
    }
    
//    public Group getRectGroup() {
//        return rectangleGroup;
//    }
}
