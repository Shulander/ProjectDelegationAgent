package pacote.mestrado.behaviors;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

import pacote.mestrado.Membro;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.MensagemTO;
import pacote.mestrado.services.ControleAtividade;
import pacote.mestrado.services.ControleGestor;
import pacote.mestrado.services.ControleMembro;

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
	    if (ControleMembro.getInstance().contaAgentesEtapa(TipoEtapaNegociacao.BUSCA_GESTOR) == 0
		    && ControleMembro.getInstance().contaAgentesEtapa(TipoEtapaNegociacao.NEGOCIACAO_ATIVO) == 0
		    && ControleMembro.getInstance().contaAgentesEtapa(TipoEtapaNegociacao.NEGOCIACAO_PASSIVO) == 0
		    && ControleAtividade.getInstance().getTerminei(membro.getAID().getLocalName())) {
		executa();
	    } else {
		block(5);
	    }
	} else {
	    block(5);
	}
    }

    private void executa() {
	System.out.println(membro.getAID().getLocalName() + ":terminei execucaoTarefa " + passo);
	calculaXPganho(membro);
	terminou = true;
    }

    private void calculaXPganho(Membro membro2) {
	// TODO Auto-generated method stub

    }

    private void notificaGestorTerminoTarefa(Atividade atividade) throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto("terminoAtividade");
	mensagem.setMensagem(atividade);
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
	membro.send(consulta);
	System.out.println(membro.getAID().getLocalName() + ": terminoTarefa Atividade: " + atividade.getId() + " "
		+ atividade.getNome());
    }

    @Override
    public boolean done() {
	if (terminou && ControleGestor.getInstance().mutexReady()) {
	    negocianteBehavior.setTerminou(true);
	    Atividade atividade = membro.getAtividadeEscolhida();
	    membro.addBehaviour(new BuscaTarefaBehaviour(membro));
	    try {
		notificaGestorTerminoTarefa(atividade);
	    } catch (IOException e) {
		System.out.println(membro.getAID().getLocalName() + ":ERRO ao comunicar o gestor do termino da tarefa");
	    }
	    return true;
	} else {
	    return false;
	}
    }

}
