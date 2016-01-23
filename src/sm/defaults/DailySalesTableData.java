package sm.defaults;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DailySalesTableData {
	private final StringProperty date;
	private final StringProperty product;
	private final DoubleProperty quantity;
	private final DoubleProperty rate;
	private final DoubleProperty totalPrice;
	private final DoubleProperty deposit;
	private final DoubleProperty rest;
	private final IntegerProperty transID;
	private final StringProperty status;

	public DailySalesTableData(String date,String product, double quantity, double rate, double totalPrice,double deposit,double rest,int transID, String status){
		this.date = new SimpleStringProperty(date);
		this.product = new SimpleStringProperty(product);
		this.quantity = new SimpleDoubleProperty(quantity);
		this.rate = new SimpleDoubleProperty(rate);
		this.totalPrice = new SimpleDoubleProperty(totalPrice);
		this.deposit = new SimpleDoubleProperty(deposit);
		this.rest = new SimpleDoubleProperty(rest);
		this.transID = new SimpleIntegerProperty(transID);
		this.status = new SimpleStringProperty(status);
	}

	public void setdate(String date) {
		this.date.set(date);
	}
	public String getdate() {
		return date.get();
	}

	public void setshareDepositValue(String product) {
		this.product.set(product);
	}
	public String getProduct() {
		return product.get();
	}


	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}
	public double getQuantity() {
		return quantity.get();
	}


	public void setRate(int rate) {
		this.rate.set(rate);
	}
	public double getRate() {
		return rate.get();
	}


	public void setTotalPrice(int totalPrice) {
		this.totalPrice.set(totalPrice);
	}
	public double getTotalPrice() {
		return totalPrice.get();
	}

	public void setDeposit(int deposit) {
		this.deposit.set(deposit);
	}
	public double getDeposit() {
		return deposit.get();
	}

	public void setTrans(int trans) {
		this.transID.set(trans);
	}
	public int getTrans() {
		return transID.get();
	}

	public void setRest(int rest) {
		this.rest.set(rest);
	}
	public double getRest() {
		return rest.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}
	public String getStatus() {
		return status.get();
	}


}
