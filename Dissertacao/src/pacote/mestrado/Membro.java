package pacote.mestrado;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pacote.mestrado.dao.MembroDAO;
import pacote.mestrado.dao.HabilidadeDAO;
import pacote.mestrado.entidades.Agenda;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;
import pacote.mestrado.entidades.MensagemTO;
import pacote.mestrado.services.CompatibilidadeTarefaService;

/**
 * Classe que corresponde ao agente membro de um projeto
 * @author Liane Cafarate
 */
public class Membro extends Agent 
{
    private static final long serialVersionUID = -4300677614968664782L;

    private int passo = 0;
    private int id;
    private String nome;
    private double salario; // homem/hora 
    private Collection<Habilidade> habilidades; // habilidades que a pessoa possui
    private Agenda agenda; // tempo disponivel da pessoa    
    private Atividade atividadeEscolhida;
    private Collection<Atividade> atividadesInvalidas; //atividades que o agente quis, mas por algum motivo nao pode ter e devem ser descartadas

    /**
     * Metodo de inicializacao de agentes
     */
    protected void setup() 
    {
	System.out.println("Agente " + getAID().getLocalName() + " vivo! :)");
	inicializaMembro ();
	addBehaviour(new Plano());
    }
    
    /**
     * Metodo de finalizacao de agentes
     */
    protected void takeDown() 
    {
	System.out.println("Agente " + getAID().getLocalName() + " está morto.");
    }

    /**
     * Inicializa o agente com os dados cadastrados no banco de dados.
     */
    private void inicializaMembro ()
    {
	MembroDAO daoMemb = new MembroDAO ();
	Membro temp = daoMemb.get(getLocalName());
	//Inicializa dados do agente
	this.id = temp.getId();
	this.nome = temp.getNome();
	this.salario = temp.getSalario();
	//Inicializa habilidades
	HabilidadeDAO daoHab = new HabilidadeDAO ();
	habilidades = daoHab.getHabilidades(this.id, "Membro");
	/*System.out.println(toString());
	for (Habilidade habilidade : habilidades) {
	    System.out.println(habilidade.toString());
	}*/
    }
    
    private class Plano extends CyclicBehaviour 
    {
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

	private void recebeConfirmacaoAtividadeEscolhida() 
	{
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

	private void notificaGestor(Atividade atividadeEscolhida) throws IOException 
	{
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

    public void buscarAtividade() 
    {

    }



    public void solicitaListaAtividades() throws IOException 
    {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto("ListaAtividades");
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
	send(consulta);
	System.out.println(getAID().getLocalName() + ": Enviei mensagem ao gestor.");
    }

    @SuppressWarnings("unchecked")
    public List<Atividade> recebeListaAtividades() 
    {
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

    public int getId() 
    {
	return id;
    }

    public void setId(int id) 
    {
	this.id = id;
    }

    public Collection<Habilidade> getHabilidades() 
    {
	return habilidades;
    }

    public void setHabilidades(Collection<Habilidade> habilidades) 
    {
	this.habilidades = habilidades;
    }

    public double getSalario() 
    {
	return salario;
    }

    public void setSalario(double salario) 
    {
	this.salario = salario;
    }

    public String getNome() 
    {
	return nome;
    }

    public void setNome(String nome) 
    {
	this.nome = nome;
    }
    
    public String toString ()
    {
	StringBuilder str = new StringBuilder();
	str.append("--Agente--" + "\n");
	str.append("ID: " + this.id + "\n");
	str.append("Nome: " + this.nome + "\n");
	str.append("Salario: " + this.salario + "\n");	
	return str.toString();
    }

}
