package pacote.mestrado.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ControleAtividade {

    private static ControleAtividade instance = null;

    private static Map<String, Date> datasTermino = new HashMap<String, Date>();

    private static Date dataAtual = null;

    private ControleAtividade() {
    }

    /**
     * altera a etapa de um agente.
     * 
     * @param agente
     * @param dataFim
     */
    public synchronized void adiciona(String agente, Date dataFim) {
	datasTermino.put(agente, dataFim);
    }

    public Date getDataAtual() {
	return dataAtual;
    }

    /**
     * Chamado pelo gestor no inicio da carga de tarefas, setada para a primeira
     * data de inicio de uma tarefa
     * 
     * @param novaDataAtual
     */
    public void setDataAtual(Date novaDataAtual) {
	dataAtual = novaDataAtual;
    }

    /**
     * Remove um agente
     * 
     * @param agente
     * @return
     */
    public synchronized Date remove(String agente) {
	return datasTermino.remove(agente);
    }

    /**
     * Retorna a data Final da execucao da atividade de um agente
     * 
     * @param agente
     * @return
     */
    public synchronized Date getDataFim(String agente) {
	return datasTermino.get(agente);
    }

    /**
     * Busca Verifica se o agente Terminou sua execucao;
     * 
     * @param etapa
     * @return
     */
    public synchronized boolean getTerminei(String agente) {
	Date dataTermino = getDataFim(agente);
	for (String agenteNome : datasTermino.keySet()) {
	    // caso a data atual é apos a data de outro agente retorna falso.
	    if (dataTermino.after(datasTermino.get(agenteNome))) {
		return false;
	    }
	}
	// define a data atual como sendo a nova data
	dataAtual = dataTermino;
	// caso foi tstado com todos e nenhum termina antes
	return true;
    }

    public synchronized static ControleAtividade getInstance() {
	if (instance == null) {
	    instance = new ControleAtividade();
	}
	return instance;
    }

    public synchronized int getDiasParaTermino(String agente) {
	Date dataTermino = getDataFim(agente);
	if (dataTermino == null || dataAtual == null) {
	    return 0;
	}
	return DateUtil.getDiferencaEmDiasUteis(dataAtual, dataTermino);
    }

    public String toString() {
	StringBuilder str = new StringBuilder();
	for (String agenteNome : datasTermino.keySet()) {
	    str.append(agenteNome);
	    str.append("-");
	    str.append(datasTermino.get(agenteNome));
	    str.append("\n");
	}

	return str.toString();
    }
}
