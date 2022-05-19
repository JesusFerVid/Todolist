package com.severo.todolist;

import com.severo.todolist.datamodel.Task;
import com.severo.todolist.datamodel.TaskSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DialogController {
	@FXML
	private TextField description;

	@FXML
	private TextArea details;

	@FXML
	private DatePicker date;

	public Task onOKButtonClick() {
		Task t = new Task(description.getText(), details.getText(), date.getValue());
		TaskSingleton.getInstance().addTask(t);
		return t;
	}
}
