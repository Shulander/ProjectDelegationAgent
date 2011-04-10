package pacote.mestrado.behaviors;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pacote.mestrado.Membro;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.ControleMembro;
import pacote.mestrado.entidades.MensagemTO;
import pacote.mestrado.services.CompatibilidadeTarefaService;

/**
 * Esse agente irá iniciar a execução da primeira etapa de negociação com o
 * Gestor. Caso encontre uma tarefa que esteja disponivel, selecionará a tarefa
 * e passará para o comportamento de NegociantePassivoBehaviour caso a tarefa
 * nao esteja disponivel iniciara o comporamento de NegocianteAtivoBehaviour
 * 
 * @author Shulander
 * 
 */
public class BuscaTarefaBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = -8069717410691786862L;

    // Membro ao qual o Behaviour esta associado
    private Membro membro = null;
    private int passo;

    private boolean terminou;

    public BuscaTarefaBehaviour(Membro membro) {
	ControleMembro.getInstance().notifica(membro.getAID().getLocalName(), TipoEtapaNegociacao.BUSCA_GESTOR);
	this.membro = membro;
	membro.setAtividadeEscolhida(null);
	setTerminou(false);
	passo = 0;
	System.out.println(membro.getAID().getLocalName() + ":trocou para o comportamento: BuscaTarefaBehaviour");
    }

    public void action() {
	System.out.println(membro.getAID().getLocalName() + " - Passo: " + passo);
	List<Atividade> lista = new ArrayList<Atividade>();
	try {
	    switch (passo) {
	    case 0:
		solicitaListaAtividades();
		++passo;
		break;
	    case 1:
		lista = recebeListaAtividades();
		if (!lista.isEmpty()) {
		    System.out.println(membro.getAID().getLocalName() + " recebeu lista de atividades");
		    // escolher atividade compativel com a do membro
		    Atividade atividadeEscolhida = CompatibilidadeTarefaService.selecionaAtividadeHabilidade(lista,
			    membro.getHabilidades(), membro.getAtividadesInvalidas());
		    if (atividadeEscolhida != null) {
			notificaGestor(atividadeEscolhida);
			membro.setAtividadeEscolhida(atividadeEscolhida);
			++passo;
		    } else {
			setTerminou(true);
			passo = 0;
			System.out.println(membro.getAID().getLocalName() + "Terminou por faltar novas tarefas");
		    }
		} else {
		    System.out.println(membro.getAID().getLocalName() + "Lista de atividades está vazia! WTF :(");
		    setTerminou(true);
		    passo = 0;
		    System.out.println(membro.getAID().getLocalName() + "Terminou por faltar novas tarefas");
		}
		break;
	    case 2:
		// TODO: recebe a confirmacao do gestor da alocacao da
		recebeConfirmacaoAtividadeEscolhida();
		++passo;
		break;
	    default:
		setTerminou(true);
		membro.addBehaviour(new BTeste());
		// doDelete();
		System.out.println("Tarefa a realizar: " + membro.getAtividadeEscolhida().getNome());
	    }
	} catch (IOException e) {
	    System.out.println("Erro ao notificar o Gestor");
	    System.exit(1);
	}
    }

    private void recebeConfirmacaoAtividadeEscolhida() {
	System.out.println(membro.getAID().getLocalName()
		+ ": Recebeu a confirmacao da solicitacao da tarefa do gestor.");
	ACLMessage resposta = membro.blockingReceive();
	if (resposta != null) {
	    try {
		MensagemTO mensagem = (MensagemTO) resposta.getContentObject();
		Atividade atividade = (Atividade) mensagem.getMensagem();
		// caso a atividade enviada pelo gestor seja designada para
		// o agente, caso contrario reinicia do passo 1;
		if (atividade.getMembroNome().equals(membro.getAID().getLocalName())) {
		    membro.setAtividadeEscolhida(atividade);
		    setTerminou(true);
		    membro.addBehaviour(new NegociantePassivoBehaviour(membro));
		} else {
		    // TODO esse trexo de codigo se aplica quando
		    // invalida a atividade para uma proxima selecao
		    // membro.getAtividadesInvalidas().add(atividade);
		    // membro.setAtividadeEscolhida(null);
		    // passo = -1;
		    membro.setAtividadeEscolhida(atividade);
		    setTerminou(true);
		    membro.addBehaviour(new NegocianteAtivoBehaviour(membro));
		}
	    } catch (UnreadableException e) {
		System.out.println("Problema ao converter a lista de atividades");
		e.printStackTrace();
	    }
	} else {
	    System.out.println(membro.getAID().getLocalName() + ": Erro ao receber a lista de atividades do gestor!");
	}

    }

    private void notificaGestor(Atividade atividadeEscolhida) throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto("notificaGestor");
	mensagem.setMensagem(atividadeEscolhida);
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
	membro.send(consulta);
	System.out.println(membro.getAID().getLocalName() + ": notificaGestor Atividade Escolhida: "+ atividadeEscolhida.getId());
    }

    public void solicitaListaAtividades() throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto("ListaAtividades");
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
	membro.send(consulta);
	System.out.println(membro.getAID().getLocalName() + ": ListaAtividades.");
    }

    @SuppressWarnings("unchecked")
    public List<Atividade> recebeListaAtividades() {
	System.out.println(membro.getAID().getLocalName() + ": Esperando receber lista de atividades do gestor.");
	ACLMessage resposta = membro.blockingReceive();
	List<Atividade> listaAtividades = new ArrayList<Atividade>();
	if (resposta == null) {
	    System.out.println(membro.getAID().getLocalName() + ": Erro ao receber a lista de atividades do gestor!");
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

    @Override
    public boolean done() {
	return terminou();
    }

    private boolean terminou() {
        return terminou;
    }

    private void setTerminou(boolean terminou) {
	if(terminou) {
	    ControleMembro.getInstance().remove(membro.getAID().getLocalName());
	}
        this.terminou = terminou;
    }
}
