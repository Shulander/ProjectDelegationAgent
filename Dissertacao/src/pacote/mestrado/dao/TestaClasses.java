package pacote.mestrado.dao;

import pacote.mestrado.Membro;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TestaClasses 
{
    public static void main(String[] args) 
    {
	Connection connection = new ConnectionFactory().getConnection();
	System.out.println("Conexão aberta!");
	MembroDAO dao = new MembroDAO();
	List<Membro> membros = dao.getLista();
	for (Membro membro : membros) {
	    System.out.println("----------------------------");
	    System.out.println("ID: " +membro.getId());
	    System.out.println("Nome: " + membro.getNome());
	    System.out.println("Salario: " + membro.getSalario());	    
	}
	System.out.println("----------------------------");
	try {
	    connection.close();
	    System.out.println("Conexão fechada!");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
    }
}
