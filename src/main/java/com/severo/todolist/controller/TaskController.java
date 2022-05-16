package com.severo.todolist.controller;

import com.severo.todolist.datamodel.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskController {
	private List<Task> tasks;

	@FXML
	private ListView<Task> mainListView;

	public void initialize() {
		Task t1 = new Task("Examen de programación", "Estudiar temas 6 y 7", LocalDate.of(2022, 6, 6));
		Task t2 = new Task("Excursión Aqualandia", "Salir y pasar un día en el parque acuático", LocalDate.of(2022, 6, 17));
		Task t3 = new Task("Sacar al perro", "Dar la vuelta al pueblo", LocalDate.of(2022, 6, 24));
		Task t4 = new Task("Ir al cine", "Ver Doctor Strange 2", LocalDate.of(2022, 7, 7));

		tasks = new ArrayList<>();
		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);
		tasks.add(t4);

		mainListView.getItems().addAll(tasks);
	}
}