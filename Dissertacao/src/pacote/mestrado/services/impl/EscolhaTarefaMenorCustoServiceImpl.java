package pacote.mestrado.services.impl;

import java.util.Collection;
import java.util.List;

import pacote.mestrado.controle.ControleCusto;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;
import pacote.mestrado.services.CompatibilidadeTarefaService;
import pacote.mestrado.services.EscolhaTarefaService;
import pacote.mestrado.services.TempoExecucaoService;

public class EscolhaTarefaMenorCustoServiceImpl implements EscolhaTarefaService {

    @Override
    public Atividade selecionaAtividadeHabilidade(Collection<Atividade> atividadesDisponiveis, String nomeMembro,
	    Collection<Habilidade> habilidades, Collection<Atividade> atividadesInvalidas) {
	Atividade retorno = null;
	double custoAtual = 0;

	// Busco as atividades com compatibilidade minima
	List<Atividade> atividades = CompatibilidadeTarefaService.selecionaAtividadesCompativeis(atividadesDisponiveis,
		habilidades, atividadesInvalidas);

	// para cada atividade encontrada calculo o custo
	for (Atividade atividade : atividades) {
	    TempoExecucaoService.calculcaDataEntrega(nomeMembro, atividade, habilidades);
	    double custo = ControleCusto.calculaCustoEntrega(nomeMembro, atividade, habilidades);

	    // caso o custo seja menor que o custo atual, entao encontramos uma
	    // tarefa mais barata em ser feita
	    if (retorno == null || custo < custoAtual) {
		retorno = atividade;
		custoAtual = custo;
	    }

	}

	// retorna o custo encotrado ou null caso nao encontre
	return retorno;
    }

    @Override
    public synchronized boolean verificaHabilidadesBMelhorHabilidadesA(Atividade atividade, String nomeMembroA,
	    Collection<Habilidade> habilidadesA, String nomeMembroB, Collection<Habilidade> habilidadesB) {

	// TempoExecucaoService.calculcaDataEntrega(nomeMembroA, atividade,
	// habilidadesA);
	double custoA = ControleCusto.calculaCustoEntrega(nomeMembroA, atividade, habilidadesA);
	// TempoExecucaoService.calculcaDataEntrega(nomeMembroB, atividade,
	// habilidadesB);
	double custoB = ControleCusto.calculaCustoEntrega(nomeMembroB, atividade, habilidadesB);

	return custoB < custoA;
    }

}
