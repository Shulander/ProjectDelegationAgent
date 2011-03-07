package pacote.mestrado.entidades;

import java.io.Serializable;

import pacote.mestrado.dominios.TipoNivel;

public class Habilidade implements Serializable {
	private static final long serialVersionUID = 8586683260521276748L;

	private int id;
	private String tipo;
	private String nome;
	private TipoNivel nivel;

	public Habilidade(int id, String tipo, String nome, TipoNivel nivel) {
		this.id = id;
		this.tipo = tipo;
		this.nome = nome;
		this.nivel = nivel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoNivel getNivel() {
		return nivel;
	}

	public void setNivel(TipoNivel nivel) {
		this.nivel = nivel;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("--Habilidade--"+"\n");
		str.append("ID: " + this.id+"\n");
		str.append("Nome: " + this.nome+"\n");
		str.append("Tipo: " + this.tipo+"\n");
		str.append("N�vel: " + this.nivel+"\n");
		str.append("--------------");
		return str.toString();
	}

}