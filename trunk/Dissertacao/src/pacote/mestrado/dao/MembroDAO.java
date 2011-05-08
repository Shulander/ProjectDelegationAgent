package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pacote.mestrado.Membro;
import pacote.mestrado.services.CustoService;

public class MembroDAO {
    private Connection connection;

    public MembroDAO() {
	this.connection = new ConnectionFactory().getConnection();
    }

    public Membro get(String nome) {
	try {
	    String sql = "SELECT * FROM membros WHERE nome=?";
	    PreparedStatement stmt = this.connection.prepareStatement(sql);
	    stmt.setString(1, nome);
	    ResultSet rs = stmt.executeQuery();
	    Membro membro = new Membro();
	    while (rs.next()) {
		membro.setId(rs.getInt("id"));
		membro.setNome(rs.getString("nome"));
		membro.setSalario(rs.getDouble("salario"));
		CustoService.getInstance().adicionaCusto(membro.getNome(), membro.getSalario());
	    }
	    rs.close();
	    stmt.close();
	    return membro;
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}
    }
}
