
import java.sql.*;

public class TestCon {
	public Connection getConnection()
	//public static void main(String[] args)
	{
	Connection con= null;
		try {
        Class.forName("com.mysql.jdbc.Driver");
         System.out.println("Loaded driver");
    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/exceltables?user=root&password=");
    System.out.println("This is working");
 }
 catch(Exception e)
 {
	 e.printStackTrace();
 }

System.out.println("connection established");
return con;


}

}
