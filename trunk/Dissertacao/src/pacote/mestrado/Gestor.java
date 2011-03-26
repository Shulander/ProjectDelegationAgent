package pacote.mestrado;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import pacote.mestrado.dominios.TipoHabilidade;
import pacote.mestrado.dominios.TipoNivel;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;
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
	// Inicializa atividade 1
	Atividade at1 = new Atividade();
	at1.setId(1);
	at1.setNome("Desenvolver modulo A1");
	at1.setTipo("Desenvolvimento");
	SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
	Date dataInicial = new Date();
	try {
	    dataInicial = formatador.parse("04/03/2011");
	} catch (ParseException e) {
	    System.out.println("Erro ao inicializar data inicial!");
	    e.printStackTrace();
	}
	at1.setDataInicial(dataInicial);
	Date dataEntrega = new Date();
	try {
	    dataEntrega = formatador.parse("05/03/2011");
	} catch (ParseException e) {
	    System.out.println("Erro ao inicializar data de entrega!");
	    e.printStackTrace();
	}
	at1.setDataEntrega(dataEntrega);
	at1.setOrcamento(305.5);
	Habilidade hab1 = new Habilidade(1, TipoHabilidade.JAVA, TipoNivel.PLENO);
	Habilidade hab2 = new Habilidade(2, TipoHabilidade.HTML, TipoNivel.PLENO);
	Habilidade hab3 = new Habilidade(3, TipoHabilidade.TRABALHO_EQUIPE, TipoNivel.JUNIOR);
	at1.getRequisitosHabilidades().add(hab1);
	at1.getRequisitosHabilidades().add(hab2);
	at1.getRequisitosHabilidades().add(hab3);
	at1.setEstado(1); // disponivel

	// Inicializa atividade 2
	Atividade at2 = new Atividade();
	at2.setId(2);
	at2.setNome("Analisar requisitos");
	at2.setTipo("Requisitos");
	Date dataInicialAt2 = new Date();
	try {
	    dataInicialAt2 = formatador.parse("04/03/2011");
	} catch (ParseException e) {
	    System.out.println("Erro ao inicializar data inicial At2!");
	    e.printStackTrace();
	}
	at2.setDataInicial(dataInicialAt2);
	Date dataEntregaAt2 = new Date();
	try {
	    dataEntregaAt2 = formatador.parse("05/03/2011");
	} catch (ParseException e) {
	    System.out.println("Erro ao inicializar data de entrega!");
	    e.printStackTrace();
	}
	at2.setDataEntrega(dataEntregaAt2);
	at2.setOrcamento(150.0);
	Habilidade hab11 = new Habilidade(1, TipoHabilidade.UML, TipoNivel.PLENO);
	Habilidade hab22 = new Habilidade(2, TipoHabilidade.RELACIONAMENTO_CLIENTE, TipoNivel.PLENO);
	Habilidade hab33 = new Habilidade(3, TipoHabilidade.GESTAO_TIME, TipoNivel.JUNIOR);
	at2.getRequisitosHabilidades().add(hab11);
	at2.getRequisitosHabilidades().add(hab22);
	at2.getRequisitosHabilidades().add(hab33);
	at2.setEstado(1); // disponivel

	// Adiciona as atividades na lista
	listaAtividades.add(at1);
	listaAtividades.add(at2);
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
