module com.ehaney.turingmachineeditor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ehaney.turingmachineeditor to javafx.fxml;
    exports com.ehaney.turingmachineeditor;
    exports com.ehaney.turingmachineeditor.model;
    opens com.ehaney.turingmachineeditor.model to javafx.fxml;
}