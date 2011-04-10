package pacote.mestrado.behaviors;

import java.io.IOException;

import pacote.mestrado.Membro;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.ControleGestor;
import pacote.mestrado.entidades.ControleMembro;
import pacote.mestrado.entidades.MensagemTO;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class ExecucaoAtividadeBehavior extends SimpleBehaviour {
    private static final long serialVersionUID = 2605429686659053550L;

    private Membro membro;
    private boolean terminou;
    private NegocianteSimulacaoBehaviour negocianteBehavior;
    private int passo;

    public ExecucaoAtividadeBehavior(Membro membro) {
	ControleMembro.getInstance().notifica(membro.getAID().getLocalName(), TipoEtapaNegociacao.EXECUCAO_ATIVIDADE);
	this.membro = membro;
	terminou = false;
	passo = 0;
	negocianteBehavior = new NegocianteSimulacaoBehaviour(membro);
	membro.addBehaviour(negocianteBehavior);
	System.out.println(membro.getAID().getLocalName() + ":trocou para o comportamento: ExecucaoAtividadeBehavior");
    }

    @Override
    public void action() {

	if (!terminou) {
	    block(250);
	    System.out.println(membro.getAID().getLocalName() + ":passo de execucao " + passo);
	    passo++;
	    if (passo >= 10) {
		try {
		    notificaGestorTerminoTarefa();
		    terminou = true;
		} catch (IOException e) {
		    System.out.println(membro.getAID().getLocalName()
			    + ":ERRO ao comunicar o gestor do termino da tarefa");
		}
	    }
	} else {
	    block(250);
	}
    }

    private void notificaGestorTerminoTarefa() throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto("terminoAtividade");
	mensagem.setMensagem(membro.getAtividadeEscolhida());
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
	membro.send(consulta);
	System.out.println(membro.getAID().getLocalName() + ": terminoTarefa Atividade: "+ membro.getAtividadeEscolhida().getId());
    }

    @Override
    public boolean done() {
	if (terminou && ControleGestor.getInstance().mutexReady()) {
	    negocianteBehavior.setTerminou(true);
	    membro.addBehaviour(new BuscaTarefaBehaviour(membro));
	    return true;
	} else {
	    return false;
	}
    }

}
