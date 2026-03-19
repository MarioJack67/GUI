module jacks_test_fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
	requires javafx.graphics;
	requires javafx.base;
	requires transitive java.sql;
    opens jacks_test_fx.parking_gui to javafx.fxml;
    exports jacks_test_fx.parking_gui;
}