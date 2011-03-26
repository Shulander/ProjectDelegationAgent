package pacote.mestrado.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class AtividadeDAO 
{
    private Connection connection;
    
    public AtividadeDAO() 
    {
	this.connection = new ConnectionFactory().getConnection();
    }
    
    public List<Atividade> getAtividades ()
    {
	try {
	    String sql = "SELECT * FROM atividade";	   
	    PreparedStatement stmt = this.connection.prepareStatement(sql);	 
	    ResultSet rs = stmt.executeQuery();	    
	    List<Atividade> atividades = new ArrayList<Atividade>();
	    while (rs.next()) {
		Atividade atividade = new Atividade();
		atividade.setId(rs.getInt("id"));
		atividade.setNome(rs.getString("nome"));
		atividade.setTipo(rs.getString("tipo"));
		atividade.setDataInicial(rs.getDate("dataInicial"));
		atividade.setDataEntrega(rs.getDate("dataEntrega"));
		atividade.setDuracao(rs.getDouble("duracao"));
		atividade.setEstado(rs.getString("estado"));
		atividade.setOrcamento(rs.getDouble("orcamento"));
		atividades.add(atividade);
	    }	    	    
	    rs.close();
	    stmt.close();
	    return atividades;
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}	 
    }
    
    public List<Habilidade> getHabilidades (int id)
    {
	try {
	    String sql = "SELECT * FROM habilidades_atividade " +
	    		"JOIN habilidades WHERE " +
	    		"habilidades.id=habilidades_atividade.fk_idHabilidades " +
	    		"AND fk_idAtividade=?";
	    PreparedStatement stmt = this.connection.prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();	    
	    List<Habilidade> habilidades = new ArrayList<Habilidade>();
	    while (rs.next()) {
		System.out.println("entrei aqui");
		Habilidade habilidade = new Habilidade();
		habilidade.setId(rs.getInt("id"));
		habilidade.setArea(rs.getString("area"));
		habilidade.setNome(rs.getString("nome"));
		habilidade.setNivel(rs.getString("nivel"));
		habilidades.add(habilidade);
		System.out.println(habilidade.toString());
	    }	    	    
	    rs.close();
	    stmt.close();
	    return habilidades;
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}	 
    }
    

}
