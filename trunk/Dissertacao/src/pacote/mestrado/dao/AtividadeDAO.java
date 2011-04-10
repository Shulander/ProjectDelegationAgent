package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pacote.mestrado.dominios.TipoEstado;
import pacote.mestrado.entidades.Atividade;

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
	    String sql = "SELECT * FROM atividades";	   
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
		atividade.setEstado(TipoEstado.obterPorCodigo(rs.getString("estado")));
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
  

}
