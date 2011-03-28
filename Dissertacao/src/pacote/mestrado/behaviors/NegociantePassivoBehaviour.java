package pacote.mestrado.behaviors;

import java.io.IOException;
import java.util.Collection;

import pacote.mestrado.Membro;
import pacote.mestrado.entidades.Habilidade;
import pacote.mestrado.entidades.MensagemTO;
import pacote.mestrado.services.CompatibilidadeTarefaService;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * Esse Behaviour se inicia quando o Agente Membro ja encontrou uma tarefa. Ele
 * dará a oportunidade a outro agente de negociar com ele pela prioridade da
 * tarefa, por esse motivo se denomina Agente Passivo.
 * 
 * @author Shulander
 */
public class NegociantePassivoBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = 6495024652042573543L;

    // Membro ao qual o Behaviour esta associado
    private Membro membro;
    private boolean terminou;

    public NegociantePassivoBehaviour(Membro membro) {
	this.membro = membro;
	terminou = false;
    }

    @Override
    public void action() {
	System.out.println("Teste de novo behavior: NegociantePassivoBehaviour");
	try {
	    recebeTrocaAtividade();
	} catch (IOException e) {
	    System.out.println("Falha ao enviar a mensagem ao Membro");
	} catch (UnreadableException e) {
	    System.out.println("Falha ao receber resposta do Membro");
	}

	terminou = true;
    }

    private void recebeTrocaAtividade() throws IOException, UnreadableException {
	ACLMessage msg = membro.blockingReceive(100);
	if (msg != null) {
	    MensagemTO mensagem = (MensagemTO) msg.getContentObject();

	    String nomeRequisicao = mensagem.getAssunto();
	    Collection<Habilidade> habilidadesRequisicao = (Collection<Habilidade>) mensagem.getMensagem();

	    double minhaPontuacao = CompatibilidadeTarefaService.calculaGrauCompatibilidade(
		    membro.getAtividadeEscolhida(), membro.getHabilidades());
	    double pontuacaoRequisicao = CompatibilidadeTarefaService.calculaGrauCompatibilidade(
		    membro.getAtividadeEscolhida(), habilidadesRequisicao);

	    boolean trocaAceita = minhaPontuacao < pontuacaoRequisicao;

	    // caso a Troca foi aceita, troca o dono da atividade para o nome
	    // requisitado. nofica o gestor da troca e recomeça a procura
	    if (trocaAceita) {
		membro.getAtividadeEscolhida().setMembroNome(nomeRequisicao);
		notificaGestor();
		membro.setAtividadeEscolhida(null);
		membro.addBehaviour(new BuscaTarefaBehaviour(membro));
		terminou = true;
	    }
	    // envia resposta
	    enviaConfirmacao(msg, trocaAceita);
	}

    }

    private void notificaGestor() throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto("trocaAtividade");
	mensagem.setMensagem(membro.getAtividadeEscolhida());
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID(membro.getAtividadeEscolhida().getMembroNome(), AID.ISLOCALNAME));
	membro.send(consulta);
	System.out.println(membro.getAID().getLocalName() + ": Enviei mensagem ao gestor.");
    }

    private void enviaConfirmacao(ACLMessage msg, boolean trocaAceita) throws IOException {
	ACLMessage resposta = msg.createReply();
	MensagemTO msgResposta = new MensagemTO();
	msgResposta.setAssunto("trocaAceita");
	msgResposta.setMensagem(trocaAceita);
	resposta.setContentObject(msgResposta);
	System.out.println("Membro: enviei a resposta ao " + msg.getSender().getLocalName() + ".");
	membro.send(resposta);
    }

    @Override
    public boolean done() {
	return terminou;
    }

}
