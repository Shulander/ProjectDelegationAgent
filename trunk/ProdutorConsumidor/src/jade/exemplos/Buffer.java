package jade.exemplos;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.LinkedList;
import java.util.List;

public class Buffer extends Agent 
{
	private List<String> buffer;
	private static final int TAM_MAX = 10;
	
	protected void setup () 
	{
		buffer = new LinkedList<String>();
		String nickname = "buffer";
		AID id = new AID (nickname, AID.ISLOCALNAME);
		addBehaviour(new CyclicBehaviour(this) {						
			public void action() 
			{
				ACLMessage msg = receive ();
				if (msg != null) {
					System.out.println("msg: "+msg.getContent());
					if (msg.getContent().equals("estadoBuffer")) {
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
