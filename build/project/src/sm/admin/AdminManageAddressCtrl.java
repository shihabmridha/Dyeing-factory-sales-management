package sm.admin;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class AdminManageAddressCtrl implements Initializable{
	@FXML
	private MenuBar menu;

	@FXML
	private ComboBox<String> address;

	@FXML
	private TextField name;
	@FXML
	private TextField newName;

	@FXML
	private Button add;
	@FXML
	private Button edit;

	private ObservableList<String> addressList = FXCollections.observableArrayList();

	/********************************
	 * COMMON METHODS
	 *******************************/
	@FXML
	private void back() throws Exception{
		Scene scene =  menu.getScene();
		Stage stage = (Stage) scene.getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminNewCustomer.fxml"));
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
	private void addAddress() throws Exception{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		if(name.getText().equals("")){
			alert.setContentText("Input name.");
			alert.showAndWait();
		}else{
			DatabaseConnection ob = new DatabaseConnection();
			ob.puts("INSERT INTO address (address_name) VALUES ('"+name.getText()+"')");
			ob.connect().close();
			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setContentText("New Address added successfully.");
			alert.showAndWait();
			address.getItems().clear();
			addressList.clear();
			getProduct();
		}
	}


	@FXML
	private void editAddress() throws Exception{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		if(address.getSelectionModel().getSelectedItem() == null || newName.equals("")){
			alert.setContentText("Input error. Please check.");
			alert.showAndWait();
		}else{
			DatabaseConnection ob = new DatabaseConnection();
			ob.puts("UPDATE address SET address_name='"+newName.getText()+"' WHERE address_name='"+address.getValue()+"'");
			ob.connect().close();
			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setContentText("Address updated successfully.");
			alert.showAndWait();
			address.getItems().clear();
			addressList.clear();
			getProduct();
		}
	}


	private void getProduct(){
		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("select address_name from address;");
			while(rs.next()){
				addressList.add(rs.getString("address_name"));
			}
			rs.close();
			ob.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		address.getItems().addAll(addressList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getProduct();
	}
}
