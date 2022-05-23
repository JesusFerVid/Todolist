package com.severo.todolist.controller;

import com.severo.todolist.DialogController;
import com.severo.todolist.MainApplication;
import com.severo.todolist.datamodel.Task;
import com.severo.todolist.datamodel.TaskSingleton;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MainController {
	@FXML
	private ToggleButton todaysTaskButton;

	@FXML
	private TextArea detailsTextArea;

	@FXML
	private Label dateLabel;

	private ObservableList<Task> tasks;

	@FXML
	private ListView<Task> mainListView;

	@FXML
	private BorderPane mainBorderPane;

	@FXML
	private ContextMenu listViewContextMenu;

	private FilteredList<Task> taskFilteredList;

	public void initialize() {
		listViewContextMenu = new ContextMenu();
		MenuItem deleteMenuItem = new MenuItem("Delete");
		deleteMenuItem.setOnAction(event -> {
			Task t = mainListView.getSelectionModel().getSelectedItem();
			deleteTask(t);
		});
		listViewContextMenu.getItems().add(deleteMenuItem);

		MenuItem editMenuItem = new MenuItem("Edit");
		editMenuItem.setOnAction(event -> {
			Task t = mainListView.getSelectionModel().getSelectedItem();
			editTask(t);
		});
		listViewContextMenu.getItems().add(editMenuItem);

		taskFilteredList = new FilteredList<>(TaskSingleton.getInstance().getTasks(), t -> true);

		SortedList<Task> taskSortedList = new SortedList<>(taskFilteredList, new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				return t1.getExpirationDate().compareTo(t2.getExpirationDate());
			}
		});

		mainListView.setItems(taskSortedList);

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

		// Destacar en rojo las celdas que expiran hoy
		mainListView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
			@Override
			public ListCell<Task> call(ListView<Task> taskListView) {
				ListCell<Task> cell = new ListCell<>(){
					@Override
					protected void updateItem(Task task, boolean isEmpty) {
						super.updateItem(task, isEmpty);
						if (isEmpty) {
							setText(null);
						} else {
							setText(task.getDescription());
							if(task.getExpirationDate().equals(LocalDate.now())) {
								setTextFill(Color.RED);
							} else if (task.getExpirationDate().equals(LocalDate.now().plusDays(1))){
								setTextFill(Color.ORANGE);
							}
						}
					}
				};
				cell.emptyProperty().addListener((obs, notEmpty, wasEmpty) -> {
					if (wasEmpty) {
						cell.setContextMenu(null);
					} else {
						cell.setContextMenu(listViewContextMenu);
					}
				});
				return cell;
			}
		});
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
			mainListView.getSelectionModel().select(t);
		}
	}

	private void editTask(Task t) {
		// TODO
	}

	private void deleteTask(Task t) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deleting task");
		alert.setHeaderText("Delete a task");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> response = alert.showAndWait();
		if (response.isPresent() && response.get() == ButtonType.OK) {
			TaskSingleton.getInstance().deleteTask(t);
		}
	}

	@FXML
	public void todaysTaskButtonClicked() {
		Task sel = mainListView.getSelectionModel().getSelectedItem();
		if (todaysTaskButton.isSelected()) {
			taskFilteredList.setPredicate(t -> t.getExpirationDate().equals(LocalDate.now()));
			if (taskFilteredList.isEmpty()) {
				detailsTextArea.clear();
				dateLabel.setText("");
			} else {
				mainListView.getSelectionModel().selectFirst();
			}
		} else {
			taskFilteredList.setPredicate(t -> true);
			if (sel == null) {
				mainListView.getSelectionModel().selectFirst();
			} else {
				mainListView.getSelectionModel().select(sel);
			}

		}
	}

	@FXML
	public void onDeleteKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {
			Task t = mainListView.getSelectionModel().getSelectedItem();
			if (t != null) {
				deleteTask(t);
			}
		}
	}

	@FXML
	public void onExitClicked() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setResizable(true);
		alert.setTitle("Close application");
		alert.setHeaderText("Exit");
		alert.setContentText("Are you sure?");
		Optional<ButtonType> response = alert.showAndWait();
		if (response.isPresent() && response.get() == ButtonType.OK) {
			Platform.exit();
		}
	}
}