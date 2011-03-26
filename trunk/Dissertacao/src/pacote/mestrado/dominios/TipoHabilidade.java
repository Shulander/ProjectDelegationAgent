package pacote.mestrado.dominios;

public enum TipoHabilidade {
    JAVA(1, TipoArea.LINGUAGEM_PROGRAMACAO, "Java"), 
    HTML(2, TipoArea.LINGUAGEM_PROGRAMACAO, "HTML"), 
    TRABALHO_EQUIPE(3, TipoArea.SOCIAL, "Trabalho em Equipe"), 
    RELACIONAMENTO_CLIENTE(4, TipoArea.SOCIAL, "Relacionamento com cliente"), 
    GESTAO_TIME(5, TipoArea.SOCIAL, "Gestao de time"), 
    UML(6,TipoArea.MODELAGEM, "UML");

    private Integer codigo;
    private String descricao;
    private TipoArea area;

    private TipoHabilidade(Integer codigo, TipoArea area, String descricao) {
	this.codigo = codigo;
	this.area = area;
	this.descricao = descricao;
    }

    public enum TipoArea {
	LINGUAGEM_PROGRAMACAO(1, "Linguagem de Programacao"), SOCIAL(2, "Social"), MODELAGEM(3, "Modelagem");

	private Integer codigo;
	private String descricao;

	private TipoArea(Integer codigo, String descricao) {
	    this.codigo = codigo;
	    this.descricao = descricao;
	}

	public String toString() {
	    return descricao;
	}
    }

    public TipoArea getArea() {
	return area;
    }

    public TipoHabilidade obterPorCodigo(Integer codigo) {
	for (TipoHabilidade tipo : values()) {
	    if (tipo.codigo.equals(codigo)) {
		return tipo;
	    }
	}
	throw new IllegalArgumentException("O codigo informado (" + codigo
		+ ") é invalido para a obtenção do TipoHabilidade");
    }

    public String toString() {
	return descricao + " - " + area.toString();
    }
}
