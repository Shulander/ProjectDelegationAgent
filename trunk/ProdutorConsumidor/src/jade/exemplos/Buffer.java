package jade.exemplos;

import java.util.Vector;
import java.util.concurrent.CyclicBarrier;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Buffer extends Agent 
{
	private Vector buffer;
	private static final int TAM_MAX = 10;
	
	protected void setup () 
	{
		String nickname = "buffer";
		AID id = new AID (nickname, AID.ISLOCALNAME);
		addBehaviour(new CyclicBehaviour(this) {						
			public void action() 
			{
				ACLMessage msg = receive ();
				if (msg != null) {
					if (msg.getContent() == "estadoBuffer") {
						enviaMensagem (getEstadoBuffer(), "produtor");
					} else {
						System.out.println ("Bufer: Erro ao receber mensagens!");
						doDelete ();
					}
				}
			}
		});
		
			
		//se buffer esta quase cheio (MAX-1), acorda produtor
		//se buffer tem pelo menos um item, acorda consumidor
	}
	
	public String getEstadoBuffer ()
	{
		if (buffer.isEmpty()) {
			return "vazio";
		} else if (buffer.size() == TAM_MAX) {
			return "cheio";
		} else if (buffer.size() == (TAM_MAX-1)) {
			return "quase_cheio";
		} else if (buffer.size() == 1) {
			return "quase_vazio";
		} else {
			return "normal";
		}
	}
	
	void enviaMensagem (String msg, String destinatario)
	{
		ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
		mensagem.setContent (msg);
		mensagem.addReceiver(new AID(destinatario, AID.ISLOCALNAME));
		send (mensagem);
	}
	
	public static int getTamMax() 
	{
		return TAM_MAX;
	}
}
