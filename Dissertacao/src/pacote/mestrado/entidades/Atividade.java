package pacote.mestrado.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import pacote.mestrado.dominios.TipoEstado;

public class Atividade implements Serializable {
    private static final long serialVersionUID = 8586977373449624267L;

    private Integer id;
    private String nome;
    private String tipo;
    private Collection<Atividade> atividadesPredecessoras; // ids das atividades
							   // que precedem a
							   // atividade
    private Date dataInicial;
    private Date dataEntrega;
    // private Periodo periodoExecucao;
    private Date dataInicioExecucao;
    private Date dataTerminoExecucao;
    private Collection<Habilidade> requisitosHabilidades;
    private TipoEstado estado; // bloqueada - disponivel - alocada

    private String membroNome;

    public Atividade() {
	atividadesPredecessoras = new ArrayList<Atividade>();
	requisitosHabilidades = new ArrayList<Habilidade>();
	// if (dataInicial != null && dataEntrega != null) {
	// calculaDuracao();
	// }
    }

    // void calculaDuracao() {
    // this.setDuracao(0.0); // calcular duracao
    // }

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

    public Collection<Atividade> getAtividadesPredecessoras() {
	return atividadesPredecessoras;
    }

    public void setAtividadesPredecessoras(ArrayList<Atividade> atividadesPredecessoras) {
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

    public Collection<Habilidade> getRequisitosHabilidades() {
	return requisitosHabilidades;
    }

    public void setRequisitosHabilidades(Collection<Habilidade> requisitosHabilidades) {
	this.requisitosHabilidades = requisitosHabilidades;
    }

    public TipoEstado getEstado() {
	return estado;
    }

    public void setEstado(TipoEstado estado) {
	this.estado = estado;
    }

    public String toString() {

	StringBuilder str = new StringBuilder();

	str.append("--------------Atividade------------" + "\n");
	str.append("ID: " + this.id + "\n");
	str.append("Nome: " + this.nome + "\n");
	str.append("Tipo: " + this.tipo + "\n");
	// if(this.periodoExecucao != null) {
	// str.append("Data de in�cio: " + this.periodoExecucao.getInicio() +
	// "\n");
	// str.append("Data de entrega: " + this.periodoExecucao.getFim() +
	// "\n");
	// }
	str.append("Data de in�cio: " + this.dataInicial + "\n");
	str.append("Data de entrega: " + this.dataEntrega + "\n");
	str.append("Estado: " + this.estado + "\n");
	// imprime atividades predecessoras
	if (!atividadesPredecessoras.isEmpty()) {
	    str.append("-Atividades Predecessoras-" + "\n");
	    for (Atividade ativ : atividadesPredecessoras) {
		str.append(ativ.getId() + "\n");
	    }
	    str.append("--------------------------" + "\n");
	} else {
	    str.append("Nao h� atividades predecessoras." + "\n");
	}
	// imprime habilidades
	if (!requisitosHabilidades.isEmpty()) {
	    str.append("-Requisitos da Atividade-" + "\n");
	    for (Habilidade req : requisitosHabilidades) {
		str.append(req.toString());
	    }
	} else {
	    str.append("Esta atividade nao possui requisitos." + "\n");
	}
	return str.toString();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Atividade other = (Atividade) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    public String getMembroNome() {
	return membroNome;
    }

    public void setMembroNome(String membroNome) {
	this.membroNome = membroNome;
    }

    public Date getDataInicioExecucao() {
	return dataInicioExecucao;
    }

    public void setDataInicioExecucao(Date dataInicioExecucao) {
	this.dataInicioExecucao = dataInicioExecucao;
    }

    public Date getDataTerminoExecucao() {
	return dataTerminoExecucao;
    }

    public void setDataTerminoExecucao(Date dataTerminoExecucao) {
	this.dataTerminoExecucao = dataTerminoExecucao;
    }

}
