package pacote.mestrado;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import pacote.mestrado.dao.AtividadeDAO;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.MensagemTO;

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
	addBehaviour(new InformaTarefas());
    }

    public void inicializaListaAtividades() {
	AtividadeDAO dao = new AtividadeDAO ();
	//inicializa atividades
	listaAtividades = (ArrayList<Atividade>) dao.getAtividades ();
	for (Atividade atividade : listaAtividades) {
	    //inicializa requisitos de habilidade das atividades
	    atividade.setRequisitosHabilidades(dao.getHabilidades(atividade.getId()));
	    System.out.println(atividade.toString());
	}
    }

    private class InformaTarefas extends CyclicBehaviour {
	private static final long serialVersionUID = -1703109073768236603L;

	public void action() {
	    ACLMessage msg = blockingReceive();
	    try {
		if (msg != null) {
		    System.out.println("Gestor recebeu msg= " + msg.getContent() + " de "
			    + msg.getSender().getLocalName() + ".");
		    MensagemTO mensagem;
		    mensagem = (MensagemTO) msg.getContentObject();
		    if (mensagem.getAssunto().equals("ListaAtividades")) {
			ACLMessage resposta = msg.createReply();
			MensagemTO msgResposta = new MensagemTO();
			msgResposta.setAssunto("resListaAtividades");
			msgResposta.setMensagem(listaAtividades);
			resposta.setContentObject(msgResposta);
			System.out.println("Gestor: enviei a resposta ao " + msg.getSender().getLocalName() + ".");
			send(resposta);
		    } else if (mensagem.getAssunto().equals("notificaGestor")) {
			Atividade atividade = (Atividade) mensagem.getMensagem();
			alocaAtividadeMembro(msg.getSender().getLocalName(), atividade);
			ACLMessage resposta = msg.createReply();
			MensagemTO msgResposta = new MensagemTO();
			msgResposta.setAssunto("resNotificaGestor");
			msgResposta.setMensagem(findAtividadeById(atividade.getId()));
			resposta.setContentObject(msgResposta);
			send(resposta);
		    }
		} else {
		    // System.out.println("Gestor: Entao mensagem eh null");
		}
	    } catch (UnreadableException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    } catch (IOException e) {
		System.out.println("Gestor: Erro ao enviar listaAtividades ao " + msg.getSender().getLocalName() + "!");
		e.printStackTrace();
	    }
	}

	private boolean alocaAtividadeMembro(String nomeMembro, Atividade atividade) {

	    // ja esta alocada
	    if (hashAtividadesMembroAlocadas.containsKey(atividade.getId())
		    && !hashAtividadesMembroAlocadas.get(atividade.getId()).equals(nomeMembro)) {
		return false;
	    }
	    // nao esta alocada

	    atividade = findAtividadeById(atividade.getId());
	    atividade.setMembroNome(nomeMembro);
	    hashAtividadesMembroAlocadas.put(atividade.getId(), nomeMembro);
	    hashMembroAtividadeAlocadas.put(nomeMembro, atividade);

	    return true;
	}

	private Atividade findAtividadeById(int id) {
	    for (Atividade atividade : listaAtividades) {
		if (atividade.getId() == id) {
		    return atividade;
		}
	    }
	    return null;
	}
    }

    public ArrayList<Atividade> getListaAtividades() {
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

}
