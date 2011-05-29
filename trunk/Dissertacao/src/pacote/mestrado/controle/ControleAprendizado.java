package pacote.mestrado.controle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pacote.mestrado.entidades.Habilidade;

public class ControleAprendizado {

    private static ControleAprendizado instance;

    private static Map<String, Collection<Habilidade>> aprendizadoMembro = new HashMap<String, Collection<Habilidade>>();

    public synchronized void adicionaHabilidades(String membro, Collection<Habilidade> valorHora) {
	aprendizadoMembro.put(membro, valorHora);
    }

    public synchronized Collection<Habilidade> getHabilidades(String membro) {
	return aprendizadoMembro.get(membro);
    }

    public static ControleAprendizado getInstance() {
	if (instance == null) {
	    instance = new ControleAprendizado();
	}
	return instance;
    }
}
