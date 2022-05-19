package com.severo.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;

public class TaskSingleton {
	private static TaskSingleton instance = new TaskSingleton();
	private static String filename = "Tasks.txt";
	private ObservableList<Task> tasks;

	private TaskSingleton() {

	}

	public void saveTasks() throws IOException {
		Path path = Paths.get(filename);
		BufferedWriter writer = Files.newBufferedWriter(path);
		try {
			Iterator<Task> it = tasks.iterator();
			Task task;
			while (it.hasNext()) {
				task = it.next();
				String linea = task.getDescription() +
					"\t" +
					task.getDetails() +
					"\t" +
					task.getExpirationDate().toString();
				writer.write(linea);
				writer.newLine();
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public void loadTasks() throws IOException {
		tasks = FXCollections.observableArrayList();
		Path path = Paths.get(filename);
		BufferedReader reader = Files.newBufferedReader(path);

		String line;
		try {
			line = reader.readLine();
			while (line != null) {
				String[] taskPiece = line.split("\t");
				tasks.add(new Task(taskPiece[0], taskPiece[1], LocalDate.parse(taskPiece[2])));
				line = reader.readLine();
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public void deleteTask(Task task) {
		tasks.remove(task);
	}

	public static TaskSingleton getInstance() {
		return instance;
	}

	public ObservableList<Task> getTasks() {
		return tasks;
	}
}

class Test {
	public static void main(String[] args) {
		ObservableList<Task> tareas = TaskSingleton.getInstance().getTasks();
	}
}
