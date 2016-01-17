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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class AdminManageProduct implements Initializable{
	@FXML
	private MenuBar menu;

	@FXML
	private ComboBox<String> product1;
	@FXML
	private ComboBox<String> product2;

	@FXML
	private TextField quantity;
	@FXML
	private TextField price;

	@FXML
	private Button manageBtn1;
	@FXML
	private Button manageBtn2;

	private ObservableList<String> pname = FXCollections.observableArrayList();

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
	int proQuantity,proPrice;

	@FXML
	private void manageQuantity() throws Exception{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText(null);
		if(product1.getSelectionModel().getSelectedItem() == null || quantity.getText().equals("")){
			alert.setContentText("Check inputs please.");
			alert.showAndWait();
		}else{
			getData(product1.getValue().toString());
			int total = proQuantity + Integer.parseInt(quantity.getText());

			DatabaseConnection ob = new DatabaseConnection();
			ob.puts("UPDATE products SET product_storage='"+total+"' WHERE product_name='"+product1.getValue()+"';");
			ob.connect().close();

			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setContentText("Product quantity updated!");
			alert.showAndWait();
		}
	}

	@FXML
	private void managePrice() throws Exception{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText(null);
		if(product2.getSelectionModel().getSelectedItem() == null || price.getText().equals("")){
			alert.setContentText("Check inputs please.");
			alert.showAndWait();
		}else{
			getData(product2.getValue().toString());
			int total = Integer.parseInt(price.getText());

			DatabaseConnection ob = new DatabaseConnection();
			ob.puts("UPDATE products SET product_price='"+total+"' WHERE product_name='"+product2.getValue()+"';");
			ob.connect().close();

			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setContentText("Product price updated!");
			alert.showAndWait();
		}
	}

	private void getData(String pro){
		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("SELECT product_storage,product_price FROM products WHERE product_name='"+pro+"';");
			if(rs.next()){
				proQuantity = Integer.parseInt(rs.getString("product_storage"));
				proPrice = Integer.parseInt(rs.getString("product_price"));
			}
			rs.close();
			ob.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getProduct(){
		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("select product_name from products;");
			while(rs.next()){
				pname.add(rs.getString("product_name"));
			}
			rs.close();
			ob.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		product1.getItems().addAll(pname);
		product2.getItems().addAll(pname);
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getProduct();
	}
}
