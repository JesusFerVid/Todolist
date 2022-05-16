module com.severo.todolist {
	requires javafx.controls;
	requires javafx.fxml;


	opens com.severo.todolist to javafx.fxml;
	exports com.severo.todolist;
	exports com.severo.todolist.controller;
	opens com.severo.todolist.controller to javafx.fxml;
}