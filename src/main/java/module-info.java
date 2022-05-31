module com.example.ricochet_robot {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.ricochet_robot to javafx.fxml;
    exports com.example.ricochet_robot.backend;
    exports com.example.ricochet_robot;
}