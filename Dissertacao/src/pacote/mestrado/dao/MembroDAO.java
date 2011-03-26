package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pacote.mestrado.dao.ConnectionFactory;
import pacote.mestrado.Membro;
import pacote.mestrado.entidades.Habilidade;

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
	    List<Habilidade> habilidades = new ArrayList<Habilidade>();
	    PreparedStatement stmt = this.connection.prepareStatement(
		    "SELECT membro.id AS idMembro, " +
		    "membro.nome AS nomeMembro, " +
		    "membro.salario, " +
		    "habilidade.id AS idHabilidade, " +
		    "habilidade.area AS areaHabilidade, " +
		    "habilidade.nome AS nomeHabilidade " +
		    "FROM membro INNER JOIN " +
		    "(membro_habilidade INNER JOIN habilidade ON " +
		    "membro_habilidade.fk_idHabilidade=habilidade.id)" +
		    "ON membro.id=membro_habilidade.fk_idMembro;"
	    );
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
		// criando o objeto Membro
		Membro membro = new Membro();
		membro.setId(rs.getInt("idMembro"));
		membro.setNome(rs.getString("nomeMembro"));
		membro.setSalario(rs.getDouble("salario"));
		habilidades.
		membro.setHabilidades(habilidades);
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
