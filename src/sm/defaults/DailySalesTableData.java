package sm.defaults;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DailySalesTableData {
	private final StringProperty date;
	private final StringProperty product;
	private final IntegerProperty quantity;
	private final IntegerProperty rate;
	private final IntegerProperty totalPrice;
	private final IntegerProperty deposit;
	private final IntegerProperty rest;
	private final IntegerProperty transID;
	private final StringProperty status;

	public DailySalesTableData(String date,String product, int quantity, int rate, int totalPrice,int deposit,int rest,int transID, String status){
		this.date = new SimpleStringProperty(date);
		this.product = new SimpleStringProperty(product);
		this.quantity = new SimpleIntegerProperty(quantity);
		this.rate = new SimpleIntegerProperty(rate);
		this.totalPrice = new SimpleIntegerProperty(totalPrice);
		this.deposit = new SimpleIntegerProperty(deposit);
		this.rest = new SimpleIntegerProperty(rest);
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
	public int getQuantity() {
		return quantity.get();
	}


	public void setRate(int rate) {
		this.rate.set(rate);
	}
	public int getRate() {
		return rate.get();
	}


	public void setTotalPrice(int totalPrice) {
		this.totalPrice.set(totalPrice);
	}
	public int getTotalPrice() {
		return totalPrice.get();
	}

	public void setDeposit(int deposit) {
		this.deposit.set(deposit);
	}
	public int getDeposit() {
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
	public int getRest() {
		return rest.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}
	public String getStatus() {
		return status.get();
	}


}
