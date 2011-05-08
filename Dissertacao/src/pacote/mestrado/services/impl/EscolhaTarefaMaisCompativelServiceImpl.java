package pacote.mestrado.services.impl;

import java.util.Collection;
import java.util.Date;

import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;
import pacote.mestrado.services.CompatibilidadeTarefaService;
import pacote.mestrado.services.EscolhaTarefaService;
import pacote.mestrado.services.TempoExecucaoService;

public class EscolhaTarefaMaisCompativelServiceImpl implements EscolhaTarefaService {

    @Override
    public Atividade selecionaAtividadeHabilidade(Collection<Atividade> atividadesDisponiveis, String nomeMembro,
	    Collection<Habilidade> habilidades, Collection<Atividade> atividadesInvalidas) {
	return CompatibilidadeTarefaService.selecionaAtividadeHabilidade(atividadesDisponiveis, habilidades,
		atividadesInvalidas);
    }

    @Override
    public boolean verificaHabilidadesBMelhorHabilidadesA(Atividade atividade, String nomeMembroA,
	    Collection<Habilidade> habilidadesA, String nomeMembroB, Collection<Habilidade> habilidadesB) {
	double minhaPontuacao = CompatibilidadeTarefaService.calculaGrauCompatibilidade(atividade, habilidadesA);
	double pontuacaoRequisicao = CompatibilidadeTarefaService.calculaGrauCompatibilidade(atividade, habilidadesB);

	boolean trocaAceita = minhaPontuacao < pontuacaoRequisicao;
	if (trocaAceita) {
	    Date minhaDataTermino = TempoExecucaoService.calculcaDataEntrega(nomeMembroA, atividade, habilidadesA);
	    Date dataRequisicao = TempoExecucaoService.calculcaDataEntrega(nomeMembroB, atividade, habilidadesB);
	    trocaAceita = minhaDataTermino.after(dataRequisicao);
	}
	return trocaAceita;
    }

}
