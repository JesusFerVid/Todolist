package com.severo.todolist.datamodel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskSingleton {
	private static TaskSingleton instance = new TaskSingleton();
	private static String filename = "Tasks.txt";
	private List<Task> tasks;

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
				StringBuilder sb = new StringBuilder(task.getDescription());
				sb.append("\t");
				sb.append(task.getDetails());
				sb.append("\t");
				sb.append(task.getExpirationDate().toString());
				writer.write(sb.toString());
				writer.newLine();
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public void loadTasks() throws IOException {
		tasks = new ArrayList<>();
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

	public static TaskSingleton getInstance() {
		return instance;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}

class Test {
	public static void main(String[] args) {
		List<Task> tareas = TaskSingleton.getInstance().getTasks();
	}
}
