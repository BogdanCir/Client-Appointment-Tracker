module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires transitive org.xerial.sqlitejdbc;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}