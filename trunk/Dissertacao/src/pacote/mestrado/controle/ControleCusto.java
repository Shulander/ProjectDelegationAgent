package pacote.mestrado.controle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;
import pacote.mestrado.services.TempoExecucaoService;

public class ControleCusto {

    private static ControleCusto instance;

    private static Map<String, Double> custosMembro = new HashMap<String, Double>();

    public synchronized void adicionaCusto(String membro, double valorHora) {
	custosMembro.put(membro, valorHora);
    }

    public synchronized double getCusto(String membro) {
	return custosMembro.get(membro);
    }

    public static ControleCusto getInstance() {
	if (instance == null) {
	    instance = new ControleCusto();
	}
	return instance;
    }

    public static Double calculaCustoEntrega(String membroNome, Atividade atividadeEscolhida,
	    Collection<Habilidade> habilidades) {
	int diasUteisEntrega = TempoExecucaoService
		.calculaDiasUteisEntrega(membroNome, atividadeEscolhida, habilidades);
	return diasUteisEntrega * getInstance().getCusto(membroNome);
    }
}
