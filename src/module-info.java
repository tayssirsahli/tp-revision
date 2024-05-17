module TP_Revision1 {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	
	opens application to javafx.base,javafx.graphics, javafx.fxml;
}
