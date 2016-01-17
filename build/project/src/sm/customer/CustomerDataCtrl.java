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
	private Button addTransactionBtn;
	@FXML
	private Button deleteTransactionBtn;

	@FXML
	private DatePicker datePicker;

	@FXML
	private ComboBox <String> productSelect;
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
	int theid;

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
			tableData.add(new DailySalesTableData(td.getString("date"), td.getString("product"), td.getInt("quantity"), td.getInt("price"), td.getInt("total"), td.getInt("deposit"), td.getInt("rest"), td.getInt("trans_id"), td.getString("status")));
		}
		td.close();

		ob.connect().close();


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

		int theRate = 0;
		int theQuantity = 0;
		int theDeposit = 0;
		int total = 0;
		int theRest = 0;
		String dispo = null;

		DatabaseConnection ob = new DatabaseConnection();
		ob.setQuery(ob.connect().createStatement());

		if(productSelect.getValue() == null){
			dispo = "জমা";
		}else{
			dispo = productSelect.getValue();

			ResultSet rs = ob.getQuery().executeQuery("SELECT product_price FROM products WHERE product_name='"+dispo+"';");
			if(rs.next()){
				theRate = Integer.parseInt(rs.getString("product_price"));
			}
			rs.close();
		}


		if(!quantity.getText().equals("")){
			theQuantity = Integer.parseInt(quantity.getText());
		}

		if(!deposit.getText().equals("")){
			theDeposit = Integer.parseInt(deposit.getText());
		}

		total = theQuantity*theRate;
		if(total > 0){
			theRest = total - theDeposit;
		}


		ob.puts("INSERT INTO customers_data (date,customer_id,product,quantity,price,total,deposit,rest,status) VALUES('"+date+"','"+theid+"','"+dispo+"','"+theQuantity+"','"+theRate+"','"+total+"','"+theDeposit+"','"+theRest+"','"+status+"');");

		int theTrans = 0;
		ResultSet rt= ob.getQuery().executeQuery("select trans_id from customers_data order by trans_id desc limit 1;");
		if(rt.next()){
			theTrans = Integer.parseInt(rt.getString("trans_id"));
		}
		rt.close();

		tableData.add(new DailySalesTableData(date,dispo,theQuantity,theRate,total,theDeposit,theRest,theTrans,status));

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

			DatabaseConnection db = new DatabaseConnection();
			db.setQuery(db.connect().createStatement());

			db.puts("DELETE FROM customers_data WHERE trans_id='"+thedata.getTrans()+"'");
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
	}

	public ObservableList<DailySalesTableData> getPersonData() {
		return tableData;
	}


}
