package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory 
{
    
    private static Connection conexao; 
    
    public ConnectionFactory() {
	conexao = null;
    }
    
    public Connection getConnection()
    {
	System.out.println("Conectando ao banco");
	try {
	    if(conexao == null) {
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}  
		conexao = DriverManager.getConnection("jdbc:mysql://localhost/mestrado2", "root", "");
	    }
	    return conexao;
	} catch(SQLException e) {
	    throw new RuntimeException(e);
	}
    }
    

}
