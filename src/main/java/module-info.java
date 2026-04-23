module jacks_test_fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
	requires transitive javafx.graphics;
	requires transitive javafx.base;
	requires transitive java.sql;
	requires com.stripe;
    opens jacks_test_fx.parking_gui to javafx.fxml;
    exports jacks_test_fx.parking_gui;
}