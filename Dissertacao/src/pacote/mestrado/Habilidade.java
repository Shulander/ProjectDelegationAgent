package pacote.mestrado;

import java.io.Serializable;

public class Habilidade implements Serializable
{
	private int id;
	private String tipo;
	private String nome;
	private String nivel;
	
	Habilidade (int id, String tipo, String nome, String nivel)
	{
		this.id = id;
		this.tipo = tipo;
		this.nome = nome;
		this.nivel = nivel;
	}
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getTipo() 
	{
		return tipo;
	}
	
	public void setTipo(String tipo) 
	{
		this.tipo = tipo;
	}
	
	public String getNome() 
	{
		return nome;
	}
	
	public void setNome(String nome) 
	{
		this.nome = nome;
	}
	
	public String getNivel() 
	{
		return nivel;
	}
	
	public void setNivel(String nivel) 
	{
		this.nivel = nivel;
	}
	
	public void imprime ()
	{
		System.out.println("--Habilidade--");
		System.out.println("ID: "+this.id);
		System.out.println("Nome: "+this.nome);
		System.out.println("Tipo: " + this.tipo);
		System.out.println("Nível: " + this.nivel);
		System.out.println("--------------");
	}

}
