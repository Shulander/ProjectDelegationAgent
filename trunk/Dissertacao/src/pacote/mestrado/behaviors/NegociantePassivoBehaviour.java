package pacote.mestrado.behaviors;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.Collection;

import pacote.mestrado.Membro;
import pacote.mestrado.controle.ControleAtividade;
import pacote.mestrado.controle.ControleGestor;
import pacote.mestrado.controle.ControleMembro;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.Habilidade;
import pacote.mestrado.entidades.MensagemTO;
import pacote.mestrado.services.TempoExecucaoService;

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
	ControleMembro.getInstance().notifica(membro.getAID().getLocalName(), TipoEtapaNegociacao.NEGOCIACAO_PASSIVO);
	ControleAtividade.getInstance().adiciona(
		membro.getAID().getLocalName(),
		TempoExecucaoService.calculcaDataEntrega(membro.getAID().getLocalName(),
			membro.getAtividadeEscolhida(), membro.getHabilidades()));
	this.membro = membro;
	terminou = false;
	System.out.println(membro.getAID().getLocalName() + ":trocou para o comportamento: NegociantePassivoBehaviour");
    }

    @Override
    public void action() {
	try {
	    recebeTrocaAtividade();
	} catch (IOException e) {
	    System.out.println("Falha ao enviar a mensagem ao Membro");
	} catch (UnreadableException e) {
	    System.out.println("Falha ao receber resposta do Membro");
	}

    }

    private void recebeTrocaAtividade() throws IOException, UnreadableException {
	ACLMessage msg = membro.blockingReceive(100);
	if (msg != null) {
	    MensagemTO mensagem = (MensagemTO) msg.getContentObject();

	    String nomeRequisicao = mensagem.getAssunto();
	    Collection<Habilidade> habilidadesRequisicao = (Collection<Habilidade>) mensagem.getMensagem();

	    boolean trocaAceita = membro.getEscolhaTarefaService().verificaHabilidadesBMelhorHabilidadesA(
		    membro.getAtividadeEscolhida(), membro.getAID().getLocalName(), membro.getHabilidades(),
		    nomeRequisicao, habilidadesRequisicao);

	    // double minhaPontuacao =
	    // CompatibilidadeTarefaService.calculaGrauCompatibilidade(
	    // membro.getAtividadeEscolhida(), membro.getHabilidades());
	    // double pontuacaoRequisicao =
	    // CompatibilidadeTarefaService.calculaGrauCompatibilidade(
	    // membro.getAtividadeEscolhida(), habilidadesRequisicao);
	    //
	    // boolean trocaAceita = minhaPontuacao < pontuacaoRequisicao;
	    // if (trocaAceita) {
	    // Date minhaDataTermino =
	    // TempoExecucaoService.calculcaDataEntrega(membro.getAID().getLocalName(),
	    // membro.getAtividadeEscolhida(), membro.getHabilidades());
	    // Date dataRequisicao =
	    // TempoExecucaoService.calculcaDataEntrega(nomeRequisicao,
	    // membro.getAtividadeEscolhida(), habilidadesRequisicao);
	    // trocaAceita = minhaDataTermino.after(dataRequisicao);
	    // }
	    // caso a Troca foi aceita, troca o dono da atividade para o nome
	    // requisitado. nofica o gestor da troca e recomeça a procura
	    if (trocaAceita) {
		ControleAtividade.getInstance().remove(membro.getAID().getLocalName());
		membro.getAtividadeEscolhida().setMembroNome(nomeRequisicao);
		notificaGestor();
		membro.getAtividadesInvalidas().add(membro.getAtividadeEscolhida());
		membro.setAtividadeEscolhida(null);
		membro.addBehaviour(new BuscaTarefaBehaviour(membro));
		terminou = true;
	    }
	    // envia resposta
	    enviaConfirmacao(msg, trocaAceita);
	} else {
	    if (ControleMembro.getInstance().contaAgentesEtapa(TipoEtapaNegociacao.BUSCA_GESTOR) == 0
		    && ControleMembro.getInstance().contaAgentesEtapa(TipoEtapaNegociacao.NEGOCIACAO_ATIVO) == 0
		    && ControleGestor.getInstance().mutexReady()) {
		terminou = true;
		notificaGestorInicioTarefa();
		membro.addBehaviour(new ExecucaoAtividadeBehavior(membro));
	    } else {
		System.out.println(membro.getAID().getLocalName()
			+ ":ALGUEM ESTA FAZENDO MERDA... NAO TA DEIXANDO EU EXECUTAR A TAREFA: "
			+ ControleMembro.getInstance().contaAgentesEtapa(TipoEtapaNegociacao.BUSCA_GESTOR) + " - "
			+ ControleMembro.getInstance().contaAgentesEtapa(TipoEtapaNegociacao.NEGOCIACAO_ATIVO) + " - "
			+ ControleGestor.getInstance().mutexReady());
		System.out.println(ControleMembro.getInstance().toString());
	    }
	}
    }

    private void notificaGestor() throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto("trocaAtividade");
	mensagem.setMensagem(membro.getAtividadeEscolhida());
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
	membro.send(consulta);
	System.out.println(membro.getAID().getLocalName() + ": trocaAtividade notifica gestor Atividade: "
		+ membro.getAtividadeEscolhida().getId());
    }

    private void notificaGestorInicioTarefa() throws IOException {
	MensagemTO mensagem = new MensagemTO();
	ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
	mensagem.setAssunto("inicioAtividade");
	mensagem.setMensagem(membro.getAtividadeEscolhida());
	consulta.setContentObject(mensagem);
	consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
	membro.send(consulta);
	System.out.println(membro.getAID().getLocalName() + ": trocaAtividade gestor Atividade: "
		+ membro.getAtividadeEscolhida().getId());
    }

    private void enviaConfirmacao(ACLMessage msg, boolean trocaAceita) throws IOException {
	if (ControleMembro.getInstance().getEtapa(msg.getSender().getLocalName())
		.equals(TipoEtapaNegociacao.NEGOCIACAO_ATIVO)) {
	    ACLMessage resposta = msg.createReply();
	    MensagemTO msgResposta = new MensagemTO();
	    msgResposta.setAssunto("trocaAceita");
	    msgResposta.setMensagem(trocaAceita);
	    resposta.setContentObject(msgResposta);
	    System.out.println("Membro: enviei a resposta ao " + msg.getSender().getLocalName() + ".");
	    membro.send(resposta);
	}
    }

    @Override
    public boolean done() {
	return terminou;
    }

}
