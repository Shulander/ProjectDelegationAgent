package pacote.mestrado.entidades;

import java.io.Serializable;

public class MensagemTO implements Serializable {
    private static final long serialVersionUID = -5005842616962128468L;

    private String assunto;
    private Object mensagem;

    public MensagemTO() {
	assunto = null;
	mensagem = null;
    }

    public String getAssunto() {
	return assunto;
    }

    public void setAssunto(String assunto) {
	this.assunto = assunto;
    }

    public Object getMensagem() {
	return mensagem;
    }

    public void setMensagem(Object mensagem) {
	this.mensagem = mensagem;
    }
}
