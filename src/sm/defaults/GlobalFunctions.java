package sm.defaults;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GlobalFunctions {
	public static String user;

	public void about() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText(null);
		alert.setContentText("Hi, thank you for your interest !\n\nDeveloper: Shihab Mridha\nE-mail: shihabmridha@gmail.com");
		alert.showAndWait();
	}

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
