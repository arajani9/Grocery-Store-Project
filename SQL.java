import java.sql.* ;
// done 
public class SQL {
	Statement statement;
	public SQL() throws SQLException{
		try {
			Class.forName("org.postgresql.Driver");
			} catch (Exception cnfe){
			    System.out.println("Class not found");
			    }	 
		String url = "jdbc:postgresql://localhost:5432/Ecom";
		//Enter you database server user name and password below.
		Connection con = DriverManager.getConnection (url, "username", "password") ;
	    statement = con.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE ) ;
	    System.out.println("Build connection with database successfully");
	}
	public void WriteExcute(String sqlCode){
			try {
				statement.executeUpdate ( sqlCode ) ;
				System.out.println("Write excute the code "+sqlCode);
			} catch (SQLException e)
			    {
				int errorCode = e.getErrorCode(); // Get SQLCODE
				String sqlState = e.getSQLState(); // Get SQLSTATE     
				System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
			    }
			}
		public java.sql.ResultSet QueryExchte(String sqlCode)
		{ 
			java.sql.ResultSet rs = null;
			try {
			    rs = statement.executeQuery (sqlCode ) ;
			    System.out.println("Query excute the code "+sqlCode);
			} catch (SQLException e)
			    {
				int errorCode = e.getErrorCode(); // Get SQLCODE
				String sqlState = e.getSQLState(); // Get SQLSTATE     
				System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
			    }
			return rs;
		}
}