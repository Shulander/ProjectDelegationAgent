package pacote.mestrado.dao;

import java.sql.*;

public class ConnectionFactory 
{
    public Connection getConnection() 
    {
	System.out.println("Conectando ao banco");
	try {
	    return DriverManager.getConnection("jdbc:mysql://localhost/mestrado", "root", "");
	} catch(SQLException e) {
	    throw new RuntimeException(e);
	}
    }
    

}
