package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pacote.mestrado.Membro;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class TestaClasses {
    public static void main(String[] args) {
	Connection connection = new ConnectionFactory().getConnection();
	System.out.println("Conexão aberta!");
	MembroDAO daoMemb = new MembroDAO();
	HabilidadeDAO daoHab = new HabilidadeDAO();
	Membro membro = new Membro();
	membro = daoMemb.get("liane");
	System.out.println(membro.toString());
	// Habilidades
	List<Habilidade> habilidades = new ArrayList<Habilidade>();
	habilidades = daoHab.getHabilidades(membro.getId(), "Membro");
	for (Habilidade habilidade : habilidades) {
	    System.out.println(habilidade.toString());
	}
	// Atividades
	AtividadeDAO dao2 = new AtividadeDAO();
	List<Atividade> atividades = new ArrayList<Atividade>();
	atividades = dao2.getAtividades();
	for (Atividade atividade : atividades) {
	    atividade.setRequisitosHabilidades(daoHab.getHabilidades(atividade.getId(), "Atividade"));
	    System.out.println(atividade.toString());
	}
	try {
	    connection.close();
	    System.out.println("Conexão fechada!");
	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }
}
