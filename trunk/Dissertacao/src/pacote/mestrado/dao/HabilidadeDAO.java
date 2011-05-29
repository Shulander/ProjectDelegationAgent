package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pacote.mestrado.dominios.TipoNivel;
import pacote.mestrado.entidades.Habilidade;

public class HabilidadeDAO {
    private Connection connection;

    public HabilidadeDAO() {
	this.connection = new ConnectionFactory().getConnection();
    }

    public List<Habilidade> getHabilidades(int id, String atributo) {
	try {
	    if (!atributo.equals("Membro") && !atributo.equals("Atividade") && !atributo.equals("MembroAprendizado")) {
		System.out.println("Erro! Atributo tem que ser Membro ou Atividade!");
		return null;
	    }
	    String sql = "SELECT * FROM habilidades JOIN tipos_habilidade "
		    + "WHERE habilidades.fk_idHabilidade = tipos_habilidade.id " + "AND fk_id" + atributo + "=?";
	    PreparedStatement stmt = this.connection.prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
	    List<Habilidade> habilidades = new ArrayList<Habilidade>();
	    while (rs.next()) {
		Habilidade habilidade = new Habilidade();
		habilidade.setId(rs.getInt("fk_idHabilidade"));
		habilidade.setArea(rs.getString("area"));
		habilidade.setNome(rs.getString("nome"));
		habilidade.setNivel(TipoNivel.obterPorCodigo(rs.getInt("nivel")));
		//TODO recuperar a experiencia do banco
		habilidade.setXp(0);
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
