package pacote.mestrado.entidades;

import java.io.Serializable;

import pacote.mestrado.dominios.TipoHabilidade;
import pacote.mestrado.dominios.TipoNivel;

public class Habilidade implements Serializable {
	private static final long serialVersionUID = 8586683260521276748L;

	private int id;
	private TipoHabilidade tipo;
	private TipoNivel nivel;
	private Integer xp;

	public Habilidade(int id, TipoHabilidade tipo, TipoNivel nivel) {
		this.id = id;
		this.tipo = tipo;
		this.nivel = nivel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoHabilidade getTipo() {
		return tipo;
	}

	public void setTipo(TipoHabilidade tipo) {
		this.tipo = tipo;
	}

	public TipoNivel getNivel() {
		return nivel;
	}

	public void setNivel(TipoNivel nivel) {
		this.nivel = nivel;
	}

	public void setXp(Integer xp) {
		this.xp = xp;
	}

	public Integer getXp() {
		return xp;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("--Habilidade--"+"\n");
		str.append("ID: " + this.id+"\n");
		str.append("Tipo: " + this.tipo+"\n");
		str.append("Nível: " + this.nivel+"\n");
		str.append("--------------");
		return str.toString();
	}

}
