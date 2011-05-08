package pacote.mestrado.dominios;

public enum TipoNivel {
    JUNIOR(1, "Junior"), PLENO(2, "Pleno"), SENIOR(3, "Sênior"), MASTER(4, "Master");

    private Integer codigo;
    private String descricao;

    private TipoNivel(Integer codigo, String descricao) {
	this.codigo = codigo;
	this.descricao = descricao;
    }

    public static TipoNivel obterPorCodigo(Integer codigo) {
	for (TipoNivel tipo : values()) {
	    if (tipo.codigo.equals(codigo)) {
		return tipo;
	    }
	}
	throw new IllegalArgumentException("O codigo informado (" + codigo
		+ ") é invalido para a obtenção do TipoNivel");
    }

    public String toString() {
	return descricao;
    }

    public Integer getCodigo() {
	return codigo;
    }

    public boolean before(TipoNivel tipo) {
	return getCodigo() < tipo.getCodigo();
    }

    public boolean after(TipoNivel tipo) {
	return getCodigo() > tipo.getCodigo();
    }

    public Integer diferencaNiveis(TipoNivel tipo) {
	return getCodigo() - tipo.getCodigo();
    }
}
