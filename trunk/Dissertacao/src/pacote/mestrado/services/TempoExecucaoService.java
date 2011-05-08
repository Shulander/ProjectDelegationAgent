package pacote.mestrado.services;

import java.util.Collection;
import java.util.Date;

import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class TempoExecucaoService {

    /**
     * Calcula o numero de dias uteis para a execução da tarefa
     * 
     * @param membroNome
     * @param atividadeEscolhida
     * @param habilidades
     * @return
     */
    public static int calculaDiasUteisEntrega(String membroNome, Atividade atividadeEscolhida,
	    Collection<Habilidade> habilidades) {
	int diasTerminoNova = DateUtil.getDiferencaEmDiasUteis(atividadeEscolhida.getDataInicial(),
		atividadeEscolhida.getDataEntrega());
	return (int) Math
		.ceil(diasTerminoNova
			* (2.0 - CompatibilidadeTarefaService.calculaGrauCompatibilidade(atividadeEscolhida,
				habilidades, true)));
    }

    /**
     * Calcula a data de entrega
     * 
     * @param membroNome
     * @param atividadeEscolhida
     * @param habilidades
     * @return
     */
    public static Date calculcaDataEntrega(String membroNome, Atividade atividadeEscolhida,
	    Collection<Habilidade> habilidades) {
	Date dataInicio = ControleAtividade.getInstance().getDataAtual();
	// if(dataInicio == null) {
	// dataInicio = atividadeEscolhida.getDataInicial();
	// }

	int diasTerminoAtual = ControleAtividade.getInstance().getDiasParaTermino(membroNome);
	int diasTerminoNova = calculaDiasUteisEntrega(membroNome, atividadeEscolhida, habilidades);

	// OS dias do termino da atual ja levam em conta dias uteis
	dataInicio = DateUtil.adicionaDias(dataInicio, diasTerminoAtual);
	atividadeEscolhida.setDataInicioExecucao(dataInicio);
	atividadeEscolhida.setDataTerminoExecucao(DateUtil.adicionaDiasUteis(dataInicio, diasTerminoNova));
	return atividadeEscolhida.getDataTerminoExecucao();
    }

}
