package pacote.mestrado.services;

import java.util.Collection;

import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class AprendizadoService {

    /**
     * Calcula o grau de compatibilidade da atividade com a atividade
     * 
     * @param atividade
     *            Atividade a ser verificada
     * @param aprendizagem
     *            Habilidades que gostaria de desenvolver habilidades
     * @return
     */
    public static double calculaCompatibilidadeAprendizado(Atividade atividade,
	    Collection<Habilidade> habilidadesMembro, Collection<Habilidade> habilidadesAprendizagem) {
	double experienciaAtual = 0;
	for (Habilidade habMembro : habilidadesMembro) {
	    // encontramos a habilidae da atividade que tenha melhor
	    // compatibilidade com a tarefa do membro
	    Habilidade habTarefa = ExperienciaService.encontraMelhorHabilidade(habMembro,
		    atividade.getRequisitosHabilidades());

	    // calculamos a experiencia que ela dará
	    double experiencia = ExperienciaService.calculaExperienciaGanha(habTarefa, habMembro);

	    // econtramos alguma habilidade que gostariamos de ter aprendizado
	    // nessa tarefa.
	    if (experiencia > experienciaAtual) {
		Habilidade habAprendizagem = encontraHabilidadeAprendizado(habTarefa, habilidadesAprendizagem);
		if (habAprendizagem != null) {
		    experienciaAtual = experiencia;
		}
	    }
	}
	
	// Calcula a experiencia percentual para retorno
	return ExperienciaService.calculaPercentualRelativoMaximoDia(experienciaAtual);
    }

    private static Habilidade encontraHabilidadeAprendizado(Habilidade habTarefa,
	    Collection<Habilidade> habilidadesAprendizagem) {
	for (Habilidade habAprendizado : habilidadesAprendizagem) {
	    if (habTarefa.getNome().equals(habAprendizado.getNome())) {
		return habAprendizado;
	    }
	}
	return null;
    }
}
