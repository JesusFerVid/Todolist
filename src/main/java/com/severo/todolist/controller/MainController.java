package com.severo.todolist.controller;

import com.severo.todolist.DialogController;
import com.severo.todolist.MainApplication;
import com.severo.todolist.datamodel.Task;
import com.severo.todolist.datamodel.TaskSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Optional;

public class MainController {
	@FXML
	private TextArea detailsTextArea;

	@FXML
	private Label dateLabel;

	private List<Task> tasks;

	@FXML
	private ListView<Task> mainListView;

	@FXML
	private BorderPane mainBorderPane;

	public void initialize() {
		mainListView.getItems().setAll(TaskSingleton.getInstance().getTasks());

		// Simular un click
		mainListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
			@Override
			public void changed(ObservableValue<? extends Task> observableValue, Task oldTask, Task newTask) {
				if (newTask != null) {
					Task tarea = mainListView.getSelectionModel().getSelectedItem();
					detailsTextArea.setText(tarea.getDetails());
					dateLabel.setText(tarea.getExpirationDate()
						.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy")));
				}
			}
		});

		mainListView.getSelectionModel().selectFirst();
	}

	@FXML
	public void showNewTaskDialog() {
		Dialog<ButtonType> d = new Dialog<>();
		d.initOwner(mainBorderPane.getScene().getWindow());
		d.setResizable(true);
		d.setTitle("New task");
		FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("dialog.fxml"));
		try {
			d.getDialogPane().setContent(loader.load());
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}
		d.getDialogPane().getButtonTypes().add(ButtonType.OK);
		d.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		d.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

		Optional<ButtonType> response = d.showAndWait();

		// Se ha pulsado Aceptar
		if (response.isPresent() && response.get() == ButtonType.OK) {
			DialogController controller = loader.getController();
			Task t = controller.onOKButtonClick();
			mainListView.getItems().setAll(TaskSingleton.getInstance().getTasks());
			mainListView.getSelectionModel().select(t);
		}
	}
}