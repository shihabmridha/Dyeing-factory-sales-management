package sm.daily;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import sm.defaults.DailySalesTableData;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class DailySalesCtrl implements Initializable{

	@FXML
	private MenuBar menu;
	@FXML
	private MenuItem back;
	@FXML
	private MenuItem close;
	@FXML
	private MenuItem about;

	@FXML
	private Button addTransactionBtn;
	@FXML
	private Button deleteTransactionBtn;

	@FXML
	private DatePicker datePicker;
	@FXML
	private DatePicker datePicker2;
	@FXML
	private ComboBox <String> producSelect;
	@FXML
	private TextField quantity;
	@FXML
	private TextField deposit;

	@FXML
	private CheckBox check;

	/*********************
	 * TABLE ITEMS
	 *********************/
	@FXML
	private TableView<DailySalesTableData> table;
	@FXML
	private TableColumn<DailySalesTableData, String> dateColumn;
	@FXML
	private TableColumn<DailySalesTableData, String> productColumn;
	@FXML
	private TableColumn<DailySalesTableData, Integer> quantityColumn;
	@FXML
	private TableColumn<DailySalesTableData, Integer> priceColumn;
	@FXML
	private TableColumn<DailySalesTableData, Integer> totalColumn;
	@FXML
	private TableColumn<DailySalesTableData, Integer> depositColumn;
	@FXML
	private TableColumn<DailySalesTableData, Integer> restColumn;
	@FXML
	private TableColumn<DailySalesTableData, String> checkColumn;
	@FXML
	private TableColumn<DailySalesTableData, Integer> transColumn;


	/************************
	 * Objects AND Variables
	 ***********************/
	private ObservableList<String> productList = FXCollections.observableArrayList();
	private ObservableList<DailySalesTableData> tableData = FXCollections.observableArrayList();


	/* *********************
	 * COMMON METHODS
	 * ********************/
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date dated = new Date();

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


	/* *********************
	 * CORE METHODS
	 * ********************/
	@FXML
	private void addTransaction(ActionEvent event) throws Exception{
		LocalDate dated = datePicker2.getValue();
		String date = dated.toString();
		String status = "নগদ";
		int theRate = 0;
		int theStorage = 0;

		DatabaseConnection ob = new DatabaseConnection();
		ob.setQuery(ob.connect().createStatement());
		ResultSet rs = ob.getQuery().executeQuery("SELECT product_price,product_storage FROM products WHERE product_name='"+producSelect.getValue()+"';");
		if(rs.next()){
			theRate = Integer.parseInt(rs.getString("product_price"));
			theStorage = Integer.parseInt(rs.getString("product_storage"));
		}
		rs.close();


		int theQuantity = Integer.parseInt(quantity.getText());

		theStorage = theStorage - theQuantity;

		int total = theQuantity*theRate;
		int theDeposit;

		if(deposit.getText().equals("")){
			theDeposit = 0;
		}else{
			theDeposit = Integer.parseInt(deposit.getText());
		}

		int theRest = total - theDeposit;

		if(check.isSelected()){
			status = "বাকী";
		}

		if(theStorage < theQuantity){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("ERROR");
			alert.setContentText("Not enough product in storage.");
			alert.showAndWait();
		}else{
			ob.puts("UPDATE products SET product_storage='"+theStorage+"' WHERE product_name='"+producSelect.getValue()+"';");
			ob.puts("INSERT INTO daily_sells (date,product,quantity,price,total,deposit,rest,status) VALUES('"+date+"','"+producSelect.getValue()+"','"+theQuantity+"','"+theRate+"','"+total+"','"+theDeposit+"','"+theRest+"','"+status+"');");

			int theTrans = 0;
			ResultSet rt= ob.getQuery().executeQuery("select trans_id from daily_sells order by trans_id desc limit 1;");
			if(rt.next()){
				theTrans = Integer.parseInt(rt.getString("trans_id"));
			}
			rt.close();

			tableData.add(new DailySalesTableData(date,producSelect.getValue().toString(),theQuantity,theRate,total,theDeposit,theRest,theTrans,status));
		}

		ob.connect().close();
	}


	@FXML
	private void deleteTransaction() throws Exception{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete transaction");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure? You will not get this data back again!");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			DailySalesTableData thedata = table.getSelectionModel().getSelectedItem();
			tableData.remove(thedata);
			int theStorage = 0;


			DatabaseConnection db = new DatabaseConnection();
			db.setQuery(db.connect().createStatement());

			ResultSet rs = db.getQuery().executeQuery("select product_storage from products where product_name = '"+thedata.getProduct()+"';");
			if(rs.next()){
				theStorage = Integer.parseInt(rs.getString("product_storage"));
			}
			rs.close();

			theStorage = theStorage + thedata.getQuantity();

			db.puts("UPDATE products SET product_storage='"+theStorage+"' WHERE product_name='"+thedata.getProduct()+"';");
			db.puts("DELETE FROM daily_sells WHERE trans_id='"+thedata.getTrans()+"'");
			db.connect().close();

		} else {
			System.out.println("Good Choice!");
		}
	}




	@FXML
	private void dateData() throws Exception{
		tableData.removeAll(tableData);
		String date = null;
		if(datePicker.getValue() == null){
			 DateFormat theFormat = new SimpleDateFormat("yyyy-MM-dd");
			 Date theDate = new Date();
			 date =  theFormat.format(theDate);
		}else{
			LocalDate dated = datePicker.getValue();
			date = dated.toString();
		}

		DatabaseConnection ob = new DatabaseConnection();
		ob.setQuery(ob.connect().createStatement());
		ResultSet rs = ob.getQuery().executeQuery("SELECT * FROM daily_sells WHERE date='"+date+"'");
		while (rs.next()) {
			int theRate = Integer.parseInt(rs.getString("price"));
			int theQuantity = Integer.parseInt(rs.getString("quantity"));
			int theTotal = Integer.parseInt(rs.getString("total"));
			int theDeposit = Integer.parseInt(rs.getString("deposit"));
			int theRest = Integer.parseInt(rs.getString("rest"));
			int theTrans = Integer.parseInt(rs.getString("trans_id"));
			tableData.add(new DailySalesTableData(rs.getString("date"), rs.getString("product"), theQuantity, theRate, theTotal, theDeposit,theRest,theTrans,rs.getString("status")));
		}
		rs.close();
		ob.connect().close();
	}

	private void getProduct(){
		DatabaseConnection ob = new DatabaseConnection();
		try {
			ob.setQuery(ob.connect().createStatement());
			ResultSet rs = ob.getQuery().executeQuery("select product_name from products;");
			while(rs.next()){
				productList.add(rs.getString("product_name"));
			}
			rs.close();
			ob.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		producSelect.getItems().addAll(productList);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		try {
			dateData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dateColumn.setCellValueFactory(cellData->new ReadOnlyStringWrapper(cellData.getValue().getdate()));
		productColumn.setCellValueFactory(cellData->new ReadOnlyStringWrapper(cellData.getValue().getProduct()));
		quantityColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getQuantity()));
		priceColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getRate()));
		totalColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getTotalPrice()));
		depositColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getDeposit()));
		restColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getRest()));
		transColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getTrans()));
		checkColumn.setCellValueFactory(cellData->new ReadOnlyStringWrapper(cellData.getValue().getStatus()));
		table.setItems(getPersonData());

		getProduct();

		datePicker.setPromptText(dateFormat.format(dated));
		datePicker2.setPromptText(dateFormat.format(dated));
	}

	public ObservableList<DailySalesTableData> getPersonData() {
		return tableData;
	}

}
