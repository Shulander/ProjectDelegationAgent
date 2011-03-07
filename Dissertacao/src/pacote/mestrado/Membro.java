package pacote.mestrado;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import pacote.mestrado.dominios.TipoNivel;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class Membro extends Agent {
	private static final long serialVersionUID = -4300677614968664782L;

	private int passo = 0;
	private int id;
	private String nome;
	private ArrayList<Habilidade> habilidades; // habilidades que a pessoa
												// possui
	private Hashtable<Date, Atividade> agenda; // tempo disponivel da pessoa
	private double salario; // homem/hora

	protected void setup() {
		System.out.println("Agente " + getAID().getLocalName() + " vivo! :)");
		inicializaMembro();
		addBehaviour(new Plano());
	}

	public void inicializaMembro() {
		this.id = 1;
		this.nome = "Joaninha";
		habilidades = new ArrayList<Habilidade>();
		habilidades.add(new Habilidade(1, "Modelagem", "UML", TipoNivel.SENIOR));
		habilidades.add(new Habilidade(2, "Social","Relacionamento com cliente", TipoNivel.SENIOR));
		habilidades.add(new Habilidade(3, "Social", "Gestao de time", TipoNivel.MASTER));
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		Date horario1 = new Date();
		Date horario2 = new Date();
		Date horario3 = new Date();
		Date horario4 = new Date();
		Date horario5 = new Date();
		Date horario6 = new Date();
		Date horario7 = new Date();
		Date horario8 = new Date();

		try {
			horario1 = formatador.parse("05/03/2011 09:00");
			horario2 = formatador.parse("05/03/2011 10:00");
			horario3 = formatador.parse("05/03/2011 11:00");
			horario4 = formatador.parse("05/03/2011 13:00");
			horario5 = formatador.parse("05/03/2011 14:00");
			horario6 = formatador.parse("05/03/2011 15:00");
			horario7 = formatador.parse("05/03/2011 16:00");
			horario8 = formatador.parse("05/03/2011 17:00");
		} catch (ParseException e) {
			System.out.println("Problema ao converter horario!");
			e.printStackTrace();
		}
		agenda = new Hashtable<Date, Atividade>();
		agenda.put(horario1, new Atividade());
		agenda.put(horario2, new Atividade());
		agenda.put(horario3, new Atividade());
		agenda.put(horario4, new Atividade());
		agenda.put(horario5, new Atividade());
		agenda.put(horario6, new Atividade());
		agenda.put(horario7, new Atividade());
		agenda.put(horario8, new Atividade());
		System.out.println(agenda);

		this.salario = 20;
	}

	private class Plano extends CyclicBehaviour {
		private static final long serialVersionUID = -8069717410691786862L;

		public void action() {
			System.out.println(getAID().getLocalName() + " - Passo: " + passo);
			List<Atividade> lista = new ArrayList<Atividade>();
			switch (passo) {
			case 0:
				solicitaListaAtividades();
				passo++;
				break;
			case 1:
				lista = recebeListaAtividades();
				if (!lista.isEmpty()) {
					System.out.println(getAID().getLocalName()
							+ " recebeu lista de atividades");
					/*
					 * System.out.println(getAID().getLocalName()+
					 * " - Lista de Atividades"); //percorre lista de atividades
					 * for (Atividade at : lista) { at.imprime (); }
					 * System.out.println
					 * ("-------------------------------------------");
					 */
				} else {
					System.out.println(getAID().getLocalName()
							+ "Lista de atividades está vazia! WTF :(");
				}
				passo++;
				break;
			case 2:
				// escolher atividade compativel com a do membro
				buscarAtividade();
			case 3:
				doDelete();
				break;
			}
		}
	}

	public void buscarAtividade() {

	}

	protected void takeDown() {
		System.out
				.println("Agente " + getAID().getLocalName() + " está morto.");
	}

	public void solicitaListaAtividades() {
		ACLMessage consulta = new ACLMessage(ACLMessage.REQUEST);
		consulta.setContent("ListaAtividades");
		consulta.addReceiver(new AID("gestor", AID.ISLOCALNAME));
		send(consulta);
		System.out.println(getAID().getLocalName()
				+ ": Enviei mensagem ao gestor.");
	}

	@SuppressWarnings("unchecked")
	public List<Atividade> recebeListaAtividades() {
		System.out.println(getAID().getLocalName()
				+ ": Esperando receber lista de atividades do gestor.");
		ACLMessage resposta = blockingReceive();
		List<Atividade> listaAtividades = new ArrayList<Atividade>();
		if (resposta == null) {
			System.out.println(getAID().getLocalName()
					+ ": Erro ao receber a lista de atividades do gestor!");
		} else {
			try {
				listaAtividades = (List<Atividade>) resposta.getContentObject();
			} catch (UnreadableException e) {
				System.out
						.println("Problema ao converter a lista de atividades");
				e.printStackTrace();
			}
		}
		return listaAtividades;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Habilidade> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(ArrayList<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
