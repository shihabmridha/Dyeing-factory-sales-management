package sm.defaults;
import java.sql.*;

/* *****************************************
 *  Developed by: Shihab Mridha
 *  E-mail: shihabmridha@gmail.com
 *  Web: www.shihabmridha.com
 * ****************************************/

public class DatabaseConnection {
	private Connection c = null;
	private Statement query = null;

	public DatabaseConnection(){
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:main.db");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	}

	public void puts(String sql){
		try {
	      query = c.createStatement();
	      query.executeUpdate(sql);
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		System.out.println("Query OK!");
	}

	public boolean hasTable(){
		DatabaseMetaData meta;
		try {
			meta = c.getMetaData();
			ResultSet res = meta.getTables(null, null, "staf", new String[] {"TABLE"});
			if (res.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void createTable(){
		puts("CREATE TABLE staf(staf_id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT not null, password TEXT not null);");

		puts("CREATE TABLE products("
				+ "product_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "product_name TEXT not null,"
				+ "product_price1 REAL default 0,"
				+ "product_price2 REAL default 0,"
				+ "product_storage REAL default 0); ");

		puts("CREATE TABLE address (address_id INTEGER primary key autoincrement, address_name text not null);");
		puts("CREATE TABLE customers(customer_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT not null, address INT not null, mobile TEXT not null);");

		puts("CREATE TABLE customers_data("
				+ "trans_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "customer_id INT not null,"
				+ "date TEXT not null,"
				+ "product TEXT not null,"
				+ "quantity REAL not null,"
				+ "price REAL not null,"
				+ "total REAL not null,"
				+ "deposit REAL not null,"
				+ "rest REAL not null, "
				+ "status TEXT not null); ");

		puts("CREATE TABLE daily_sells("
				+ "trans_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "date TEXT not null,"
				+ "product TEXT not null,"
				+ "quantity REAL not null,"
				+ "price REAL not null,"
				+ "total REAL not null,"
				+ "deposit REAL not null,"
				+ "rest REAL not null,"
				+ "status TEXT not null); ");

		puts("insert into staf (username,password) values ('admin','123')");
	}

	public Connection connect(){
		return c;
	}
	public Statement query(){
		return query;
	}
	public void setQuery(Statement query){
		this.query = query;
	}

	public Statement getQuery(){
		return query;
	}

}
