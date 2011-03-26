package pacote.mestrado;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import pacote.mestrado.dominios.TipoHabilidade;
import pacote.mestrado.dominios.TipoNivel;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;
import pacote.mestrado.entidades.MensagemTO;
import pacote.mestrado.services.CompatibilidadeTarefaService;

public class Membro extends Agent {
    private static final long serialVersionUID = -4300677614968664782L;

    private int passo = 0;
    private int id;
    private String nome;

    private Atividade atividadeEscolhida;
    private Collection<Atividade> atividadesInvalidas;
    private Collection<Habilidade> habilidades; // habilidades que a pessoa
						// possui
    private Hashtable<Date, Atividade> agenda; // tempo disponivel da pessoa
    private double salario; // homem/hora

    protected void setup() {
	System.out.println("Agente " + getAID().getLocalName() + " vivo! :)");
	inicializaMembro();
	addBehaviour(new Plano());
    }

    public void inicializaMembro() {
	atividadesInvalidas = new LinkedList<Atividade>();
	this.id = 1;
	this.nome = "Joaninha";
	habilidades = new ArrayList<Habilidade>();
	habilidades.add(new Habilidade(1, TipoHabilidade.UML, TipoNivel.SENIOR));
	habilidades.add(new Habilidade(2, TipoHabilidade.RELACIONAMENTO_CLIENTE, TipoNivel.SENIOR));
	habilidades.add(new Habilidade(3, TipoHabilidade.GESTAO_TIME, TipoNivel.MASTER));
	SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	Date horario1 = new Date();
	Date horario2 = new Date();
	Date horario3 = new Date();
	Date horario4 = new Date();
	Date horario5 = new Date();
	Date horario6 = new Date();
	Date horario7 = new Date();
	Date horario8 = new Date();

	try {
	    horario1 = formatador.parse("05/03/2011 09:00");
	    horario2 = formatador.parse("05/03/2011 10:00");
	    horario3 = formatador.parse("05/03/2011 11:00");
	    horario4 = formatador.parse("05/03/2011 13:00");
	    horario5 = formatador.parse("05/03/2011 14:00");
	    horario6 = formatador.parse("05/03/2011 15:00");
	    horario7 = formatador.parse("05/03/2011 16:00");
	    horario8 = formatador.parse("05/03/2011 17:00");
	} catch (ParseException e) {
	    System.out.println("Problema ao converter horario!");
	    e.printStackTrace();
	}
	agenda = new Hashtable<Date, Atividade>();
	agenda.put(horario1, new Atividade());
	agenda.put(horario2, new Atividade());
	agenda.put(horario3, new Atividade());
	agenda.put(horario4, new Atividade());
	agenda.put(horario5, new Atividade());
	agenda.put(horario6, new Atividade());
	agenda.put(horario7, new Atividade());
	agenda.put(horario8, new Atividade());
	System.out.println(agenda);

	this.salario = 20;
    }

    private class Plano extends CyclicBehaviour {
	private static final long serialVersionUID = -8069717410691786862L;

	public void action() {
	    System.out.println(getAID().getLocalName() + " - Passo: " + passo);
	    List<Atividade> lista = new ArrayList<Atividade>();
	    try {
		switch (passo) {
		case 0:
		    solicitaListaAtividades();
		    break;
		case 1:
		    lista = recebeListaAtividades();
		    if (!lista.isEmpty()) {
			System.out.println(getAID().getLocalName() + " recebeu lista de atividades");
			// escolher atividade compativel com a do membro
			atividadeEscolhida = CompatibilidadeTarefaService.selecionaAtividadeHabilidade(lista,
				getHabilidades(), atividadesInvalidas);

			notificaGestor(atividadeEscolhida);

			/*
			 * System.out.println(getAID().getLocalName()+
			 * " - Lista de Atividades"); //percorre lista de
			 * atividades for (Atividade at : lista) { at.imprime
			 * (); } System.out.println
			 * ("-------------------------------------------");
			 */
		    } else {
			System.out.println(getAID().getLocalName() + "Lista de atividades está vazia! WTF :(");
		    }
		    break;
		case 2:
		    // TODO: recebe a confirmacao do gestor da alocacao da
		    recebeConfirmacaoAtividadeEscolhida();
		    break;
		default:
		    doDelete();
		}
	    } catch (IOException e) {
		System.out.println("Erro ao notificar o Gestor");
		System.exit(1);
	    }
	    ++passo;
	}

	private void recebeConfirmacaoAtividadeEscolhida() {
	    System.out.println(getAID().getLocalName() + ": Recebeu a confirmacao da solicitacao da tarefa do gestor.");
	    ACLMessage resposta = blockingReceive();
	    if (resposta != null) {
		try {
		    MensagemTO mensagem = (MensagemTO) resposta.getContentObject();
		    Atividade atividade = (Atividade) mensagem.getMensagem();
		    // caso a atividade enviada pelo gestor seja designada para o agente,
		    // caso contrario reinicia do passo 1;
		    if(atividade.getMembroNome().equals(getAID().getLocalName())) {
			atividadeEscolhida = atividade;
		    } else {
			//invalida a atividade para uma proxima selecao
			atividadesInvalidas.add(atividade);
			atividadeEscolhida = null;
			passo = -1;
		    }
		} catch (UnreadableException e) {
		    System.out.println("Problema ao converter a lista de atividades");
		    e.printStackTrace();
		}
	    } else {
		System.out.println(getAID().getLocalName() + ": Erro ao receber a lista de atividades do gestor!");
	    }

	}

	private void notificaGestor(Atividade atividadeEscolhida) throws IOException {
	    MensagemTO mensagem = new MensagemTO();
	    ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	    mensagem.setAssunto("notificaGestor");
	    mensagem.setMensagem(atividadeEscolhida);
	    consulta.setContentObject(mensagem);
	    consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
	    send(consulta);
	    System.out.println(getAID().getLocalName() + ": Enviei mensagem ao gestor.");
	}
    }

    public void buscarAtividade() {

    }

    protected void takeDown() {
	System.out.println("Agente " + getAID().getLocalName() + " está morto.");
    }

    public void solicitaListaAtividades() throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto("ListaAtividades");
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
	send(consulta);
	System.out.println(getAID().getLocalName() + ": Enviei mensagem ao gestor.");
    }

    @SuppressWarnings("unchecked")
    public List<Atividade> recebeListaAtividades() {
	System.out.println(getAID().getLocalName() + ": Esperando receber lista de atividades do gestor.");
	ACLMessage resposta = blockingReceive();
	List<Atividade> listaAtividades = new ArrayList<Atividade>();
	if (resposta == null) {
	    System.out.println(getAID().getLocalName() + ": Erro ao receber a lista de atividades do gestor!");
	} else {
	    try {
		MensagemTO mensagem = (MensagemTO) resposta.getContentObject();
		listaAtividades = (List<Atividade>) mensagem.getMensagem();
	    } catch (UnreadableException e) {
		System.out.println("Problema ao converter a lista de atividades");
		e.printStackTrace();
	    }
	}
	return listaAtividades;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public Collection<Habilidade> getHabilidades() {
	return habilidades;
    }

    public void setHabilidades(Collection<Habilidade> habilidades) {
	this.habilidades = habilidades;
    }

    public double getSalario() {
	return salario;
    }

    public void setSalario(double salario) {
	this.salario = salario;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

}
