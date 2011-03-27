package pacote.mestrado.entidades;

import java.io.Serializable;

import pacote.mestrado.dominios.TipoNivel;

public class Habilidade implements Serializable 
{
    private static final long serialVersionUID = 8586683260521276748L;

    private int id;
    private String area;
    private String nome;
    private TipoNivel nivel;
    private Integer xp;
  
    public String toString() 
    {
	StringBuilder str = new StringBuilder();
	str.append("--Habilidade--" + "\n");
	str.append("ID: " + this.id + "\n");
	str.append("Area: " + this.area + "\n");
	str.append("Nome: " + this.nome + "\n");
	str.append("Nível: " + this.nivel + "\n");
	str.append("Experiencia: " + this.xp + "\n");
	return str.toString();
    }

    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }


    public String getArea() 
    {
        return area;
    }


    public void setArea(String area) 
    {
        this.area = area;
    }


    public String getNome() 
    {
        return nome;
    }


    public void setNome(String nome) 
    {
        this.nome = nome;
    }


    public TipoNivel getNivel() 
    {
        return nivel;
    }


    public void setNivel(TipoNivel tipoNivel) 
    {
        this.nivel = tipoNivel;
    }


    public Integer getXp() 
    {
        return xp;
    }


    public void setXp(Integer xp) 
    {
        this.xp = xp;
    }

}
