package sm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import sm.defaults.DatabaseConnection;

import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {

		/* *************************
		 * DATABASE CONFIGURATION;
		 * ************************/
		try {
			DatabaseConnection ob = new DatabaseConnection();
			if(!ob.hasTable()){
				ob.createTable();
				System.out.println("Database configured!");
			}
			ob.connect().close();
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/sm/com/LoginForm.fxml"));
			primaryStage.setScene(new Scene(loader.load()));
			primaryStage.setTitle("Please Login");
			primaryStage.setResizable(false);
			primaryStage.getIcons().add(new Image("/resource/img/ico.png"));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
