package sm.admin;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class AdminEditStaf implements Initializable{

	@FXML
	private MenuBar menu;

	@FXML
	private ComboBox<String> username;
	@FXML
	private TextField password;
	@FXML
	private Button updateBtn;

	private ObservableList<String> uname = FXCollections.observableArrayList();
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
	private void updateStaf() throws Exception{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Successful");
		alert.setHeaderText(null);
		alert.setContentText("Password changed successfully!");
		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.puts("UPDATE staf SET password='"+password.getText()+"' WHERE username='"+username.getValue()+"'");
			ob.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		alert.showAndWait();
	}

	@FXML
	private void deleteStaf(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Staf account");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure? You will not get this user back again!");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			if(username.getValue().equals("admin")){
				alert.setAlertType(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setContentText("Sorry! You can not delete Administrator.");
				alert.showAndWait();
			}else{
				DatabaseConnection ob = new DatabaseConnection();
				try {
					ob.puts("DELETE FROM staf WHERE username='"+username.getValue()+"'");
					ob.connect().close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				username.getItems().clear();
				uname.clear();
				getUsername();
			}
		} else {
			System.out.println("Request Cancled!");
		}
	}

	private void getUsername(){
		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("select username from staf;");
			while(rs.next()){
				uname.add(rs.getString("username"));
			}
			rs.close();
			ob.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		username.getItems().addAll(uname);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getUsername();
	}

}
