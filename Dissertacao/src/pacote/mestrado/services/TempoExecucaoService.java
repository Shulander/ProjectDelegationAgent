package pacote.mestrado.services;

import java.util.Collection;
import java.util.Date;

import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class TempoExecucaoService {

    /**
     * Calcula a data de entrega
     * 
     * @param membroNome
     * @param atividadeEscolhida
     * @param habilidades
     * @return
     */
    public static Date calculcaDataEntrega(String membroNome, Atividade atividadeEscolhida, Collection<Habilidade> habilidades) {
	Date dataInicio = ControleAtividade.getInstance().getDataAtual();
	if(dataInicio == null) {
	    dataInicio = atividadeEscolhida.getDataInicial();
	}
	
	int diasTerminoAtual = ControleAtividade.getInstance().getDiasParaTermino(membroNome);
	int diasTerminoNova = DateUtil.getDiferencaEmDiasUteis(atividadeEscolhida.getDataInicial(), atividadeEscolhida.getDataEntrega());
	
	diasTerminoNova = (int) Math.ceil(diasTerminoNova * (2.0-CompatibilidadeTarefaService.calculaGrauCompatibilidade(atividadeEscolhida, habilidades)));
	
	return DateUtil.adicionaDiasUteis(dataInicio, diasTerminoNova+diasTerminoAtual);
    }

}