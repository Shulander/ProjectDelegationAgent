package pacote.mestrado.controle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pacote.mestrado.entidades.Habilidade;

public class ControleXPMembro {

    private static ControleXPMembro instance;

    private static Map<String, Collection<Habilidade>> xpMembro = new HashMap<String, Collection<Habilidade>>();

    public synchronized void adicionaHabilidades(String membro, Collection<Habilidade> habilidades) {
	xpMembro.put(membro, habilidades);
    }

    public synchronized Collection<Habilidade> getHabilidades(String membro) {
	return xpMembro.get(membro);
    }

    public static ControleXPMembro getInstance() {
	if (instance == null) {
	    instance = new ControleXPMembro();
	}
	return instance;
    }
}
