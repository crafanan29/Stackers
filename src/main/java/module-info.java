module camapps.stackers {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens camapps.stackers to javafx.fxml;
    exports camapps.stackers;
}