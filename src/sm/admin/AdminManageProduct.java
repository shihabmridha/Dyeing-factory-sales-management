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
	private TextField price1;
	@FXML
	private TextField price2;


	@FXML
	private Button currentStoreBtn;
	@FXML
	private Button currentPriceBtn;
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
	int proQuantity;
	double proPrice1,proPrice2;

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
		if(product2.getSelectionModel().getSelectedItem() == null || price1.getText().equals("") || price2.getText().equals("")){
			alert.setContentText("Check inputs please.");
			alert.showAndWait();
		}else{
			getData(product2.getValue().toString());

			DatabaseConnection ob = new DatabaseConnection();
			double pr1 = Double.parseDouble(price1.getText().toString());
			double pr2 = Double.parseDouble(price2.getText());
			System.out.println(price1.getText() + " : " + price2.getText());
			System.out.println("Pro1: " + pr1 + "\n" + "Pro2: " + pr2);
			ob.puts("UPDATE products SET product_price1='"+pr1+"',product_price2='"+pr2+"' WHERE product_name='"+product2.getValue()+"';");
			ob.connect().close();

			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setContentText("Product price updated!");
			alert.showAndWait();
		}
	}


	@FXML
	private void getCurrentStorage() throws Exception{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("STORAGE INFO");

		if(product1.getSelectionModel().getSelectedItem() == null){
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("Select product please.");
			alert.showAndWait();
		}else{
			getData(product1.getValue());
			alert.setContentText("You have " + proQuantity + " " + product1.getValue() + " in storage.");
			alert.showAndWait();
		}



	}

	@FXML
	private void getCurrentPrice() throws Exception{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("PRICE INFO");

		if(product2.getSelectionModel().getSelectedItem() == null){
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("Select product please.");
			alert.showAndWait();
		}else{
			getData(product2.getValue());
			alert.setContentText("Current price of " + product2.getValue() + " is:\n" + "Price 1: " + proPrice1 + "\nPrice 2: " + proPrice2 );
			alert.showAndWait();
		}
	}


	@FXML
	private void getData(){
		getData("");
	}


	private void getData(String pro){
		if(pro.equals("")){
			pro = product2.getValue();
		}

		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("SELECT product_storage,product_price1,product_price2 FROM products WHERE product_name='"+pro+"';");
			if(rs.next()){
				proQuantity = rs.getInt("product_storage");
				proPrice1 = rs.getDouble("product_price1");
				proPrice2 = rs.getDouble("product_price2");
				price1.setPromptText(Double.toString(proPrice1));
				price2.setPromptText(Double.toString(proPrice2));
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
