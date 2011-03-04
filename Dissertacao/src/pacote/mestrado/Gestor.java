package pacote.mestrado;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Gestor extends Agent
{
	private ArrayList<Atividade> listaAtividades = new ArrayList<Atividade>();
	private Hashtable prioridadeAtividades; //par prioridade e atividade
	String nickname = "gestor";

	protected void setup ()
	{
		AID aid = new AID (nickname, AID.ISLOCALNAME);
		System.out.println ("Agent "+getAID().getLocalName()+" in action! :)");
		Atividade at1 = new Atividade ();
		at1.setNome("Desenvolver software");
		Atividade at2 = new Atividade ();
		at2.setNome("Analisar requisitos");
		listaAtividades.add(at1);
		listaAtividades.add(at2);	
		addBehaviour (new InformaTarefas());
	}
	
	private class InformaTarefas extends CyclicBehaviour 
	{
		public void action() 
		{
			ACLMessage msg = receive ();
			if (msg != null) {
				System.out.println("Gestor recebeu msg= "+msg.getContent()+ " do membro.");
				if (msg.getContent().equals("BuscaTarefas")) {
					ACLMessage resposta = msg.createReply();
					try {
						resposta.setContentObject(listaAtividades);
					} catch (IOException e) {
						System.out.println("Gestor: Erro ao enviar listaAtividades ao membro!");
						e.printStackTrace();
					}
					send (resposta);
				} else {
					System.out.println ("Gestor: Erro ao receber mensagens!");
					//doDelete ();
				}
			} else {
				//System.out.println("Gestor: Entao mensagem eh null");
			}
		}
	}
	
	public ArrayList<Atividade> getListaAtividades() 
	{
		return listaAtividades;
	}

	public void setListaAtividades(ArrayList<Atividade> listaAtividades) 
	{
		this.listaAtividades = listaAtividades;
	}
	

}
