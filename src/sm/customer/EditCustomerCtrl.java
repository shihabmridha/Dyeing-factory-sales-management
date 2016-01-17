package sm.customer;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class EditCustomerCtrl implements Initializable{

	@FXML
	private MenuBar menu;

	@FXML
	private TextField name;
	@FXML
	private TextField mobile;
	@FXML
	private ComboBox<String> address;

	@FXML
	private Button update;
	@FXML
	private Button delete;

	private ObservableList<String> addressList = FXCollections.observableArrayList();
	int theid;

	/* *********************
	 * COMMON METHODS
	 * ********************/
	@FXML
	private void back(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerData.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		CustomerDataCtrl ob = loader.<CustomerDataCtrl>getController();
		ob.setId(theid);
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



	/* *********************
	 * CORE METHODS
	 * ********************/
	public void setId(int id) throws Exception{
		theid = id;

		DatabaseConnection ob = new DatabaseConnection();
		ob.setQuery(ob.connect().createStatement());
		ResultSet rs = ob.getQuery().executeQuery("select * from customers where customer_id="+theid+";");
		if(rs.next()){
			name.setText(rs.getString("name"));
			mobile.setText(rs.getString("mobile"));
			address.setPromptText(rs.getString("address"));
		}
		rs.close();
		ob.connect().close();
	}

	@FXML
	private void updateInfo() throws Exception{

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Successful");
		alert.setContentText("Customer information updated successfully!");

		if(name.getText().equals("") || mobile.getText().equals("") || address.getValue() == null){
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("Please select inputs!");
			alert.showAndWait();
		}else{
			DatabaseConnection ob = new DatabaseConnection();
			ob.puts("UPDATE customers SET name='"+name.getText()+"',mobile='"+mobile.getText()+"',address='"+address.getValue()+"' WHERE customer_id="+theid+";");
			ob.connect().close();

			alert.showAndWait();
		}



	}

	@FXML
	private void deleteCustomer() throws Exception{
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(null);
		alert.setTitle("ALERT");
		alert.setContentText("Are you sure? You won't get this data back.");
		alert.showAndWait();
		DatabaseConnection ob = new DatabaseConnection();
		ob.puts("DELETE FROM customers WHERE customer_id="+theid+";");
		ob.connect().close();

		Stage stage = (Stage) delete.getScene().getWindow();
		Scene scene = delete.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerHome.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();

	}


	private void getAddress(){
		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("select address_name from address order by address_name asc;");
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		getAddress();
	}


}
