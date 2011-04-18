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
import pacote.mestrado.entidades.MensagemTO;
import pacote.mestrado.services.CompatibilidadeTarefaService;
import pacote.mestrado.services.ControleAtividade;
import pacote.mestrado.services.ControleMembro;

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
	ControleAtividade.getInstance().remove(membro.getAID().getLocalName());
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
		MensagemTO resposta = recebeListaAtividades();
		if(resposta.getAssunto().equals("resListaAtividadesNOT")) {
		    setTerminou(true);
		    ControleMembro.getInstance().remove(membro.getAID().getLocalName());
		    System.out.println(membro.getAID().getLocalName() + " TERMINOU POR FALTA DE TAREFAS");
		    return;
		} else {
		    lista = (List<Atividade>) resposta.getMensagem();
		}
		// escolher atividade compativel com a do membro
		Atividade atividadeEscolhida = CompatibilidadeTarefaService.selecionaAtividadeHabilidade(lista,
			membro.getHabilidades(), membro.getAtividadesInvalidas());
		if (atividadeEscolhida != null) {
		    System.out.println(membro.getAID().getLocalName() + " recebeu lista de atividades");
		    ControleMembro.getInstance().notifica(membro.getAID().getLocalName(),
			    TipoEtapaNegociacao.BUSCA_GESTOR);
		    notificaGestor(atividadeEscolhida);
		    membro.setAtividadeEscolhida(atividadeEscolhida);
		    ++passo;
		} else {
		    ControleMembro.getInstance().notifica(membro.getAID().getLocalName(),
			    TipoEtapaNegociacao.AGUARDANDO_ATIVIDADE_COMPATIVEL);
		    passo = 0;
		    System.out.println(membro.getAID().getLocalName() + " Nenhuma tarefa disponivel atualmente");
		    // aguardaBloqueado
		    membro.blockingReceive();
		}
		break;
	    case 1:
		recebeConfirmacaoAtividadeEscolhida();
		++passo;
		break;
	    default:
		setTerminou(true);
		// doDelete();
		System.out.println("Tarefa a realizar: " + membro.getAtividadeEscolhida().getNome());
	    }
	} catch (IOException e) {
	    System.out.println("Erro ao notificar o Gestor");
	    System.exit(1);
	} catch (UnreadableException e) {
	    System.out.println("Erro ao receber mensagem do gestor");
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
		// o agente, caso contrario tenta negociar;
		if (atividade.getMembroNome().equals(membro.getAID().getLocalName())) {
		    membro.setAtividadeEscolhida(atividade);
		    setTerminou(true);
		    membro.addBehaviour(new NegociantePassivoBehaviour(membro));
		} else {
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
	System.out.println(membro.getAID().getLocalName() + ": notificaGestor Atividade Escolhida: "
		+ atividadeEscolhida.getId());
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
    public MensagemTO recebeListaAtividades() throws UnreadableException {
	System.out.println(membro.getAID().getLocalName() + ": Esperando receber lista de atividades do gestor.");
	ACLMessage resposta = membro.blockingReceive();
	MensagemTO mensagem = (MensagemTO) resposta.getContentObject();

	return mensagem;

    }

    @Override
    public boolean done() {
	return terminou();
    }

    private boolean terminou() {
	return terminou;
    }

    private void setTerminou(boolean terminou) {
	this.terminou = terminou;
    }
}
