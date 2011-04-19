package pacote.mestrado.behaviors;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.Collection;

import pacote.mestrado.Membro;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.MensagemTO;
import pacote.mestrado.services.CompatibilidadeTarefaService;
import pacote.mestrado.services.ControleGestor;
import pacote.mestrado.services.ControleMembro;

/**
 * Esse Behaviour inicia pela notificacao do gestor que algum agente escolheu
 * uma tarefa enquanto o agente atual esta exeuctando sua atividade. O agente
 * irá verificar se ele tem interesse na execuçao daquela tarefa. caso tenha,
 * tenta negociar com o agente em questão.
 * 
 * @author Shulander
 * 
 */
public class NegocianteSimulacaoBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = 6495024652042573543L;

    // Membro ao qual o Behaviour esta associado
    private Membro membro;
    private boolean terminou;
    private int passo;
    private MensagemTO mensagem;

    public NegocianteSimulacaoBehaviour(Membro membro) {
	passo = 0;
	this.membro = membro;
	terminou = false;
	mensagem = null;
    }

    @Override
    public void action() {
	try {
	    switch (passo) {
	    case 0:
		mensagem = recebeNotificacaoSelecao();
		if (mensagem != null) {
		    passo++;
		}
		break;
	    case 1:
		if (ControleMembro.getInstance().notificaUnico(membro.getAID().getLocalName() + "Sim",
			TipoEtapaNegociacao.NEGOCIACAO_ATIVO)) {
		    processaNotificacaoSelecao(mensagem);
		    ControleMembro.getInstance().remove(membro.getAID().getLocalName() + "Sim");
		    passo = 0;
		} else {
		    block(20);
		}
		break;
	    }
	} catch (IOException e) {
	    System.out.println("Falha ao enviar a mensagem ao Membro");
	} catch (UnreadableException e) {
	    System.out.println("Falha ao receber resposta do Membro");
	}

    }

    private MensagemTO recebeNotificacaoSelecao() throws UnreadableException, IOException {
	ACLMessage resposta = membro.blockingReceive(50);
	if (resposta != null) {
	    MensagemTO mensagem = (MensagemTO) resposta.getContentObject();
	    return mensagem;
	}
	return null;
    }

    private void processaNotificacaoSelecao(MensagemTO mensagem) throws UnreadableException, IOException {
	String membroNome = mensagem.getAssunto();
	if (ControleMembro.getInstance().getEtapa(membroNome).equals(TipoEtapaNegociacao.NEGOCIACAO_PASSIVO)) {
	    Collection<Atividade> atividades = (Collection<Atividade>) mensagem.getMensagem();
	    Atividade atividadeEscolhida = CompatibilidadeTarefaService.selecionaAtividadeHabilidade(atividades,
		    membro.getHabilidades(), membro.getAtividadesInvalidas());

	    // A atividade escolhida foi destinada ao membro que originou a
	    // notificadao.
	    // tentaremos negociar com ele para deixar pra mim
	    if (atividadeEscolhida != null && atividadeEscolhida.getMembroNome() != null
		    && atividadeEscolhida.getMembroNome().equals(membroNome)) {
		solicitaTrocaAtividade(atividadeEscolhida);
	    }
	}
	ControleGestor.getInstance().mutexCheck();
    }

    private void solicitaTrocaAtividade(Atividade atividadeEscolhida) throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto(membro.getAID().getLocalName());
	mensagem.setMensagem(membro.getHabilidades());
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID(atividadeEscolhida.getMembroNome(), AID.ISLOCALNAME));
	membro.send(consulta);
	System.out.println(membro.getAID().getLocalName() + ": trocaAtividade Simulcacao Membro: "
		+ atividadeEscolhida.getMembroNome());
    }

    @Override
    public boolean done() {
	return terminou;
    }

    public void setTerminou(boolean terminou) {
	this.terminou = terminou;
    }
}
