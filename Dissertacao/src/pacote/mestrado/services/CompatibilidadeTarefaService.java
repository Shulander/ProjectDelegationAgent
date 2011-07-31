package pacote.mestrado.services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class CompatibilidadeTarefaService {

    private static final double MESMA_HABILIDADE = 1;
    private static final double MESMA_AREA = 0.6;
    private static final double COEFICIENTE_NIVEL_CONHECIMENTO = 0.2;
    private static boolean ignoraNivelAcima = false;
    private static final double GRAU_COMPATIBILIDADE_MINIMO = 0.5;

    /**
     * Calcula o grau de compatibilidade entre uma atividade e um conjunto de
     * habilidades
     * 
     * @param atividade
     * @param habilidades
     * @return retorna a media dos coeficientes encontrados.
     */
    public static double calculaGrauCompatibilidade(Atividade atividade, Collection<Habilidade> habilidades) {
	double retorno = 0.0;
	int divisorPonderado = 0;

	// para cada habildiade
	for (Habilidade hab : atividade.getRequisitosHabilidades()) {
	    double calc = 0.0;
	    // procuramos uma habilidade equivalente
	    for (Habilidade hab2 : habilidades) {
		double coefHabilide = calculaGrauCompatibilidade(hab, hab2);
		// caso o novo seja maior substituimos o antigo
		if (coefHabilide > calc) {
		    calc = coefHabilide;
		}
	    }
	    retorno += calc * hab.getNivel().getCodigo();
	    divisorPonderado += hab.getNivel().getCodigo();
	}

	// calcula a media ponderada (levando em conta o nivel exigido pela
	// habilidade)
	retorno = retorno / divisorPonderado;
	return retorno;
    }

    /**
     * Calcula o grau de compatibilidade entre duas habilidades
     * 
     * @param hab
     * @param hab2
     * @return grau de compatibilidade 0.0 - 1.0
     */
    public static double calculaGrauCompatibilidade(Habilidade hab, Habilidade hab2) {
	double coefHabilide;
	// caso encontremos a mesma habilidade
	// caso seja a mesma area
	// ou seja casos diferentes, tratamos aqui
	if (hab.getNome().equals(hab2.getNome())) {
	    coefHabilide = MESMA_HABILIDADE;
	} else if (hab.getArea().equals(hab2.getArea())) {
	    coefHabilide = MESMA_AREA;
	} else {
	    coefHabilide = 0.0;
	}

	// calculamos a diferenca de niveis de conhecimento
	Integer diferenca = hab.getNivel().diferencaNiveis(hab2.getNivel());

	if (!getIgnoraNivelAcima()) {
	    diferenca = Math.abs(diferenca);
	}
	
	// e descontamos pelo coeficiente de 1 que seria o ideal
	double coefNivel = 0;
	if(Math.abs(diferenca)<=1) {
	    coefNivel = 1 - (diferenca * COEFICIENTE_NIVEL_CONHECIMENTO); 
	}
	
	// calculamos e comparamos o novo coeficiente pelo antigo.
	coefHabilide = coefHabilide * coefNivel;
	return coefHabilide;
    }

    /**
     * Calcula a atividade mais compativel de acordo com uma lista de
     * habilidades
     * 
     * @param atividadesDisponiveis
     * @param habilidades
     * @param atividadesInvalidas
     * @return atividade
     */
    public static Atividade selecionaAtividadeHabilidade(Collection<Atividade> atividadesDisponiveis,
	    Collection<Habilidade> habilidades, Collection<Atividade> atividadesInvalidas) {
	double compatibilidade = GRAU_COMPATIBILIDADE_MINIMO;
	Atividade retorno = null;
	if (atividadesDisponiveis != null) {
	    // verifica todas as atividades disponiveis
	    for (Atividade atividade : atividadesDisponiveis) {
		// caso a atividade nao esta na lista de atividades a ignorar
		if (atividadesInvalidas == null || !atividadesInvalidas.contains(atividade)) {
		    // calcula a compatibilidade nova
		    double compatNova = calculaGrauCompatibilidade(atividade, habilidades);
		    // caso a nova compatibilidade seja maior
		    // substituimos a antiga
		    if (compatNova > compatibilidade) {
			compatibilidade = compatNova;
			retorno = atividade;
		    }
		}
	    }
	}

	// retornamos a atividade mais compativel de acordo com as habilidade
	return retorno;
    }

    public static List<Atividade> selecionaAtividadesCompativeis(Collection<Atividade> atividadesDisponiveis,
	    Collection<Habilidade> habilidades, Collection<Atividade> atividadesInvalidas) {
	List<Atividade> retorno = new LinkedList<Atividade>();

	if (atividadesDisponiveis != null) {
	    // verifica todas as atividades disponiveis
	    for (Atividade atividade : atividadesDisponiveis) {
		// caso a atividade nao esta na lista de atividades a ignorar
		if (atividadesInvalidas == null || !atividadesInvalidas.contains(atividade)) {
		    // calcula a compatibilidade nova
		    double compatNova = calculaGrauCompatibilidade(atividade, habilidades);
		    // caso a compatibilidade seja maior que a compatibilidade
		    // minima
		    // adicionammos a tarefa ao retorno
		    if (compatNova > GRAU_COMPATIBILIDADE_MINIMO) {
			retorno.add(atividade);
		    }
		}
	    }
	}

	return retorno;
    }

    public static double calculaGrauCompatibilidade(Atividade atividadeEscolhida, Collection<Habilidade> habilidades,
	    boolean ignoraNivelAcima) {
	setIgnoraNivelAcima(ignoraNivelAcima);
	double retorno = calculaGrauCompatibilidade(atividadeEscolhida, habilidades);
	setIgnoraNivelAcima(false);
	return retorno;
    }

    private static void setIgnoraNivelAcima(boolean nivelIgnorado) {
	ignoraNivelAcima = nivelIgnorado;
    }

    private static boolean getIgnoraNivelAcima() {
	return ignoraNivelAcima;
    }
}
