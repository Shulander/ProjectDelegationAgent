package jade.exemplos;

import jade.core.*;
import jade.lang.acl.ACLMessage;

public class Produtor extends Agent 
{
	protected void setup () 
	{
		String produto = produz ();
		//pergunta estadoBuffer
		enviaMensagem ("estadoBuffer");
		//recebe a resposta
		ACLMessage resposta = receive();
		if (resposta == null) {
			System.out.println ("Erro ao receber resposta do buffer!");
			doDelete ();
		} 	
		System.out.println("msg: "+resposta.getContent());		
		//se buffer esta cheio
		if (resposta.getContent().equals("buffer_cheio")) {
			doWait (); //dorme
		} else { //se nao
			//envia nova mensagem pedindo para inserir produto no buffer
			enviaMensagem (produto);
		}	
	}
	
	void enviaMensagem (String msg)
	{
		ACLMessage pergunta = new ACLMessage(ACLMessage.REQUEST);
		pergunta.setContent (msg);
		pergunta.addReceiver(new AID("buffer", AID.ISLOCALNAME));
		send (pergunta);
	}
	
	String produz ()
	{
		return String.valueOf(Math.random());
	}

}
