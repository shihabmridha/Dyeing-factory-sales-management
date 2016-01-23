package sm.customer;

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
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import sm.defaults.DailySalesTableData;
import sm.defaults.DatabaseConnection;
import sm.defaults.GlobalFunctions;

public class CustomerDataCtrl implements Initializable{
	@FXML
	private MenuBar menu;

	@FXML
	private Label name;
	@FXML
	private Label mobile;
	@FXML
	private Label address;
	@FXML
	private Label totalRest;

	@FXML
	private Button addTransactionBtn;
	@FXML
	private Button deleteTransactionBtn;

	@FXML
	private DatePicker datePicker;

	@FXML
	private ComboBox <String> productSelect;
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
	int theid;
	double ltotal = 0,ldepo = 0, lgrand = 0;

	public void setId(int id) throws Exception{
		this.theid = id;

		DatabaseConnection ob = new DatabaseConnection();
		ob.setQuery(ob.connect().createStatement());
		ResultSet rs = ob.getQuery().executeQuery("select * from customers where customer_id="+theid+";");
		if(rs.next()){
			name.setText(": "+ rs.getString("name"));
			mobile.setText(": "+ rs.getString("mobile"));
			address.setText(": "+ rs.getString("address"));
		}
		rs.close();

		ResultSet td = ob.getQuery().executeQuery("select * from customers_data where customer_id="+theid+";");
		while(td.next()){
			ltotal += td.getDouble("total");
			ldepo += td.getDouble("deposit");
			lgrand = ltotal - ldepo;
			totalRest.setText(": " + Double.toString(lgrand));
			tableData.add(new DailySalesTableData(td.getString("date"), td.getString("product"), td.getDouble("quantity"), td.getDouble("price"), td.getDouble("total"), td.getDouble("deposit"), td.getDouble("rest"), td.getInt("trans_id"), td.getString("status")));
		}
		td.close();

		ob.connect().close();


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
	}

	/* *********************
	 * COMMON METHODS
	 * ********************/
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date dated = new Date();

	@FXML
	private void back(ActionEvent event) throws Exception{
		Stage stage = (Stage) menu.getScene().getWindow();
		Scene scene = menu.getScene();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerHome.fxml"));
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
		LocalDate dated = datePicker.getValue();
		String date = dated.toString();
		String status = "নগদ";
		if(check.isSelected()){
			status = "বাকী";
		}

		double theRate = 0;
		double theQuantity = 0;
		double theDeposit = 0;
		double total = 0;
		double theRest = 0;

		DatabaseConnection ob = new DatabaseConnection();
		ob.setQuery(ob.connect().createStatement());

		if(!productSelect.getValue().equals("জমা")){

			ResultSet rs = ob.getQuery().executeQuery("SELECT product_price1,product_price2 FROM products WHERE product_name='"+productSelect.getValue()+"';");

			if(price.getText().equals("")){
				if(rs.next()){
					if(priceSelect.getValue().equals("Price 1")){
						theRate = rs.getDouble("product_price1");
					}else{
						theRate = rs.getDouble("product_price2");
					}
				}
			}else{
				theRate = Double.parseDouble(price.getText());
			}
			rs.close();

			if(!quantity.getText().equals("")){
				theQuantity = Double.parseDouble(quantity.getText());
			}
		}



		if(!deposit.getText().equals("")){
			theDeposit = Double.parseDouble(deposit.getText());
		}


		total = theQuantity*theRate;

		if(!check.isSelected() && !productSelect.getValue().equals("জমা")){
			theDeposit = total;
		}

		if(total > 0){
			theRest = total - theDeposit;
		}

		total = GlobalFunctions.round(total, 2);
		theDeposit = GlobalFunctions.round(theDeposit, 2);

		ob.puts("INSERT INTO customers_data (date,customer_id,product,quantity,price,total,deposit,rest,status) VALUES('"+date+"','"+theid+"','"+productSelect.getValue()+"','"+theQuantity+"','"+theRate+"','"+total+"','"+theDeposit+"','"+theRest+"','"+status+"');");

		int theTrans = 0;
		ResultSet rt= ob.getQuery().executeQuery("select trans_id from customers_data order by trans_id desc limit 1;");
		if(rt.next()){
			theTrans = Integer.parseInt(rt.getString("trans_id"));
		}
		rt.close();

		tableData.add(new DailySalesTableData(date,productSelect.getValue(),theQuantity,theRate,total,theDeposit,theRest,theTrans,status));

		ob.connect().close();

		if(theQuantity <=0 && theDeposit != 0){
			lgrand -= theDeposit;
		}
		lgrand += theRest;
		totalRest.setText(": " + Double.toString(lgrand));
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

			DatabaseConnection db = new DatabaseConnection();
			db.setQuery(db.connect().createStatement());

			db.puts("DELETE FROM customers_data WHERE trans_id='"+thedata.getTrans()+"'");

			if(thedata.getRest() == 0){
				if(thedata.getDeposit() != 0 && thedata.getQuantity() == 0){
					lgrand+= thedata.getDeposit();
					totalRest.setText(": " + Double.toString(lgrand));
				}
			}else{
				lgrand -= thedata.getRest();
				totalRest.setText(": " + Double.toString(lgrand));
			}
			db.connect().close();
		} else {
			System.out.println("Good Choice!");
		}
	}

	@FXML
	private void editCustomer() throws Exception{
		Scene scene = menu.getScene();
		Stage stage = (Stage) scene.getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCustomer.fxml"));
		scene.setRoot(loader.load());
		stage.setScene(scene);
		EditCustomerCtrl ob = loader.<EditCustomerCtrl>getController();
		ob.setId(theid);
		stage.show();
	}


	private void getProduct(){
		productList.add("জমা");
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
		productSelect.getItems().addAll(productList);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getProduct();
		priceSelect.getItems().addAll("Price 1", "Price 2");
	}

	public ObservableList<DailySalesTableData> getPersonData() {
		return tableData;
	}


}
