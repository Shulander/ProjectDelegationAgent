package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pacote.mestrado.Membro;
import pacote.mestrado.dominios.TipoNivel;
import pacote.mestrado.entidades.Habilidade;

public class MembroDAO 
{
    private Connection connection;
    
    public MembroDAO() 
    {
	this.connection = new ConnectionFactory().getConnection();
    }
    
    public Membro get (String nome)
    {
	try {
	    String sql = "SELECT * FROM membro WHERE nome=?";
	    PreparedStatement stmt = this.connection.prepareStatement(sql);
	    stmt.setString(1, nome);
	    ResultSet rs = stmt.executeQuery();
	    Membro membro = new Membro();
	    while (rs.next()) {		
		membro.setId(rs.getInt("id"));
		membro.setNome(rs.getString("nome"));
		membro.setSalario(rs.getDouble("salario"));		
	    }	    	    
	    rs.close();
	    stmt.close();
	    return membro;
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}
    }
    
    public List<Habilidade> getHabilidades (int id)
    {
	try {
	    String sql = "SELECT * FROM habilidade " +
	    		"JOIN habilidades WHERE " +
	    		"habilidades.id=habilidade.fk_idHabilidades " +
	    		"AND fk_idMembro=?";	   
	    PreparedStatement stmt = this.connection.prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();	    
	    List<Habilidade> habilidades = new ArrayList<Habilidade>();
	    while (rs.next()) {
		Habilidade habilidade = new Habilidade();
		habilidade.setId(rs.getInt("id"));
		habilidade.setArea(rs.getString("area"));
		habilidade.setNome(rs.getString("nome"));
		habilidade.setNivel(TipoNivel.obterPorCodigo(rs.getInt("nivel")));
		habilidade.setXp(rs.getInt("xp"));
		habilidades.add(habilidade);
	    }	    	    
	    rs.close();
	    stmt.close();
	    return habilidades;
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}	 
    }
    
}
