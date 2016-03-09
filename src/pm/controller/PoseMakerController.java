/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pm.controller;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import pm.PoseMaker;

/**
 *
 * @author Steve
 */
public class PoseMakerController {

    PoseMaker app;
    double clickedX;
    double clickedY;
    double draggedX, draggedY;
    Group rectangleGroup = new Group();
    Rectangle rect;
    Ellipse ellipse;
    

    public PoseMakerController(PoseMaker initApp) {
        // KEEP IT FOR LATER
        app = initApp;
    }

    public void handleAddRectRequest(Pane pane) {

        pane.setCursor(Cursor.CROSSHAIR);
        
        pane.setOnMouseClicked(e -> {
            
            rect = new Rectangle(e.getX(), e.getY(), 0, 0);
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
    
    public void handleAddEllipseRequest(Pane pane) {

        pane.setCursor(Cursor.CROSSHAIR);
        
        pane.setOnMouseClicked(e -> {
            
            ellipse = new Ellipse(e.getX(), e.getY(), 0, 0);
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

    public void handleRemoveShapeRequest() {

    }

    public void handleSelectionShapeRequest() {

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

    public void handleCanvasClickedRequest(double clickedX, double clickedY) {

        this.clickedX = clickedX;
        this.clickedY = clickedY;
        
        
    }
    
     public void handleCanvasDraggedRequest(double draggedX, double draggedY) {

        this.draggedX = draggedX;
        this.draggedY = draggedY;
        paintRect();
        
        
    }
    
     public void handleCanvasClickReleasedRequest(double clickedX, double clickedY) {

       paintRect();
        
        
    }
    
    

    public void paintRect() {
        Rectangle rect = new Rectangle(clickedX, clickedY, draggedX - clickedX, draggedY - clickedY);
        rect.setFill(Color.RED);
        rectangleGroup.getChildren().add(rect);
    }
    
    public Group getRectGroup() {
        return rectangleGroup;
    }
}
