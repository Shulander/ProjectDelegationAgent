package pacote.mestrado.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Atividade implements Serializable {
	private static final long serialVersionUID = 8586977373449624267L;

	private Integer id;
	private String nome;
	private String tipo;
	private ArrayList<Integer> atividadesPredecessoras; // ids das atividades
														// que precedem a
														// atividade
	private Date dataInicial;
	private Date dataEntrega;
	private double orcamento;
	private ArrayList<Habilidade> requisitosHabilidades;
	private double duracao;
	private int estado; // -1: bloqueada, 0 - disponivel, outro numero: alocada
						// (numero corresponde ao id do membro)

	public Atividade() {
		atividadesPredecessoras = new ArrayList<Integer>();
		requisitosHabilidades = new ArrayList<Habilidade>();
		if (dataInicial != null && dataEntrega != null) {
			calculaDuracao();
		}
	}

	void calculaDuracao() {
		this.setDuracao(0); // calcular duracao
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public ArrayList<Integer> getAtividadesPredecessoras() {
		return atividadesPredecessoras;
	}

	public void setAtividadesPredecessoras(
			ArrayList<Integer> atividadesPredecessoras) {
		this.atividadesPredecessoras = atividadesPredecessoras;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public double getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(double orcamento) {
		this.orcamento = orcamento;
	}

	public ArrayList<Habilidade> getRequisitosHabilidades() {
		return requisitosHabilidades;
	}

	public void setRequisitosHabilidades(
			ArrayList<Habilidade> requisitosHabilidades) {
		this.requisitosHabilidades = requisitosHabilidades;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public void imprime() {
		System.out.println("--------------Atividade------------");
		System.out.println("ID: " + this.id);
		System.out.println("Nome: " + this.nome);
		System.out.println("Tipo: " + this.tipo);
		System.out.println("Data de início: " + this.dataInicial);
		System.out.println("Data de entrega: " + this.dataEntrega);
		System.out.println("Orçamento: " + this.orcamento);
		System.out.println("Estado: " + this.estado);
		// imprime atividades predecessoras
		if (!atividadesPredecessoras.isEmpty()) {
			System.out.println("-Atividades Predecessoras-");
			for (int idAtiv : atividadesPredecessoras) {
				System.out.println(idAtiv);
			}
			System.out.println("--------------------------");
		} else {
			System.out.println("Nao há atividades predecessoras.");
		}
		// imprime habilidades
		if (!requisitosHabilidades.isEmpty()) {
			System.out.println("-Requisitos da Atividade-");
			for (Habilidade req : requisitosHabilidades) {
				req.imprime();
			}
			System.out.println("-------------------------");
		} else {
			System.out.println("Esta atividade nao possui requisitos.");
		}

	}

	public void setDuracao(double duracao) {
		this.duracao = duracao;
	}

	public double getDuracao() {
		return duracao;
	}

}
