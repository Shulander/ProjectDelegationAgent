package pacote.mestrado;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Gestor extends Agent
{
	private ArrayList<Atividade> listaAtividades = new ArrayList<Atividade>();
	private Hashtable<Integer, Atividade> prioridadeAtividades; //par prioridade e atividade

	protected void setup ()
	{
		System.out.println ("Agente "+getAID().getLocalName()+" vivo! :)");
		inicializaListaAtividades ();		
		addBehaviour (new InformaTarefas());
	}
	
	public void inicializaListaAtividades ()
	{
		//Inicializa atividade 1
		Atividade at1 = new Atividade ();
		at1.setId(1);
		at1.setNome("Desenvolver modulo A1");
		at1.setTipo("Desenvolvimento");
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");  
		Date dataInicial = new Date ();
		try {
			dataInicial = formatador.parse("04/03/2011");
		} catch (ParseException e) {
			System.out.println("Erro ao inicializar data inicial!");
			e.printStackTrace();
		}  
		at1.setDataInicial(dataInicial);
		Date dataEntrega = new Date ();
		try {
			dataEntrega = formatador.parse("05/03/2011");
		} catch (ParseException e) {
			System.out.println("Erro ao inicializar data de entrega!");
			e.printStackTrace();
		}  
		at1.setDataEntrega(dataEntrega);
		at1.setOrcamento(305.5);
		Habilidade hab1 = new Habilidade (1, "Linguagem de Programacao", "Java", "Pleno");
		Habilidade hab2 = new Habilidade (2, "Linguagem de Programacao", "HTML", "Pleno");
		Habilidade hab3 = new Habilidade (3, "Social", "Trabalho em equipe", "Regular");
		at1.getRequisitosHabilidades().add(hab1);
		at1.getRequisitosHabilidades().add(hab2);
		at1.getRequisitosHabilidades().add(hab3);
		at1.setEstado(1); //disponivel
		
		//Inicializa atividade 2
		Atividade at2 = new Atividade ();
		at2.setId(2);
		at2.setNome("Analisar requisitos");
		at2.setTipo("Requisitos");
		Date dataInicialAt2 = new Date ();
		try {
			dataInicialAt2 = formatador.parse("04/03/2011");
		} catch (ParseException e) {
			System.out.println("Erro ao inicializar data inicial At2!");
			e.printStackTrace();
		}  
		at2.setDataInicial(dataInicialAt2);
		Date dataEntregaAt2 = new Date ();
		try {
			dataEntregaAt2 = formatador.parse("05/03/2011");
		} catch (ParseException e) {
			System.out.println("Erro ao inicializar data de entrega!");
			e.printStackTrace();
		}  
		at2.setDataEntrega(dataEntregaAt2);
		at2.setOrcamento(150);
		Habilidade hab11 = new Habilidade (1, "Modelagem", "UML", "Pleno");
		Habilidade hab22 = new Habilidade (2, "Social", "Relacionamento com cliente", "Pleno");
		Habilidade hab33 = new Habilidade (3, "Social", "Gestao de time", "Regular");
		at2.getRequisitosHabilidades().add(hab11);
		at2.getRequisitosHabilidades().add(hab22);
		at2.getRequisitosHabilidades().add(hab33);
		at2.setEstado(1); //disponivel
				
		//Adiciona as atividades na lista
		listaAtividades.add(at1);
		listaAtividades.add(at2);
	}
	
	private class InformaTarefas extends CyclicBehaviour 
	{
		public void action() 
		{
			ACLMessage msg = receive ();
			if (msg != null) {
				System.out.println("Gestor recebeu msg= "+msg.getContent()+ " de "+msg.getSender().getLocalName()+".");
				if (msg.getContent().equals("ListaAtividades")) {
					ACLMessage resposta = msg.createReply();
					try {				
						resposta.setContentObject(listaAtividades);
						System.out.println("Gestor: enviei a resposta ao "+msg.getSender().getLocalName()+".");
					} catch (IOException e) {
						System.out.println("Gestor: Erro ao enviar listaAtividades ao "+msg.getSender().getLocalName()+"!");
						e.printStackTrace();
					}
					send (resposta);
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

	public void setPrioridadeAtividades(Hashtable<Integer, Atividade> prioridadeAtividades) 
	{
		this.prioridadeAtividades = prioridadeAtividades;
	}

	public Hashtable<Integer, Atividade> getPrioridadeAtividades() 
	{
		return prioridadeAtividades;
	}
	

}
