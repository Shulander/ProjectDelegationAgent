package pacote.mestrado.behaviors;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

import pacote.mestrado.Gestor;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.ControleMembro;
import pacote.mestrado.entidades.MensagemTO;

public class InformaTarefasBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = -1703109073768236603L;

    // Gestor ao qual o Behaviour esta associado
    private Gestor gestor;

    public InformaTarefasBehaviour(Gestor gestor) {
	this.gestor = gestor;
    }

    public void action() {
	ACLMessage msg = gestor.blockingReceive();
	try {
	    if (msg != null) {
		MensagemTO mensagem;
		mensagem = (MensagemTO) msg.getContentObject();
		System.out.println("Gestor recebeu msg= " + mensagem + " de " + msg.getSender().getLocalName() + ".");
		if (mensagem.getAssunto().equals("ListaAtividades")) {
		    ACLMessage resposta = msg.createReply();
		    MensagemTO msgResposta = new MensagemTO();
		    msgResposta.setAssunto("resListaAtividades");
		    msgResposta.setMensagem(gestor.getListaAtividadesDisponiveis());
		    resposta.setContentObject(msgResposta);
		    System.out.println("Gestor: enviei a resposta ao " + msg.getSender().getLocalName() + ".");
		    gestor.send(resposta);
		} else if (mensagem.getAssunto().equals("notificaGestor")) {
		    Atividade atividade = (Atividade) mensagem.getMensagem();
		    boolean alocado = alocaAtividadeMembro(msg.getSender().getLocalName(), atividade);
		    if(alocado) {
			broadcastEscolha(msg.getSender().getLocalName(), atividade);
		    }
		    ACLMessage resposta = msg.createReply();
		    MensagemTO msgResposta = new MensagemTO();
		    msgResposta.setAssunto("resNotificaGestor");
		    msgResposta.setMensagem(gestor.findAtividadeById(atividade.getId()));
		    resposta.setContentObject(msgResposta);
		    gestor.send(resposta);
		} else if(mensagem.getAssunto().equals("trocaAtividade")) {
		    Atividade atividade = (Atividade) mensagem.getMensagem();
		    String alocarPara = atividade.getMembroNome();
		    // desaloca a atividade de quem enviou a requisicao
		    desalocaAtividadeMembro(msg.getSender().getLocalName(), atividade);
		    // aloca para o novo membro
		    alocaAtividadeMembro(alocarPara, atividade);
		}
	    } else {
		// System.out.println("Gestor: Entao mensagem eh null");
	    }
	} catch (UnreadableException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e) {
	    System.out.println("Gestor: Erro ao enviar listaAtividades ao " + msg.getSender().getLocalName() + "!");
	    e.printStackTrace();
	}
    }

    private void broadcastEscolha(String localName, Atividade atividade) {
	// TODO Auto-generated method stub
	
    }

    private void desalocaAtividadeMembro(String nomeMembro, Atividade atividade) {
	// verifica se a tarefa esta alocada para o membro
	if (gestor.getHashAtividadesMembroAlocadas().containsKey(atividade.getId())
		&& !gestor.getHashAtividadesMembroAlocadas().get(atividade.getId()).equals(nomeMembro)) {
	    // remove das hashs
	    gestor.getHashAtividadesMembroAlocadas().remove(atividade.getId());
	    gestor.getHashMembroAtividadeAlocadas().remove(nomeMembro);
	}
    }

    private boolean alocaAtividadeMembro(String nomeMembro, Atividade atividade) {

	// ja esta alocada
	if (gestor.getHashAtividadesMembroAlocadas().containsKey(atividade.getId())
		&& !gestor.getHashAtividadesMembroAlocadas().get(atividade.getId()).equals(nomeMembro)) {
	    return false;
	}
	
	// caso o membro esteja em execucao nao pode alocar uma tarefa para ele
	if(ControleMembro.getInstance().getEtapa(nomeMembro).equals(TipoEtapaNegociacao.EXECUCAO_ATIVIDADE)) {
	    return false;
	}
	
	// nao esta alocada

	atividade = gestor.findAtividadeById(atividade.getId());
	atividade.setMembroNome(nomeMembro);
	gestor.getHashAtividadesMembroAlocadas().put(atividade.getId(), nomeMembro);
	gestor.getHashMembroAtividadeAlocadas().put(nomeMembro, atividade);

	return true;
    }
}