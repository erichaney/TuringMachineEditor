package com.ehaney.turingmachineeditor;

import com.ehaney.turingmachineeditor.gui.Vertex;
import com.ehaney.turingmachineeditor.model.Machine;
import com.ehaney.turingmachineeditor.model.State;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TMEditorApp extends Application {
    Machine machine;
    @Override
    public void start(Stage stage) {
        machine = new Machine("[b] a a a b a", "0", State.Action.R);
        Vertex v = new Vertex(machine.getState("0"), 100, 100);
        Group group = new Group(v);
        Scene scene = new Scene(group, 320, 240, Color.LIGHTGRAY);
        stage.setTitle("Turing Machine Editor");
        stage.setScene(scene);
        stage.show();

        // test

        v.highlight();
        machine.addState("1", State.Action.H);
        Vertex v2 = new Vertex(machine.getState("1"), 150, 100);
        group.getChildren().add(v2);
    }

    public static void main(String[] args) {
        launch();
    }
}