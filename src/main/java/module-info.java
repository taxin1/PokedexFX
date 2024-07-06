module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.controlsfx.controls;
    requires mysql.connector.j;

    opens com.example.Lab08_1B_210041106 to javafx.fxml;
    exports com.example.Lab08_1B_210041106;
}