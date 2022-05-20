package com.severo.todolist;

import com.severo.todolist.datamodel.TaskSingleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-window.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 800, 500);
		setUserAgentStylesheet(STYLESHEET_MODENA); // Cambiar tema aplicación
		stage.setTitle("Todolist");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void init() throws Exception {
		try {
			TaskSingleton.getInstance().loadTasks();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void stop() throws Exception {
		TaskSingleton.getInstance().saveTasks();
	}

	public static void main(String[] args) {
		launch();
	}
}