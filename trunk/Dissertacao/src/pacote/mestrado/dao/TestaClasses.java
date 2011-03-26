package pacote.mestrado.dao;

import pacote.mestrado.Membro;
import java.sql.Connection;
import java.sql.SQLException;

public class TestaClasses 
{
    public static void main(String[] args) 
    {
	Connection connection = new ConnectionFactory().getConnection();
	System.out.println("Conexão aberta!");
	MembroDAO dao = new MembroDAO();
	Membro membro = new Membro ();
	membro = dao.get("Liane Cafarate");
	System.out.println(membro.toString());
	try {
	    connection.close();
	    System.out.println("Conexão fechada!");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
    }
}
