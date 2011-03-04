package pacote.mestrado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Atividade implements Serializable
{
	private int id;
	private String nome;
	private String tipo;
	private ArrayList atividadesPredecessoras; //ids das atividades que precedem a atividade
	private Date dataInicial;
	private Date dataEntrega;
	private float orcamento;
	private ArrayList<Habilidade> requisitosHabilidades;
	private int estado; //-1: bloqueada, 0 - disponivel, outro numero: alocada (numero corresponde ao id do membro)
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getNome() 
	{
		return nome;
	}
	
	public void setNome(String nome) 
	{
		this.nome = nome;
	}
	
	public String getTipo() 
	{
		return tipo;
	}
	
	public void setTipo(String tipo) 
	{
		this.tipo = tipo;
	}
	
	public ArrayList getAtividadesPredecessoras() 
	{
		return atividadesPredecessoras;
	}
	
	public void setAtividadesPredecessoras(ArrayList atividadesPredecessoras) 
	{
		this.atividadesPredecessoras = atividadesPredecessoras;
	}
	
	public Date getDataInicial() 
	{
		return dataInicial;
	}
	
	public void setDataInicial(Date dataInicial) 
	{
		this.dataInicial = dataInicial;
	}
	
	public Date getDataEntrega() 
	{
		return dataEntrega;
	}
	
	public void setDataEntrega(Date dataEntrega) 
	{
		this.dataEntrega = dataEntrega;
	}
	
	public float getOrcamento() 
	{
		return orcamento;
	}
	
	public void setOrcamento(float orcamento) 
	{
		this.orcamento = orcamento;
	}
	
	public ArrayList<Habilidade> getRequisitosHabilidades() 
	{
		return requisitosHabilidades;
	}
	
	public void setRequisitosHabilidades(ArrayList<Habilidade> requisitosHabilidades) 
	{
		this.requisitosHabilidades = requisitosHabilidades;
	}

	public int getEstado() 
	{
		return estado;
	}

	public void setEstado(int estado) 
	{
		this.estado = estado;
	}

}
