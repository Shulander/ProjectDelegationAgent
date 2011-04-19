package pacote.mestrado.services;


public class ControleGestor {

    private static ControleGestor instance = null;

    private static int mutexConfirmacaoSimulacao = 0;

    private ControleGestor() {
	
    }

    public static ControleGestor getInstance() {
	if (instance == null) {
	    instance = new ControleGestor();
	}
	return instance;
    }

    /**
     * Utilizado somente pelo gestor. Seta quantos Agentes estavam em execucao
     * Atividade e precisam checar antes do termino da negociacao com um agente
     * que esta escolhendo uma nova tarefa
     * 
     * @param i
     */
    public synchronized void gestorNotificaSimuladores(int i) {
	mutexConfirmacaoSimulacao += i;
    }

    /**
     * Utilizado pelos agents que estao em execucao. responsavel por notificar
     * que o agente ja executou e que liberou o agente prosseguir
     */
    public synchronized void mutexCheck() {
	if(mutexConfirmacaoSimulacao > 0) {
	    --mutexConfirmacaoSimulacao;
	}
    }

    /**
     * chamado pelo agente em espera, verifica se os agentes em execucao
     * terminaram de checar a execucao.
     * 
     * @return
     */
    public synchronized boolean mutexReady() {
	return mutexConfirmacaoSimulacao == 0;
    }
}
