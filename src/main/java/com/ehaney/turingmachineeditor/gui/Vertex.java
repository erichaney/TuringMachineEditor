package com.ehaney.turingmachineeditor.gui;

import com.ehaney.turingmachineeditor.model.State;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Vertex extends StackPane {
    private State state;
    private Circle icon;
    private Label idLabel;
    private Label actionLabel;

    private static Font actionFont = Font.font("Arial", FontWeight.BLACK, FontPosture.REGULAR, 20);
    private static Font idFont = Font.font("Arial", 12);
    private double startDragX, startDragY;

    public Vertex(State state, double x, double y) {
        this.state = state;
        icon = new Circle(25, Color.WHITE);
        icon.setStroke(Color.BLACK);
        icon.setStrokeWidth(2);
        idLabel = new Label(state.getID());
        idLabel.setFont(idFont);
        idLabel.setTextFill(Color.GREEN);
        actionLabel = new Label(state.getAction().toString());
        actionLabel.setFont(actionFont);
        //VBox labels = new VBox(idLabel, actionLabel);
        getChildren().addAll(icon, idLabel, actionLabel);
        setAlignment(idLabel, Pos.BOTTOM_CENTER);
        setAlignment(actionLabel, Pos.CENTER);
        setTranslateX(x);
        setTranslateY(y);
        makeDraggable();
    }

    private void makeDraggable() {
        setOnMouseEntered(mouseEvent -> setCursor(Cursor.HAND));
        setOnMouseExited(mouseEvent -> setCursor(Cursor.DEFAULT));

        setOnMousePressed(mouseEvent -> {
            setCursor(Cursor.CLOSED_HAND);
            startDragX = mouseEvent.getSceneX() - getLayoutX();
            startDragY = mouseEvent.getSceneY() - getLayoutY();
        });

        setOnMouseDragged(mouseEvent -> {
            setLayoutX(mouseEvent.getSceneX() - startDragX);
            setLayoutY(mouseEvent.getSceneY() - startDragY);
        });

        setOnMouseReleased(e -> setCursor(Cursor.OPEN_HAND));
    }

    public void highlight() {
        icon.setFill(Color.GOLD);
    }

    public void unhighlight() {
        icon.setFill(Color.WHITE);
    }
}
