import java.sql.Connection;
import java.sql.DriverManager;


public class LoadDriver {
	public Connection getConnection()
	//public static void main(String[] args)
	{
	Connection con= null;
		try {
        Class.forName("com.mysql.jdbc.Driver");
         System.out.println("Loaded driver");
    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/futortables?user=root&password=");
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



