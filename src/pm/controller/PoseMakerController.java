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
    Rectangle selectedRect = new Rectangle();
    Ellipse ellipse;
    Ellipse selectedEllipse;
    String rectId;
    String ellipseId;
    boolean selectable = true;

    private static int count = 0;

    public PoseMakerController(PoseMaker initApp) {
        // KEEP IT FOR LATER
        app = initApp;
    }

    public void handleAddRectRequest(Pane pane, ColorPicker outlinePicker, ColorPicker fillPicker, Slider slider) {

        pane.setCursor(Cursor.CROSSHAIR);

        pane.setOnMousePressed(e -> {
            if (e == null) {
                return;
            }
            //System.out.println(e.getX() + ", "+ e.getY());

//            rect = new Rectangle(e.getX(), e.getY(), 0, 0);   
            rect = paintRect(e.getX(), e.getY(), .1, .1, slider.getValue(), outlinePicker.getValue(), fillPicker.getValue());
            pane.getChildren().add(rect);

            pane.setOnMouseDragged(f -> {
                if (rect != null) {

                    rect.setWidth((f.getX() - rect.getX()));
                    rect.setHeight((f.getY() - rect.getY()));

                }

            });

        });

    }

    public void handleAddEllipseRequest(Pane pane, boolean selectable, ColorPicker outlinePicker, ColorPicker fillPicker, Slider slider) {

        pane.setCursor(Cursor.CROSSHAIR);

        setSelectable(selectable);
        pane.setOnMousePressed(e -> {
            if (e == null) {
                return;
            }

//            ellipse = new Ellipse(e.getX(), e.getY(), 0, 0);
            ellipse = paintEllipse(e.getX(), e.getY(), 0, 0, slider.getValue(), outlinePicker.getValue(), fillPicker.getValue());
//            ellipse.setCenterX(e.getX());
//            ellipse.setCenterY(e.getY());
            pane.getChildren().add(ellipse);

            pane.setOnMouseDragged(f -> {

                if (ellipse != null) {
                    ellipse.setRadiusX(e.getX() - ellipse.getRadiusX());
                    ellipse.setRadiusY(e.getY() - ellipse.getRadiusY());
                }
            });
        });

    }

    public void handleRemoveShapeRequest(Pane pane) {

        ObservableList<Node> list = pane.getChildren();
        for (Node node : list) {
//            if(node.getId().equals(rectId) || node.getId().equals(ellipseId) 
//                    && getSelectedRect().getId().equals(rectId) || 
//                    getSelectedEllipse().getId().equals(ellipseId)) {
//                list.remove(node);
//            }

            if (node.equals(selectedRect) || node.equals(selectedEllipse)) {
                list.remove(node);
                setSelectable(true);
            }

        }

    }

    public void handleSelectionShapeRequest() {

        Shape shape;

    }

    public void handleMoveShapeRequest() {

    }

    public void handleSnapshotRequest() {

    }

    public void handleCanvasClickedRequest(Pane pane, boolean selectable, ColorPicker outlinePicker, ColorPicker fillPicker, Slider slider) {

        pane.setOnMousePressed(e -> {

            pane.setCursor(Cursor.DEFAULT);
            Rectangle r = getSelectedRect();
            deSelect(r, outlinePicker, fillPicker, slider);
            setSelectable(selectable);

        });

    }
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

//         r.setOnMousePressed(e -> {
//                r.setStroke(Color.GREEN);
//                r.setStrokeWidth(sliderValue);
//                r.setStroke(Color.YELLOW);
//                r.setStrokeWidth(10);
//                setSelectedRect(r);
////                setSelectable(false);
//
//            });
//        if (selectable) {
//            r.setOnMousePressed(e -> {
//                r.setStroke(Color.YELLOW);
//                r.setStrokeWidth(10);
//                selectedRect = r;
//
//            });
//        }
        select(r, sliderValue, borderColor, fillColor);
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

    public void select(Rectangle r, double sliderValue, Color borderColor, Color fillColor) {

//        if (selectable) {
        r.setOnMousePressed(e -> {
            if (selectedRect != null) {
                selectedRect.setStroke(borderColor);
//                getSelectedRect().setStrokeWidth(sliderValue);
                r.setStroke(Color.YELLOW);
                r.setStrokeWidth(10);
                setSelectedRect(r);
//                setSelectable(false);
            }

        });
//        }

    }

    public void deSelect(Rectangle r, ColorPicker outlinePicker, ColorPicker fillPicker, Slider slider) {
        r = getSelectedRect();
        r.setFill(fillPicker.getValue());
        r.setStroke(outlinePicker.getValue());
        r.setStrokeWidth(slider.getValue());
//        setSelectedRect(r);
    }

    /**
     *
     * @param selectable
     */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public void setSelectedRect(Rectangle selectedR) {
//        if(selectable)
        selectedRect = selectedR;
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
