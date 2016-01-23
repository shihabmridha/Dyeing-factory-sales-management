package sm.com;

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
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import sm.defaults.GlobalFunctions;

public class HomeCtrl implements Initializable{
	@FXML
	private Button dailySales;

	@FXML
	private MenuBar menu;

	@FXML
	private MenuItem close;

	@FXML
	private MenuItem about;

	@FXML
	private Button accountManagerBtn;

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

	@FXML
	private void DailySales() throws Exception{
		Stage stage = (Stage) dailySales.getScene().getWindow();
		Scene scene = dailySales.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/sm/daily/DailySales.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	private void RegularCustomer(ActionEvent event) throws Exception{
		Stage stage = (Stage) dailySales.getScene().getWindow();
		Scene scene = dailySales.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/sm/customer/CustomerHome.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void AccountManager(ActionEvent event) throws Exception{
		Stage stage = (Stage) dailySales.getScene().getWindow();
		Scene scene = dailySales.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/sm/admin/AdminAccountManager.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void logout() throws Exception{
		Stage stage = (Stage) dailySales.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/sm/com/LoginForm.fxml"));
		stage.setScene(new Scene(loader.load()));
		stage.setResizable(false);
		GlobalFunctions.user = null;
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(!GlobalFunctions.user.equals("admin")){
			accountManagerBtn.setVisible(false);
		}

	}


}
