package pacote.mestrado.services.impl;

import java.util.Collection;
import java.util.List;

import pacote.mestrado.controle.ControleAprendizado;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;
import pacote.mestrado.services.AprendizadoService;
import pacote.mestrado.services.CompatibilidadeTarefaService;
import pacote.mestrado.services.EscolhaTarefaService;

public class EscolhaTarefaAprendizadoImpl implements EscolhaTarefaService {

    private static final double ESCOLHA_TAREFA_RATIO = 0.7;

    @Override
    public Atividade selecionaAtividadeHabilidade(Collection<Atividade> atividadesDisponiveis, String nomeMembro,
	    Collection<Habilidade> habilidades, Collection<Atividade> atividadesInvalidas) {
	Atividade retorno = null;
	double compatibilidadeAtual = 0;

	// Busco as atividades com compatibilidade minima
	List<Atividade> atividades = CompatibilidadeTarefaService.selecionaAtividadesCompativeis(atividadesDisponiveis,
		habilidades, atividadesInvalidas);

	for (Atividade atividade : atividades) {
	    double compatibilidadeBase = CompatibilidadeTarefaService
		    .calculaGrauCompatibilidade(atividade, habilidades);
	    double compatibilidadeAprendizado = AprendizadoService.calculaCompatibilidadeAprendizado(atividade, habilidades,
		    ControleAprendizado.getInstance().getHabilidades(nomeMembro));
	    double compatibilidade = calculaCompatibilidade(compatibilidadeBase, compatibilidadeAprendizado);

	    // caso a compatibilidade seja maior que a atual, entao encontramos uma
	    // tarefa mais compativel a ser feita
	    if (retorno == null || compatibilidade > compatibilidadeAtual) {
		retorno = atividade;
		compatibilidadeAtual = compatibilidade;
	    }
	}

	return retorno;
    }

    private double calculaCompatibilidade(double compatibilidadeBase, double compatibilidadeAprendizado) {
	return (compatibilidadeBase * ESCOLHA_TAREFA_RATIO + compatibilidadeAprendizado * (1 - ESCOLHA_TAREFA_RATIO)) / 2;
    }

    @Override
    public boolean verificaHabilidadesBMelhorHabilidadesA(Atividade atividade, String nomeMembroA,
	    Collection<Habilidade> habilidadesA, String nomeMembroB, Collection<Habilidade> habilidadesB) {

	// calcula a compatibilidade de A
	double compatibilidadeBaseA = CompatibilidadeTarefaService.calculaGrauCompatibilidade(atividade, habilidadesA);
	Collection<Habilidade> habilidadesAprendizadoA = ControleAprendizado.getInstance().getHabilidades(nomeMembroA);
	double compatibilidadeAprendizadoA = AprendizadoService.calculaCompatibilidadeAprendizado(atividade, habilidadesA, habilidadesAprendizadoA);
	double compatibilidadeA = calculaCompatibilidade(compatibilidadeBaseA, compatibilidadeAprendizadoA);

	// calcula a compatibilidade de B
	double compatibilidadeBaseB = CompatibilidadeTarefaService.calculaGrauCompatibilidade(atividade, habilidadesB);
	Collection<Habilidade> habilidadesAprendizadoB = ControleAprendizado.getInstance().getHabilidades(nomeMembroB);
	double compatibilidadeAprendizadoB = AprendizadoService.calculaCompatibilidadeAprendizado(atividade, habilidadesB, habilidadesAprendizadoB);
	double compatibilidadeB = calculaCompatibilidade(compatibilidadeBaseB, compatibilidadeAprendizadoB);

	return compatibilidadeB < compatibilidadeA;
    }

}
