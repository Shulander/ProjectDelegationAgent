package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pacote.mestrado.dominios.TipoEstado;
import pacote.mestrado.entidades.Atividade;

public class AtividadeDAO {
    private Connection connection;

    public AtividadeDAO() {
	this.connection = new ConnectionFactory().getConnection();
    }

    public List<Atividade> getAtividades() {
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
		// atividade.setPeriodoExecucao(new Periodo());
		// atividade.getPeriodoExecucao().setInicio(rs.getDate("dataInicial"));
		// atividade.getPeriodoExecucao().setFim(rs.getDate("dataEntrega"));
		atividade.setDataInicial(rs.getDate("dataInicial"));
		atividade.setDataEntrega(rs.getDate("dataEntrega"));
//		atividade.setDuracao(rs.getDouble("duracao"));
		atividade.setEstado(TipoEstado.obterPorCodigo(rs.getString("estado")));
//		atividade.setOrcamento(rs.getDouble("orcamento"));
		atividades.add(atividade);
	    }
	    rs.close();
	    stmt.close();

	    getRequisitosAtividades(atividades);

	    return atividades;
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}
    }

    private void getRequisitosAtividades(List<Atividade> atividades) {

	try {
	    String sql = "SELECT * FROM atividades_predecessoras";
	    PreparedStatement stmt = this.connection.prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
		int idAtividade = rs.getInt("fk_idAtividade");
		int idAtividadePredecessora = rs.getInt("fk_idAtividadePredecessora");
		Atividade atividade = encontraAtividade(idAtividade, atividades);
		Atividade atividadePredecessora = encontraAtividade(idAtividadePredecessora, atividades);
		if (atividade != null && atividadePredecessora != null) {
		    atividade.getAtividadesPredecessoras().add(atividadePredecessora);
		}
	    }
	    rs.close();
	    stmt.close();

	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}

    }

    private Atividade encontraAtividade(int idAtividade, List<Atividade> atividades) {
	for (Atividade atividade : atividades) {
	    if (atividade.getId() == idAtividade) {
		return atividade;
	    }
	}
	return null;
    }

    private void getRequisitos() {

    }

}
