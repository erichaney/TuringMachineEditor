package com.ehaney.turingmachineeditor.gui;

import com.ehaney.turingmachineeditor.model.State;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.Arrays;

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

        // Configure circle
        icon = new Circle(25, Color.WHITE);
        icon.setStroke(Color.BLACK);
        icon.setStrokeWidth(2);

        // Configure labels
        idLabel = new Label();
        idLabel.textProperty().bind(state.idProperty());
        idLabel.setFont(idFont);
        idLabel.setTextFill(Color.GREEN);
        actionLabel = new Label();
        actionLabel.textProperty().bind(state.actionProperty().asString());
        actionLabel.setFont(actionFont);
        getChildren().addAll(icon, idLabel, actionLabel);
        setAlignment(idLabel, Pos.BOTTOM_CENTER);
        setAlignment(actionLabel, Pos.CENTER);

        // Position and initialize
        setTranslateX(x);
        setTranslateY(y);
        enableEdit();
        enableEditContextMenu();
    }

    public void highlight() {
        icon.setFill(Color.GOLD);
    }

    public void unhighlight() {
        icon.setFill(Color.WHITE);
    }

    public void select() {
        this.setFocused(true);
    }

    public void deselect() {
        this.setFocused(false);
    }

    public void enableEdit() {
        this.setCursor(Cursor.OPEN_HAND);
        actionLabel.setCursor(Cursor.HAND);

        enableEditContextMenu();
        enableDragAndDrop();
    }

    private void enableEditContextMenu() {

        MenuItem addTransition = new MenuItem("Add Transition");
        MenuItem editState = new MenuItem("Edit State");
        MenuItem editID = new MenuItem("Edit ID");
        MenuItem delete = new MenuItem("Delete...");
        delete.setDisable(true);

        ContextMenu editContextMenu = new ContextMenu(addTransition,
                editState, editID, delete);

        actionLabel.setOnMouseClicked(e -> {
            if (e.isStillSincePress())
                editContextMenu.show(actionLabel, Side.BOTTOM, 0, 0);
        });
    }

    private void enableDragAndDrop() {
        setOnMousePressed(mouseEvent -> {
            setCursor(Cursor.CLOSED_HAND);
            startDragX = mouseEvent.getSceneX() - getLayoutX();
            startDragY = mouseEvent.getSceneY() - getLayoutY();
        });

        setOnMouseDragged(mouseEvent -> {
            setLayoutX(mouseEvent.getSceneX() - startDragX);
            setLayoutY(mouseEvent.getSceneY() - startDragY);
        });

        setOnMouseReleased(mouseEvent -> setCursor(Cursor.OPEN_HAND));
    }

    public void enableActionContextMenu() {
        ContextMenu actionContextMenu = new ContextMenu();

        Arrays.stream(State.Action.values())
                .forEach(a -> {
                    MenuItem m = new MenuItem(a.toString());
                    m.setOnAction(e -> {
                        state.setAction(a);
                    });
                    actionContextMenu.getItems().add(m);
                });

        actionLabel.setContextMenu(actionContextMenu);
    }
}
