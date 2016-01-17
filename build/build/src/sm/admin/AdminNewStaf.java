package sm.admin;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class AdminNewStaf {
	@FXML
	private Button createBtn;

	@FXML
	private MenuBar menu;
	@FXML
	private MenuItem close;
	@FXML
	private MenuItem about;

	@FXML
	private TextField username;
	@FXML
	private TextField password;

	/********************************
	 * COMMON METHODS
	 *******************************/
	@FXML
	private void back() throws Exception{
		Scene scene =  menu.getScene();
		Stage stage = (Stage) scene.getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminAccountManager.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}
	@FXML
	private void close(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		stage.setOnCloseRequest(e->{
			System.out.println("Are you sure?");
		});
		Platform.exit();
	}

	@FXML
	private void about(ActionEvent event) throws Exception{
		GlobalFunctions ob = new GlobalFunctions();
		ob.about();
	}


	/********************************
	 * CORE
	 *******************************/
	@FXML
	private void createStaf() throws Exception{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Input correctly.");
		alert.setHeaderText(null);

		if(username.getText().equals("")){
			alert.setContentText("Input username.");
			alert.showAndWait();

		}else if(password.getText().equals("")){
			alert.setContentText("Input password.");
			alert.showAndWait();
		}else{

			DatabaseConnection ob = new DatabaseConnection();
			// CHECK IF STAFF EXISTS
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("SELECT username FROM staf WHERE username='"+username.getText()+"'");
			if(rs.next()){
				alert.setContentText("Username Already Exist. Please try another one.");
				alert.showAndWait();
			}else{
				// SEND DATA TO DATABASE;
				rs.close();
				try {
					ob.puts("INSERT INTO staf (username,password) VALUES ('"+username.getText()+"','"+password.getText()+"');");
					ob.connect().close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				alert.setAlertType(AlertType.INFORMATION);
				alert.setTitle("Congratulation");
				alert.setContentText("Staff account created successfully!");
				alert.showAndWait();
				username.clear();
				password.clear();
			}


		}


	}







}
