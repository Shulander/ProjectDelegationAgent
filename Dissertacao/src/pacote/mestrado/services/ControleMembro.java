package pacote.mestrado.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pacote.mestrado.dominios.TipoEtapaNegociacao;

public class ControleMembro {

    private static ControleMembro instance = null;

    private static Map<String, TipoEtapaNegociacao> situacao = new HashMap<String, TipoEtapaNegociacao>();

    private ControleMembro() {
    }

    /**
     * altera a etapa de um agente.
     * 
     * @param agente
     * @param etapa
     */
    public synchronized void notifica(String agente, TipoEtapaNegociacao etapa) {
	situacao.put(agente, etapa);
    }

    /**
     * Remove um agente
     * 
     * @param agente
     * @return
     */
    public synchronized TipoEtapaNegociacao remove(String agente) {
	return situacao.remove(agente);
    }

    /**
     * Retorna a etapa em que o agente se encontra.
     * 
     * @param agente
     * @return
     */
    public synchronized TipoEtapaNegociacao getEtapa(String agente) {
	return situacao.get(agente);
    }

    /**
     * Conta quantos agentes estao nessa etapa
     * 
     * @param etapa
     * @return int
     */
    public synchronized int contaAgentesEtapa(TipoEtapaNegociacao etapa) {
	return getListaAgentesEtapa(etapa).size();
    }

    /**
     * Busca o nome dos agentes que estao em um determinada etapa.
     * 
     * @param etapa
     * @return
     */
    public synchronized List<String> getListaAgentesEtapa(TipoEtapaNegociacao etapa) {
	List<String> retorno = new LinkedList<String>();

	for (String agenteNome : situacao.keySet()) {
	    if (situacao.get(agenteNome).equals(etapa)) {
		retorno.add(agenteNome);
	    }
	}

	return retorno;
    }

    public synchronized static ControleMembro getInstance() {
	if (instance == null) {
	    instance = new ControleMembro();
	}
	return instance;
    }
    
    public String toString() {
	StringBuilder str = new StringBuilder();
	for (String agenteNome : situacao.keySet()) {
	    str.append(agenteNome);
	    str.append("-");
	    str.append(situacao.get(agenteNome));
	    str.append("\n");
	}
	
	return str.toString();
    }
}
