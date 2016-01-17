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

public class AdminEditProduct implements Initializable{
	@FXML
	private MenuBar menu;

	@FXML
	private ComboBox<String> product;
	@FXML
	private TextField productName;
	@FXML
	private Button updateBtn;

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
	@FXML
	private void updateProduct() throws Exception{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText(null);

		if(productName.getText().equals("")){
			alert.setContentText("Input product name.");
			alert.showAndWait();
		}else{
			DatabaseConnection ob = new DatabaseConnection();
			try {
				ob.puts("UPDATE products SET product_name='"+productName.getText()+"' WHERE product_name='"+product.getValue()+"'");
				ob.connect().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setContentText("Product name updated successfully!");
			alert.showAndWait();
			product.getItems().clear();
			pname.clear();
			getProduct();
		}
	}

	@FXML
	private void deleteProduct(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete product");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure? You will not get this product back again!");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			DatabaseConnection ob = new DatabaseConnection();
			try {
				ob.puts("DELETE FROM products WHERE product_name='"+product.getValue()+"'");
				ob.connect().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			product.getItems().clear();
			pname.clear();
			getProduct();
		} else {
			System.out.println("Request Cancled!");
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
		product.getItems().addAll(pname);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getProduct();
	}


}
