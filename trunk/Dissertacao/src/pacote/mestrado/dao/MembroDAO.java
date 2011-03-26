package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pacote.mestrado.Membro;

public class MembroDAO 
{
    private Connection connection;
    
    public MembroDAO() 
    {
	this.connection = new ConnectionFactory().getConnection();
    }
    
    public List<Membro> getLista() 
    {
	try {
	    List<Membro> membros = new ArrayList<Membro>();
	    PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM membro");
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
		// criando o objeto Membro
		Membro membro = new Membro();
		membro.setId(rs.getInt("id"));
		membro.setNome(rs.getString("nome"));
		membro.setSalario(rs.getDouble("salario"));
		// adicionando o objeto à lista
		membros.add(membro);
	    }
	    rs.close();
	    stmt.close();
	    return membros;
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}
    }
}
