module com.example.ricochet_robot {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ricochet_robot to javafx.fxml;
    exports com.example.ricochet_robot;
    exports com.example.ricochet_robot.frontend;
    opens com.example.ricochet_robot.frontend to javafx.fxml;
}