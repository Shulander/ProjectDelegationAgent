package pacote.mestrado.behaviors;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

import pacote.mestrado.Membro;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.MensagemTO;
import pacote.mestrado.services.ControleMembro;

/**
 * Esse Behaviour se inicia quando o Agente encontrou uma tarefa para realizar,
 * por�m ja esta alocada a outro agente. Entao o agente inicia a negocia��o pela
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
//	ControleMembro.getInstance().notifica(membro.getAID().getLocalName(), TipoEtapaNegociacao.NEGOCIACAO_ATIVO);
	// Caso nao consiga o lock unico de ativo, come�a novamente.
	if(!ControleMembro.getInstance().notificaUnico(membro.getAID().getLocalName(), TipoEtapaNegociacao.NEGOCIACAO_ATIVO)) {
	    terminou = true;
	    membro.addBehaviour(new BuscaTarefaBehaviour(membro));
	} else {
	    this.membro = membro;
	    terminou = false;
	    System.out.println(membro.getAID().getLocalName() + ":trocou para o comportamento: NegocianteAtivoBehaviour");
	}
    }

    @Override
    public void action() {
	
	if(terminou) {
	    return;
	}
	
	try {
	    TipoEtapaNegociacao etapa = ControleMembro.getInstance().getEtapa(
		    membro.getAtividadeEscolhida().getMembroNome());
	    if (TipoEtapaNegociacao.NEGOCIACAO_PASSIVO.equals(etapa)) {
		solicitaTrocaAtividade();
		recebeConfirmacao();
	    } else {
		terminou = true;
		membro.addBehaviour(new BuscaTarefaBehaviour(membro));
	    }
	} catch (IOException e) {
	    System.out.println("Falha ao enviar a mensagem ao Membro");
	} catch (UnreadableException e) {
	    System.out.println("Falha ao receber resposta do Membro");
	}
    }

    private void recebeConfirmacao() throws UnreadableException {
	ACLMessage resposta = membro.blockingReceive();
	if (resposta == null) {
	    System.out.println(membro.getAID().getLocalName() + ": Erro ao receber a confirmacao da troca!");
	} else {
	    MensagemTO mensagem = (MensagemTO) resposta.getContentObject();
	    if (mensagem.getMensagem() == null) {
		membro.addBehaviour(new BuscaTarefaBehaviour(membro));
	    } else {
		Boolean trocaAceita = (Boolean) mensagem.getMensagem();
		if (trocaAceita) {
		    membro.getAtividadeEscolhida().setMembroNome(membro.getAID().getLocalName());
		    membro.addBehaviour(new NegociantePassivoBehaviour(membro));
		} else {
		    membro.getAtividadesInvalidas().add(membro.getAtividadeEscolhida());
		    membro.addBehaviour(new BuscaTarefaBehaviour(membro));
		}
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
	System.out.println(membro.getAID().getLocalName() + ": troca Atividade com: "
		+ membro.getAtividadeEscolhida().getMembroNome());

    }

    @Override
    public boolean done() {
	return terminou;
    }

}
