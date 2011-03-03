package jade.exemplos;

import jade.core.*;
import jade.lang.acl.ACLMessage;

public class Consumidor extends Agent
{
	protected void setup () 
	{
		//pergunta estado do buffer
		enviaMensagem ("estadoBuffer");
		//recebe a resposta
		ACLMessage resposta = receive();
		if (resposta == null) {
			System.out.println ("Erro ao receber resposta do buffer!");
			doDelete ();
		} 				
		//se buffer vazio
		if (resposta.getContent() == "vazio") {
			doWait (); //dorme
		} else { //se nao
			//envia nova mensagem pedindo para retirar produto buffer
			enviaMensagem ("consome");
		}
	}
	
	void enviaMensagem (String msg)
	{
		ACLMessage pergunta = new ACLMessage(ACLMessage.REQUEST);
		pergunta.setContent (msg);
		pergunta.addReceiver(new AID("buffer", AID.ISLOCALNAME));
		send (pergunta);
	}

}
