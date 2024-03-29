package pacote.mestrado;

import jade.core.Agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

import pacote.mestrado.behaviors.InformaTarefasBehaviour;
import pacote.mestrado.controle.ControleAtividade;
import pacote.mestrado.dao.AtividadeDAO;
import pacote.mestrado.dao.HabilidadeDAO;
import pacote.mestrado.dominios.TipoEstado;
import pacote.mestrado.entidades.Atividade;

public class Gestor extends Agent {
    private static final long serialVersionUID = -7779496563622856447L;

    private HashMap<String, Atividade> hashMembroAtividadeAlocadas = new HashMap<String, Atividade>();
    private HashMap<Integer, String> hashAtividadesMembroAlocadas = new HashMap<Integer, String>();

    private ArrayList<Atividade> listaAtividades = new ArrayList<Atividade>();
    private Hashtable<Integer, Atividade> prioridadeAtividades; // par
								// prioridade e
								// atividade

    protected void setup() {
	System.out.println("Agente " + getAID().getLocalName() + " vivo! :)");
	inicializaListaAtividades();
	addBehaviour(new InformaTarefasBehaviour(this));
	
    }

    public HashMap<String, Atividade> getHashMembroAtividadeAlocadas() {
	return hashMembroAtividadeAlocadas;
    }

    public void setHashMembroAtividadeAlocadas(HashMap<String, Atividade> hashMembroAtividadeAlocadas) {
	this.hashMembroAtividadeAlocadas = hashMembroAtividadeAlocadas;
    }

    public HashMap<Integer, String> getHashAtividadesMembroAlocadas() {
	return hashAtividadesMembroAlocadas;
    }

    public void setHashAtividadesMembroAlocadas(HashMap<Integer, String> hashAtividadesMembroAlocadas) {
	this.hashAtividadesMembroAlocadas = hashAtividadesMembroAlocadas;
    }

    public void inicializaListaAtividades() {
	AtividadeDAO daoAtiv = new AtividadeDAO();
	HabilidadeDAO daoHab = new HabilidadeDAO();
	// inicializa atividades
	listaAtividades = (ArrayList<Atividade>) daoAtiv.getAtividades();
	for (Atividade atividade : listaAtividades) {
	    // inicializa requisitos de habilidade das atividades
	    atividade.setRequisitosHabilidades(daoHab.getHabilidades(atividade.getId(), "Atividade"));
	    System.out.println(atividade.toString());
	}

	ajustaDataInicioProjeto();
    }

    /**
     * Ajusta no ControleAtividade a Data de inicio da primeira atividade do
     * projeto
     */
    private void ajustaDataInicioProjeto() {
	for (Atividade atividade : getListaAtividadesDisponiveis()) {
	    if (ControleAtividade.getInstance().getDataAtual() == null
		    || ControleAtividade.getInstance().getDataAtual().after(atividade.getDataInicial())) {
		ControleAtividade.getInstance().setDataAtual(atividade.getDataInicial());
	    }
	}
    }

    public Collection<Atividade> getListaAtividades() {
	return listaAtividades;
    }

    public void setListaAtividades(ArrayList<Atividade> listaAtividades) {
	this.listaAtividades = listaAtividades;
    }

    public void setPrioridadeAtividades(Hashtable<Integer, Atividade> prioridadeAtividades) {
	this.prioridadeAtividades = prioridadeAtividades;
    }

    public Hashtable<Integer, Atividade> getPrioridadeAtividades() {
	return prioridadeAtividades;
    }

    public Collection<Atividade> getListaAtividadesDisponiveis() {
	Collection<Atividade> retorno = new LinkedList<Atividade>();
	for (Atividade atividade : getListaAtividades()) {
	    if (atividade.getEstado().equals(TipoEstado.DISPONIVEL) && preRequisitosConcluidos(atividade)) {
		retorno.add(atividade);
	    }
	}
	return retorno;
    }

    private boolean preRequisitosConcluidos(Atividade atividade) {
	if (atividade.getAtividadesPredecessoras() != null) {
	    for (Atividade ativPredecessora : atividade.getAtividadesPredecessoras()) {
		if (!ativPredecessora.getEstado().equals(TipoEstado.CONCLUIDA)) {
		    return false;
		}
	    }
	}
	return true;
    }

    public boolean verificaTodasConcluidas() {
	for (Atividade atividade : getListaAtividades()) {
	    if (!atividade.getEstado().equals(TipoEstado.CONCLUIDA)
		    && !atividade.getEstado().equals(TipoEstado.BLOQUEADA)) {
		return false;
	    }
	}
	return true;
    }

    public Atividade findAtividadeById(int id) {
	for (Atividade atividade : listaAtividades) {
	    if (atividade.getId() == id) {
		return atividade;
	    }
	}
	return null;
    }

    public boolean terminouTarefas() {
	Collection<Atividade> retorno = new LinkedList<Atividade>();
	for (Atividade atividade : getListaAtividades()) {
	    if (atividade.getEstado().equals(TipoEstado.DISPONIVEL)) {
		return false;
	    }
	}
	return true;
    }

}
