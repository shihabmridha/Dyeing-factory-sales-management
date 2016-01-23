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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import sm.defaults.AutoCompleteComboBoxListener;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class CustomerHomeCtrl implements Initializable{
	@FXML
	private MenuBar menu;

	@FXML
	private ComboBox<String> customerAddress;
	@FXML
	private ComboBox<String> customerName;

	@FXML
	private Button viewBtn;

	private ObservableList<String> addressList = FXCollections.observableArrayList();
	private ObservableList<String> nameList = FXCollections.observableArrayList();

	/********************************
	 * COMMON METHODS
	 *******************************/
	@FXML
	private void back() throws Exception{
		Scene scene =  menu.getScene();
		Stage stage = (Stage) scene.getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/sm/com/HomeActivity.fxml"));
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
	 * CORE METHODS
	 *******************************/
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
		customerAddress.getItems().addAll(addressList);
	}

	private void getName(){
		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("select name from customers where address='"+customerAddress.getValue()+"' order by name asc;");
			while(rs.next()){
				nameList.add(rs.getString("name"));
			}
			rs.close();
			ob.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		customerName.getItems().addAll(nameList);
	}

	@FXML
	private void processName(){
		customerName.getItems().clear();
		nameList.clear();
		viewBtn.setVisible(false);
		getName();
		customerName.setDisable(false);
	}

	int theId;
	@FXML
	private void selectName(){
		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("select customer_id from customers where address='"+customerAddress.getValue()+"' and name='"+customerName.getValue()+"';");
			if(rs.next()){
				theId = rs.getInt("customer_id");
			}
			rs.close();
			ob.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		viewBtn.setVisible(true);
	}

	@FXML
	private void showData() throws Exception{
		Scene scene = viewBtn.getScene();
		Stage stage = (Stage) scene.getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerData.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		CustomerDataCtrl ob = loader.<CustomerDataCtrl>getController();
		ob.setId(theId);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getAddress();
		customerName.setDisable(true);
		new AutoCompleteComboBoxListener<String>(customerName);
		viewBtn.setVisible(false);
	}

}
