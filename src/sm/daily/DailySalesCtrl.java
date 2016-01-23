package sm.daily;

import java.net.URL;
import java.sql.ResultSet;
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
	private DatePicker datePicker1;
	@FXML
	private DatePicker datePicker2;
	@FXML
	private ComboBox <String> producSelect;
	@FXML
	private ComboBox <String> priceSelect;

	@FXML
	private TextField price;
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
	private TableColumn<DailySalesTableData, Double> quantityColumn;
	@FXML
	private TableColumn<DailySalesTableData, Double> priceColumn;
	@FXML
	private TableColumn<DailySalesTableData, Double> totalColumn;
	@FXML
	private TableColumn<DailySalesTableData, Double> depositColumn;
	@FXML
	private TableColumn<DailySalesTableData, Double> restColumn;
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
		if(check.isSelected()){
			status = "বাকী";
		}

		double theRate = 0;
		double theStorage = 0;
		double theQuantity = 0;
		double total = 0;
		double theDeposit = 0;

		DatabaseConnection ob = new DatabaseConnection();
		ob.setQuery(ob.connect().createStatement());

		ResultSet rs = ob.getQuery().executeQuery("SELECT product_price1,product_price2,product_storage FROM products WHERE product_name='"+producSelect.getValue()+"';");
		if(price.getText().equals("")){
			if(rs.next()){
				if(priceSelect.getValue().equals("Price 1")){
					theRate = rs.getDouble("product_price1");
				}else{
					theRate = rs.getDouble("product_price2");
				}
				theStorage = rs.getDouble("product_storage");
			}
		}else{
			theRate = Double.parseDouble(price.getText());
			theStorage = rs.getDouble("product_storage");
		}
		rs.close();


		theQuantity = Double.parseDouble(quantity.getText());

		total = theQuantity*theRate;

		theDeposit = 0;

		if(!check.isSelected()){
			theDeposit = total;
		}
		else{
			if(!deposit.getText().equals("")){
				theDeposit = Double.parseDouble(deposit.getText());
			}
		}

		double theRest = total - theDeposit;

		if(theStorage < theQuantity){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("ERROR");
			alert.setContentText("Not enough product in storage.");
			alert.showAndWait();
		}else{
			theStorage = theStorage - theQuantity;
			total = GlobalFunctions.round(total, 2);
			theDeposit = GlobalFunctions.round(theDeposit, 2);

			ob.puts("UPDATE products SET product_storage='"+theStorage+"' WHERE product_name='"+producSelect.getValue()+"';");
			ob.puts("INSERT INTO daily_sells (date,product,quantity,price,total,deposit,rest,status) VALUES('"+date+"',"
					+ "'"+producSelect.getValue()+"',"
							+ "'"+theQuantity+"',"
									+ "'"+theRate+"',"
											+ "'"+total+"',"
													+ "'"+theDeposit+"',"
															+ "'"+theRest+"',"
																	+ "'"+status+"');");

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
			double theStorage = 0;

			DatabaseConnection db = new DatabaseConnection();
			db.setQuery(db.connect().createStatement());

			ResultSet rs = db.getQuery().executeQuery("select product_storage from products where product_name = '"+thedata.getProduct()+"';");
			if(rs.next()){
				theStorage = rs.getDouble("product_storage");
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
		if(datePicker1.getValue() == null){
			 DateFormat theFormat = new SimpleDateFormat("yyyy-MM-dd");
			 Date theDate = new Date();
			 date =  theFormat.format(theDate);
		}else{
			LocalDate dated = datePicker1.getValue();
			date = dated.toString();
		}

		DatabaseConnection ob = new DatabaseConnection();
		ob.setQuery(ob.connect().createStatement());
		ResultSet rs = ob.getQuery().executeQuery("SELECT * FROM daily_sells WHERE date='"+date+"'");
		while (rs.next()) {
			double theRate = rs.getDouble("price");
			double theQuantity = rs.getDouble("quantity");
			double theTotal = rs.getDouble("total");
			double theDeposit = rs.getDouble("deposit");
			double theRest = rs.getDouble("rest");
			int theTrans = rs.getInt("trans_id");
			tableData.add(new DailySalesTableData(rs.getString("date"), rs.getString("product"), theQuantity, theRate, theTotal, theDeposit,theRest,theTrans,rs.getString("status")));
		}
		rs.close();
		ob.connect().close();
	}

	private void getProduct() throws Exception{
		DatabaseConnection ob = new DatabaseConnection();

		ob.setQuery(ob.connect().createStatement());
		ResultSet rs = ob.getQuery().executeQuery("select product_name from products;");
		while(rs.next()){
			productList.add(rs.getString("product_name"));
		}
		rs.close();

		ob.connect().close();

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
		quantityColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Double>(cellData.getValue().getQuantity()));
		priceColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Double>(cellData.getValue().getRate()));
		totalColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Double>(cellData.getValue().getTotalPrice()));
		depositColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Double>(cellData.getValue().getDeposit()));
		restColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Double>(cellData.getValue().getRest()));
		transColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getTrans()));
		checkColumn.setCellValueFactory(cellData->new ReadOnlyStringWrapper(cellData.getValue().getStatus()));
		table.setItems(getPersonData());

		try {
			getProduct();
		} catch (Exception e) {
			e.printStackTrace();
		}

		datePicker1.setPromptText(dateFormat.format(dated));
		datePicker2.setPromptText(dateFormat.format(dated));
		priceSelect.getItems().addAll("Price 1", "Price 2");
	}

	public ObservableList<DailySalesTableData> getPersonData() {
		return tableData;
	}

}
