package pacote.mestrado.behaviors;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

import pacote.mestrado.Membro;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.ControleMembro;
import pacote.mestrado.entidades.MensagemTO;

/**
 * Esse Behaviour se inicia quando o Agente encontrou uma tarefa para realizar,
 * porém ja esta alocada a outro agente. Entao o agente inicia a negociação pela
 * posse da tarefa.
 * 
 * @author Shulander
 * 
 */
public class NegocianteAtivoBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = 6495024652042573543L;

    // Membro ao qual o Behaviour esta associado
    private Membro membro;
    private boolean terminou;

    public NegocianteAtivoBehaviour(Membro membro) {
	ControleMembro.getInstance().notifica(membro.getAID().getLocalName(), TipoEtapaNegociacao.NEGOCIACAO_ATIVO);
	this.membro = membro;
	terminou = false;
    }

    @Override
    public void action() {
	System.out.println("Teste de novo behavior: NegocianteAtivoBehaviour");

	try {
	    solicitaTrocaAtividade();
	    recebeConfirmacao();
	} catch (IOException e) {
	    System.out.println("Falha ao enviar a mensagem ao Membro");
	} catch (UnreadableException e) {
	    System.out.println("Falha ao receber resposta do Membro");
	}

	terminou = true;
    }

    private void recebeConfirmacao() throws UnreadableException {
	ACLMessage resposta = membro.blockingReceive();
	if (resposta == null) {
	    System.out.println(membro.getAID().getLocalName() + ": Erro ao receber a confirmacao da troca!");
	} else {
	    MensagemTO mensagem = (MensagemTO) resposta.getContentObject();
	    Boolean trocaAceita = (Boolean) mensagem.getMensagem();
	    if(trocaAceita) {
		membro.getAtividadeEscolhida().setMembroNome(membro.getAID().getLocalName());
		membro.addBehaviour(new NegociantePassivoBehaviour(membro));
	    } else {
		membro.getAtividadesInvalidas().add(membro.getAtividadeEscolhida());
		membro.setAtividadeEscolhida(null);
		membro.addBehaviour(new BuscaTarefaBehaviour(membro));
	    }
	    terminou = true;
	}

    }

    private void solicitaTrocaAtividade() throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto(membro.getAID().getLocalName());
	mensagem.setMensagem(membro.getHabilidades());
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID(membro.getAtividadeEscolhida().getMembroNome(), AID.ISLOCALNAME));
	membro.send(consulta);
	System.out.println(membro.getAID().getLocalName() + ": Enviei mensagem ao gestor.");
    }

    @Override
    public boolean done() {
	return terminou;
    }

}
