package pacote.mestrado;

import java.util.ArrayList;
import java.util.Calendar;
import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Membro extends Agent 
{
	private int id;
	private String nome;
	private ArrayList<String> tiposAtividades; //tipos de atividade que tem habilidades para executar
	private ArrayList<Habilidade> habilidades; //habilidades que a pessoa possui
	private Calendar agenda; //tempo disponivel da pessoa
	private float salario; //homem/hora
	
	protected void setup ()
	{
		System.out.println ("Agent "+getAID().getLocalName()+" in action! :)");
		addBehaviour(new BuscaTarefas());
		addBehaviour(new RecebeTarefas());
	}
	
	private class BuscaTarefas extends CyclicBehaviour
	{
		public void action() 
		{
			ACLMessage pergunta = new ACLMessage(ACLMessage.REQUEST);
			pergunta.setContent ("BuscaTarefas");
			pergunta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
			send (pergunta);			
		}
		
	}
	
	private class RecebeTarefas extends CyclicBehaviour
	{
		public void action() 
		{
			//recebe a lista de tarefas
			ACLMessage resposta = receive();
			if (resposta == null) {
				System.out.println ("Membro: Erro ao receber a lista de atividades do gestor!");
			} else {
				ArrayList<Atividade> listaAtiv = new ArrayList<Atividade>();
				try {
					listaAtiv = (ArrayList<Atividade>) resposta.getContentObject();
				} catch (UnreadableException e) {
					System.out.println("Problema ao converter a lista de atividades");
					e.printStackTrace();
				}
				if (!listaAtiv.isEmpty()) {
					//percorre lista de atividades
					for (Atividade at : listaAtiv) {  
					    System.out.println (at.getNome());  
					}
				} else {
					System.out.println ("Lista de atividades está vazia! WTF :(");
				}
			}
			
		}
		
	}
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public ArrayList<String> getTiposAtividades() 
	{
		return tiposAtividades;
	}
	
	public void setTiposAtividades(ArrayList<String> tiposAtividades) 
	{
		this.tiposAtividades = tiposAtividades;
	}
	
	public ArrayList<Habilidade> getHabilidades() 
	{
		return habilidades;
	}
	
	public void setHabilidades(ArrayList<Habilidade> habilidades) 
	{
		this.habilidades = habilidades;
	}
	
	public Calendar getAgenda() 
	{
		return agenda;
	}
	
	public void setAgenda(Calendar agenda) 
	{
		this.agenda = agenda;
	}
	
	public float getSalario() 
	{
		return salario;
	}
	
	public void setSalario(float salario) 
	{
		this.salario = salario;
	}

	public String getNome() 
	{
		return nome;
	}

	public void setNome(String nome) 
	{
		this.nome = nome;
	}
}
