package pacote.mestrado.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class CustoService {
    
    private static CustoService instance;

    private static Map<String, Double> custosMembro = new HashMap<String, Double>();
    
    public synchronized void adicionaCusto(String membro, double valorHora) {
	custosMembro.put(membro, valorHora);
    }
    
    public synchronized double getCusto(String membro) {
	return custosMembro.get(membro);
    }
    
    public static CustoService getInstance() {
	if (instance == null) {
	    instance = new CustoService();
	}
	return instance;
    }
    
    public static Double calculaCustoEntrega(String membroNome, Atividade atividadeEscolhida, Collection<Habilidade> habilidades){
	int diasUteisEntrega = TempoExecucaoService.calculaDiasUteisEntrega(membroNome, atividadeEscolhida, habilidades);
	return diasUteisEntrega*getInstance().getCusto(membroNome);
    }
}
