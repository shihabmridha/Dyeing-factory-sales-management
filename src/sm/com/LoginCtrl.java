package sm.com;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class LoginCtrl implements Initializable{

	@FXML
	private Label msg;
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private Button loginBtn;
	@FXML
	private Button cancleBtn;


	public void keyListener(KeyEvent event) throws Exception{
	    if(event.getCode() == KeyCode.ENTER) {
	          login();
	     }
	}

	@FXML
	private void login() throws Exception{
		Stage stage = (Stage) loginBtn.getScene().getWindow();
		if(username.getText().equals("") || password.getText().equals("")){
			msg.setText("Empty field.");
			msg.setVisible(true);
		}else{
			msg.setVisible(false);
			DatabaseConnection ob = new DatabaseConnection();
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("SELECT username,password FROM staf WHERE username='"+username.getText()+"' and password='"+password.getText()+"';");
			if(rs.next()){
				GlobalFunctions.user = username.getText();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/sm/com/HomeActivity.fxml"));
				stage.setScene(new Scene(loader.load()));
				stage.setTitle("Jobbar Bostro Somvar");
				stage.getIcons().add(new Image("/resource/img/ico.png"));
				stage.setResizable(true);
				stage.show();
			}else{
				msg.setText("Login error!");
				msg.setVisible(true);
			}
			rs.close();
			ob.connect().close();
		}
	}

	@FXML
	private void cancle() throws Exception{
		System.out.println("Exit");
		Platform.exit();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		msg.setVisible(false);
	}


}
