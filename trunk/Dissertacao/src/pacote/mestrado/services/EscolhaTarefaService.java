package pacote.mestrado.services;

import java.util.Collection;

import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public interface EscolhaTarefaService {

    public Atividade selecionaAtividadeHabilidade(Collection<Atividade> atividadesDisponiveis, String nomeMembro,
	    Collection<Habilidade> habilidades, Collection<Atividade> atividadesInvalidas);

    boolean verificaHabilidadesBMelhorHabilidadesA(Atividade atividade, String nomeMembroA,
	    Collection<Habilidade> habilidadesA, String nomeMembroB, Collection<Habilidade> habilidadesB);

}
