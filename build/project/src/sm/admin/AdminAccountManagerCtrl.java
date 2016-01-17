package sm.admin;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import sm.defaults.GlobalFunctions;

public class AdminAccountManagerCtrl implements Initializable{

	@FXML
	private Button manageproduct;
	@FXML
	private MenuBar menu;

	/********************************
	 * COMMON FUNCTIONS
	 *******************************/

	@FXML
	private void back(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
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
	 * FATHER 8-)
	 *******************************/
	@FXML
	private void manageProduct() throws Exception{
		Stage stage = (Stage) manageproduct.getScene().getWindow();
		Scene scene = manageproduct.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminManageProduct.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void newProduct() throws Exception{
		Stage stage = (Stage) manageproduct.getScene().getWindow();
		Scene scene = manageproduct.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminNewProduct.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void editProduct() throws Exception{
		Stage stage = (Stage) manageproduct.getScene().getWindow();
		Scene scene = manageproduct.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminEditProduct.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void newCustomer() throws Exception{
		Stage stage = (Stage) manageproduct.getScene().getWindow();
		Scene scene = manageproduct.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminNewCustomer.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void editCustomer() throws Exception{
		Stage stage = (Stage) manageproduct.getScene().getWindow();
		Scene scene = manageproduct.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminEditCustomer.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void newStuff() throws Exception{
		Stage stage = (Stage) manageproduct.getScene().getWindow();
		Scene scene = manageproduct.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminNewStaf.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void editStaf() throws Exception{
		Stage stage = (Stage) manageproduct.getScene().getWindow();
		Scene scene = manageproduct.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminEditStaf.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
}
