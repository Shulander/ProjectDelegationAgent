package pacote.mestrado.dominios;

public enum TipoNivel {
	JUNIOR(1, "Junior"), PLENO(2, "Pleno"), SENIOR(3, "S�nior"), MASTER(4,
			"Master");

	private Integer codigo;
	private String descricao;

	private TipoNivel(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public TipoNivel obterPorCodigo(Integer codigo) {
		for (TipoNivel tipo : values()) {
			if (tipo.codigo.equals(codigo)) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("O codigo informado (" + codigo
				+ ") � invalido para a obten��o do TipoNivel");
	}

	public String toString() {
		return descricao;
	}
}
