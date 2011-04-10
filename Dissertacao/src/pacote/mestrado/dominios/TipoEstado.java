package pacote.mestrado.dominios;

public enum TipoEstado {
    BLOQUEADA("bloqueada"), DISPONIVEL("disponivel"), ALOCADA("alocada"), CONCLUIDA("concluida");

    private String codigo;

    private TipoEstado(String codigo) {
	this.codigo = codigo;
    }

    public static TipoEstado obterPorCodigo(String codigo) {
	for (TipoEstado tipo : values()) {
	    if (tipo.codigo.equals(codigo)) {
		return tipo;
	    }
	}
	throw new IllegalArgumentException("O codigo informado (" + codigo
		+ ") é invalido para a obtenção do TipoEstado");
    }
    
    public String toString() {
	return codigo;
    }

}
