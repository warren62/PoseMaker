/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pm.controller;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import pm.PoseMaker;
import pm.file.FileManager;

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
    Shape selectedShape = new Ellipse();
    String rectId;
    String ellipseId;
    Color outlineColor, fillColor, originalColor, selectedRectColor;
    double sliderValue;
    boolean selectable = true;
    boolean selector;
    boolean isShape = false;

    private static int count = 0;

    public PoseMakerController(PoseMaker initApp) {
        // KEEP IT FOR LATER
        app = initApp;
    }

    public void handleAddRectRequest(Pane pane, ColorPicker outlinePicker, ColorPicker fillPicker, Slider slider) {

        pane.setCursor(Cursor.CROSSHAIR);
        selector = false;
        pane.setOnMousePressed(e -> {
            if (e == null) {
                return;
            }
            //System.out.println(e.getX() + ", "+ e.getY());

//            rect = new Rectangle(e.getX(), e.getY(), 0, 0);   
            rect = paintRect(e.getX(), e.getY(), .1, .1, slider.getValue(), outlinePicker.getValue(), fillPicker.getValue());
            pane.getChildren().add(rect);

            System.out.println(pane.getChildren().get(0));
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
        selector = false;
        pane.setOnMousePressed(e -> {
            if (e == null) {
                return;
            }
            //System.out.println(e.getX() + ", "+ e.getY());

//            rect = new Rectangle(e.getX(), e.getY(), 0, 0);   
            ellipse = paintEllipse(e.getX(), e.getY(), .1, .1, slider.getValue(), outlinePicker.getValue(), fillPicker.getValue());
//            ellipse = new Ellipse(e.getX(), e.getY(), 0, 0);
            pane.getChildren().add(ellipse);

            pane.setOnMouseDragged(f -> {
                if (ellipse != null) {

                    ellipse.setRadiusX(f.getX() - ellipse.getCenterX());
                    ellipse.setRadiusY((f.getY() - ellipse.getCenterY()));

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

            if (node.equals(selectedShape)) {
                list.remove(node);
            }

        }

    }

    public void handleSelectionShapeRequest(Pane pane, Color outlineColor, Color fillColor, double sliderValue) {

        pane.setCursor(Cursor.DEFAULT);
        selector = true;
        pane.setOnMouseClicked(e -> {
            if (selectedShape != null && !isShape) {
                deSelect(outlineColor);
            }
            isShape = false;
        });
        pane.setOnMousePressed(e -> {
//            deSelect(selectedRect, outlineColor, fillColor, sliderValue);

        });
        pane.setOnMouseDragged(e -> {

            if (selectedShape instanceof Rectangle) {
                Rectangle r = (Rectangle) selectedShape;
                r.setX(e.getX() - (r.getWidth() / 2));
                r.setY(e.getY() - (r.getHeight() / 2));
            } else if (selectedShape instanceof Ellipse) {
                Ellipse el = (Ellipse) selectedShape;
//               el.setRadiusX(e.getX());
                el.setCenterX(e.getX());
                el.setCenterY(e.getY());
            }
        });

    }

    public void handleMoveShapeUpRequest(Pane pane) {

        ObservableList<Node> list = pane.getChildren();
        ObservableList<Node> removeList = pane.getChildren();
        if (list.size() > 1) {
            for (Node node : list) {

                list.remove(selectedShape);
                list.add(selectedShape);

            }
            list.removeAll(removeList);
        }

    }

    public void handleMoveShapeDownRequest(Pane pane) {

        ObservableList<Node> list = pane.getChildren();
        if (list.size() > 1) {
            for (Node node : list) {

                list.remove(selectedShape);
                list.add(0, selectedShape);

            }
        }

    }

    public void handleBackgroundColorRequset(Pane pane, ColorPicker colorPicker) {
        String colorBackground = colorPicker.getValue().toString();
        pane.setStyle("-fx-background-color: #" + colorBackground.substring(2));
        
    }
    
    public void handleSnapshotRequest(Pane pane) {

        WritableImage image = pane.snapshot(new SnapshotParameters(), null);

        File fileWork = new File("./work/");
        if (!fileWork.exists()) {
            fileWork.mkdir();
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(fileWork);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG Files (.*png)", ".*png");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        String path = file.getPath() + ".png";
        System.out.println(path);
        file = new File(path);
//       File file = new File("pane.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException ex) {
            Logger.getLogger(PoseMakerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    public void handleCanvasClickedRequest(Pane pane, boolean selectable, ColorPicker outlinePicker, ColorPicker fillPicker, Slider slider) {
//
//        pane.setOnMousePressed(e -> {
//
//            pane.setCursor(Cursor.DEFAULT);
//            Rectangle r = getSelectedRect();
//            deSelect(r, outlinePicker, fillPicker, slider);
//            setSelectable(selectable);
//
//        });
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
        selectShape(r, sliderValue, borderColor, fillColor);
        return r;
    }

    public Ellipse paintEllipse(double clickedX, double clickedY, double draggedX, double draggedY, double sliderValue, Color borderColor, Color fillColor) {
        Ellipse el = new Ellipse(clickedX, clickedY, draggedX, draggedY);
        el.setFill(fillColor);
        el.setStroke(borderColor);
        el.setStrokeWidth(sliderValue);
        selectShape(el, sliderValue, borderColor, fillColor);
        return el;
    }

//    public void selectRect(Rectangle r, double sliderValue, Color borderColor, Color fillColor) {
//
////        if (selector) {
//        r.setOnMousePressed(e -> {
//            if (selector) {
//                if (selectedShape != null) {
//                    isShape = true;
//                    selectedShape.setStroke(borderColor);
//                    selectedShape.setStrokeWidth(sliderValue);
////                getSelectedRect().setStrokeWidth(sliderValue);
//                    r.setStroke(Color.YELLOW);
//                    r.setStrokeWidth(10);
//                    setSelectedShape(r);
////                setSelectable(false);
//                }
//            }
//
//        });
////        }
//    }
//    
//     public void selectEllipse(Ellipse el, double sliderValue, Color borderColor, Color fillColor) {
//
////        if (selector) {
//        el.setOnMousePressed(e -> {
//            if (selector) {
//                if (selectedShape != null) {
//                    isShape = true;
//                    selectedRect.setStroke(borderColor);
//                    selectedRect.setStrokeWidth(sliderValue);
////                getSelectedRect().setStrokeWidth(sliderValue);
//                    el.setStroke(Color.YELLOW);
//                    el.setStrokeWidth(10);
//                    setSelectedShape(el);
////                setSelectable(false);
//                }
//            }
//
//        });
////        }
//    }
    public void selectShape(Shape s, double sliderValue, Color borderColor, Color fillColor) {

//        if (selector) {

        s.setOnMousePressed(e -> {
            if (selector) {
                if (selectedShape != null) {
                    isShape = true;
                    selectedShape.setStroke(selectedRectColor);
                    selectedShape.setStrokeWidth(sliderValue);
//                getSelectedRect().setStrokeWidth(sliderValue);
                    selectedRectColor = (Color) s.getStroke();

                    s.setStroke(Color.YELLOW);
                    if(sliderValue < 10) {
                        s.setStrokeWidth(10);
                    }else {
                        s.setStrokeWidth(sliderValue);
                    }
//                    s.setStrokeWidth(10);
                    setSelectedShape(s);
//                setSelectable(false);
                }
            }

        });

//        }
    }

    public void deSelect(Color outlineColor) {
//        selector = false;
//        r = getSelectedRect();
//        r.setFill(fillColor);
//        selectedRect.setStroke(outlineColor);
        selectedShape.setStroke(outlineColor);
//        selectedRect = null;
//        r.setStrokeWidth(sliderValue);
//        setSelectedRect(r);
    }

    public void handleColorChange(Color fillColor, Color borderColor, double sliderValue) {

        selectedShape.setFill(fillColor);
        selectedShape.setStroke(borderColor);
        selectedShape.setStrokeWidth(sliderValue);
    }

//    public void loadShapeAddHandlers(Pane pane, Shape s, Color borderColor, Color strokeColor, double strokeWidth) {
//
//        s.setOnMousePressed(e -> {
//            if (selector) {
//                if (selectedShape != null) {
//                    isShape = true;
//                    selectedShape.setStroke(borderColor);
//                    selectedShape.setStrokeWidth(sliderValue);
////                getSelectedRect().setStrokeWidth(sliderValue);
//                    s.setStroke(Color.YELLOW);
//                    s.setStrokeWidth(10);
//                    setSelectedShape(s);
////                setSelectable(false);
//                }
//            }
//
//        });
//
//        pane.setCursor(Cursor.CROSSHAIR);
//        selector = false;
//        pane.setOnMousePressed(e -> {
//            if (e == null) {
//                return;
//            }
//            //System.out.println(e.getX() + ", "+ e.getY());
//
////            rect = new Rectangle(e.getX(), e.getY(), 0, 0);   
//            rect = paintRect(e.getX(), e.getY(), .1, .1, slider.getValue(), outlinePicker.getValue(), fillPicker.getValue());
//            pane.getChildren().add(rect);
//
//            System.out.println(pane.getChildren().get(0));
//            pane.setOnMouseDragged(f -> {
//                if (rect != null) {
//
//                    rect.setWidth((f.getX() - rect.getX()));
//                    rect.setHeight((f.getY() - rect.getY()));
//
//                }
//
//            });
//
//        });
//
//    }

    /**
     *
     * @param selectable
     */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public void setBorderColor(Color borderColor) {
        outlineColor = borderColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setSlider(double sliderValue) {
        this.sliderValue = sliderValue;
    }

    public void setSelectedRect(Rectangle selectedR) {
//        if(selectable)
        selectedRect = selectedR;
    }

    public void setSelectedEllipse(Ellipse selectedE) {
//        if(selectable)
        selectedEllipse = selectedE;
    }

    public void setSelectedShape(Shape selectedShape) {
//        if(selectable)
        this.selectedShape = selectedShape;
    }

    public Rectangle getSelectedRect() {
        return selectedRect;
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public Ellipse getSelectedEllipse() {
        return selectedEllipse;
    }

    public Color getBorderColor() {
        return outlineColor;
    }

    public Color getfillColor() {
        return fillColor;
    }

    public double getSliderValue() {
        return sliderValue;
    }

//    public Group getRectGroup() {
//        return rectangleGroup;
//    }
}
