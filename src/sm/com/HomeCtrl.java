package sm.com;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import sm.defaults.GlobalFunctions;

public class HomeCtrl {
	@FXML
	private Button dailySales;

	@FXML
	private MenuBar menu;

	@FXML
	private MenuItem close;

	@FXML
	private MenuItem about;

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
	private void DailySales(ActionEvent event) throws Exception{
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
		System.out.println(loader.getLocation());
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


}
