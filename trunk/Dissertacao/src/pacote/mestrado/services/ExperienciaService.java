package pacote.mestrado.services;

import java.util.Collection;
import java.util.LinkedList;

import pacote.mestrado.Membro;
import pacote.mestrado.dominios.TipoNivel;
import pacote.mestrado.entidades.Habilidade;

public class ExperienciaService {

    private static final int XP_GANHO_POR_DIA = 100;
    private static final double XP_MODIFICADOR_NIVEL_ACIMA = 3;
    private static final double XP_MODIFICADOR_NIVEL_ABAIXO = 0.2;

    private static final int XP_JUNIOR_TO_PLENO = 2600;
    private static final int XP_PLENO_TO_SENIOR = XP_JUNIOR_TO_PLENO + 78000;
    private static final int XP_SENIOR_TO_MASTER = XP_PLENO_TO_SENIOR + 130000;

    // Menor q a tarefa: x 3 (1 nivel acima)
    // Maior q a tarefa: x 0.2 (1 nivel acima)
    //                  nao ganha xp (2 nivel acima)

    public static Membro calculaExperienciaGanha(Membro membro) {
	Collection<Habilidade> habilidades = new LinkedList<Habilidade>();
	habilidades.addAll(membro.getHabilidades());
	// para cada habilide
	for (Habilidade habMembro : habilidades) {
	    // encontra a habilidade da tarefa mais compativel
	    Habilidade habTarefa = encontraMelhorHabilidade(habMembro, membro.getAtividadeEscolhida()
		    .getRequisitosHabilidades());
	    if (habTarefa != null) {
		// calcula o numero de dias trabalhados
		int diasTrabalhados = DateUtil.getDiferencaEmDiasUteis(membro.getAtividadeEscolhida()
			.getDataInicioExecucao(), membro.getAtividadeEscolhida().getDataTerminoExecucao());
		// calcula a experiencia
		calculaExperiencia(habMembro, habTarefa, diasTrabalhados, membro.getHabilidades());
	    }
	}

	return membro;
    }

    private static void calculaExperiencia(Habilidade habMembro, Habilidade habTarefa, int diasTrabalhados,
	    Collection<Habilidade> habilidades) {

	// calcula a diferença entre o nivel da tarefa e o nivel do membro
	// tarefas mais dificies serao um numero positivo
	// tarefas mais faceis serao um numero negativo
	Integer diferencaNiveis = habTarefa.getNivel().diferencaNiveis(habMembro.getNivel());
	// caso a diferença seja maior do que 1 nao fazemos nada pois nao é
	// possivel realizar ou aprender
	if (Math.abs(diferencaNiveis) <= 1) {
	    // xp basico por dia e o numero de dias
	    int xp = XP_GANHO_POR_DIA * diasTrabalhados;
	    
	    // aplicamos o modificador de niveis
	    if (diferencaNiveis == -1) {
		xp *= XP_MODIFICADOR_NIVEL_ABAIXO;
	    } else if (diferencaNiveis == 1) {
		xp *= XP_MODIFICADOR_NIVEL_ACIMA;
	    }
	    
	    Habilidade habXPGanho;
	    if (habMembro.getNome().equals(habTarefa.getNome())) {
		// caso a habilidade tenha o mesmo nome utilizamos a atividade do membro
		habXPGanho = habMembro;
	    } else {
		// caso contrario é buscada uma outra tarefa para ganhar experiencia
		// caso nao encontre é criada uma nova habilidade
		habXPGanho = buscaTarefaMesmaHabilidade(habTarefa, habilidades);
	    }

	    // calcula o xp para a habilidade
	    habXPGanho.setXp(calculaXP(habXPGanho, xp));
	    // verifica se deve trocar de nivel
	    habXPGanho.setNivel(calculaNovoNivel(habXPGanho.getXp()));
	}
    }

    /**
     * Calcula o novo nivel  a partir do xp
     * @param xp
     * @return
     */
    private static TipoNivel calculaNovoNivel(Integer xp) {
	if (xp >= XP_SENIOR_TO_MASTER) {
	    return TipoNivel.MASTER;
	} else if (xp >= XP_PLENO_TO_SENIOR) {
	    return TipoNivel.SENIOR;
	} else if (xp >= XP_JUNIOR_TO_PLENO) {
	    return TipoNivel.PLENO;
	} else {
	    return TipoNivel.JUNIOR;
	}
    }

    /**
     * Retorna o novo xp baseado no nivel e no xp ganho.
     * @param habXPGanho
     * @param xp
     * @return
     */
    private static Integer calculaXP(Habilidade habXPGanho, int xp) {
	int xpAtual = 0;
	switch (habXPGanho.getNivel()) {
	case JUNIOR:
	    xpAtual = habXPGanho.getXp();
	    break;
	case PLENO:
	    xpAtual = (habXPGanho.getXp() >= XP_JUNIOR_TO_PLENO ? habXPGanho.getXp() : XP_JUNIOR_TO_PLENO);
	    break;
	case SENIOR:
	    xpAtual = (habXPGanho.getXp() >= XP_PLENO_TO_SENIOR ? habXPGanho.getXp() : XP_PLENO_TO_SENIOR);
	    break;
	case MASTER:
	    xpAtual = (habXPGanho.getXp() >= XP_SENIOR_TO_MASTER ? habXPGanho.getXp() : XP_SENIOR_TO_MASTER);
	    break;
	}
	return xpAtual + xp;
    }

    /**
     * Busca uma habilidade que tenha o mesmo nome de habilidae
     * @param habTarefa
     * @param habilidades
     * @return
     */
    private static Habilidade buscaTarefaMesmaHabilidade(Habilidade habTarefa, Collection<Habilidade> habilidades) {
	// caso encontre uma tarefa com o mesmo nome retorna
	for (Habilidade habilidade : habilidades) {
	    if (habilidade.getNome().equals(habTarefa.getNome())) {
		return habilidade;
	    }
	}

	// caso contrario cria uma nova habilidade e adiciona a lista de habilidades
	Habilidade retorno = new Habilidade();
	retorno.setArea(habTarefa.getArea());
	retorno.setNivel(TipoNivel.JUNIOR);
	retorno.setNome(habTarefa.getNome());
	retorno.setXp(0);
	
	habilidades.add(retorno);
	return retorno;
    }

    /**
     * procura a habilidade de requisitos da atividade mais compativel com a habilidade do membro 
     * @param habMembro
     * @param requisitosHabilidades
     * @return
     */
    private static Habilidade encontraMelhorHabilidade(Habilidade habMembro,
	    Collection<Habilidade> requisitosHabilidades) {

	Habilidade retorno = null;
	double coef = -1;

	for (Habilidade habTarefa : requisitosHabilidades) {
	    double coefHabilidade = CompatibilidadeTarefaService.calculaGrauCompatibilidade(habMembro, habTarefa);

	    // caso o coefHabilidade seja 0.0 entao nao deve ser selecionado
	    boolean troca = coefHabilidade > 0;
	    // caso o coeficiente atual é maior, entao foi escolhida uma nova
	    // habilidade
	    troca = troca && (coefHabilidade > coef);
	    // caso os coeficientes sejam iguais entao comparamos qual tem o
	    // maior nivel
	    troca = troca || (coefHabilidade == coef) && habTarefa.getNivel().after(retorno.getNivel());

	    if (troca) {
		coef = coefHabilidade;
		retorno = habTarefa;
	    }
	}

	return retorno;
    }

}
