package pacote.mestrado.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pacote.mestrado.Membro;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class TestaClasses 
{
    public static void main(String[] args) 
    {
	Connection connection = new ConnectionFactory().getConnection();
	System.out.println("Conexão aberta!");
	MembroDAO dao = new MembroDAO();
	Membro membro = new Membro ();
	membro = dao.get("liane");
	System.out.println(membro.toString());
	//Habilidades
	List<Habilidade> habilidades = new ArrayList<Habilidade>();
	habilidades = dao.getHabilidades(membro.getId());
	for (Habilidade habilidade : habilidades) {
	    System.out.println(habilidade.toString());
	}
	//Atividades
	AtividadeDAO dao2 = new AtividadeDAO ();
	List<Atividade> atividades = new ArrayList<Atividade>();
	atividades = dao2.getAtividades ();
	for (Atividade atividade : atividades) {
	    atividade.setRequisitosHabilidades(dao2.getHabilidades(atividade.getId()));
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
