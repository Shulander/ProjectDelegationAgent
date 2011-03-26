package pacote.mestrado.dao;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory 
{
    public java.sql.Connection getConnection() 
    {
	System.out.println("Conectando ao banco");
	try {
	    return DriverManager.getConnection("jdbc:mysql://localhost/mestrado", "root", "");
	} catch(SQLException e) {
	    throw new RuntimeException(e);
	}
    }
    

}
