package sm.admin;

import java.sql.SQLException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class AdminNewProduct {

	@FXML
	private MenuBar menu;

	@FXML
	private TextField productName;
	@FXML
	private Button addProductBtn;

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
	private void addProduct() throws Exception{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		if(productName.getText().equals("")){
			alert.setTitle("Error");
			alert.setContentText("Input product name.");
			alert.showAndWait();
		}else{
			DatabaseConnection ob = new DatabaseConnection();
			try {
				ob.puts("INSERT INTO products (product_name) VALUES ('"+productName.getText()+"')");
				ob.connect().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setContentText("Product Added Successfully!");
			alert.showAndWait();
			productName.clear();
		}
	}


}
