package pacote.mestrado.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import pacote.mestrado.dominios.TipoHabilidade;
import pacote.mestrado.dominios.TipoNivel;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;

public class CompatibilidadeTarefaService {

    static final double MESMA_HABILIDADE = 1;
    static final double MESMA_AREA = 0.6;
    static final double COEFICIENTE_NIVEL_CONHECIMENTO = 0.2;

    /**
     * Calcula o grau de compatibilidade entre uma atividade e um conjunto de
     * habilidades
     * 
     * @param atividade
     * @param habiliades
     * @return retorna a media dos coeficientes encontrados.
     */
    public static double calculaGrauCompatibilidade(Atividade atividade, Collection<Habilidade> habiliades) {
	double retorno = 0.0;
	int divisorPonderado = 0;

	// para cada habildiade
	for (Habilidade hab : atividade.getRequisitosHabilidades()) {
	    double calc = 0.0;
	    // procuramos uma habilidade equivalente
	    for (Habilidade hab2 : habiliades) {
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
	if (hab.getTipo().equals(hab2.getTipo())) {
	    coefHabilide = MESMA_HABILIDADE;
	} else if (hab.getTipo().getArea().equals(hab2.getTipo().getArea())) {
	    coefHabilide = MESMA_AREA;
	} else {
	    coefHabilide = 0.0;
	}

	// calculamos a diferenca de niveis de conhecimento
	Integer diferenca = hab.getNivel().diferencaNiveis(hab2.getNivel());
	diferenca = Math.abs(diferenca);
	// e descontamos pelo coeficiente de 1 que seria o ideal
	double coefNivel = 1 - (diferenca * COEFICIENTE_NIVEL_CONHECIMENTO);

	// calculamos e comparamos o novo coeficiente pelo antigo.
	coefHabilide = coefHabilide * coefNivel;
	return coefHabilide;
    }

    public static Atividade selecionaAtividadeHabilidade(Collection<Atividade> atividadesDisponiveis,
	    Collection<Habilidade> habiliades, Collection<Atividade> atividadesInvalidas) {
	double compatibilidade = 0.0;
	Atividade retorno = null;

	// verifica todas as atividades disponiveis
	for (Atividade atividade : atividadesDisponiveis) {
	    // caso a atividade nao esta na lista de atividades a ignorar
	    if (atividadesInvalidas == null || !atividadesInvalidas.contains(atividade)) {
		// calcula a compatibilidade nova
		double compatNova = calculaGrauCompatibilidade(atividade, habiliades);
		// caso a nova compatibilidade seja maior
		// substituimos a antiga
		if (compatNova > compatibilidade) {
		    compatibilidade = compatNova;
		    retorno = atividade;
		}
	    }
	}

	// retornamos a atividade mais compativel de acordo com as habilidade
	return retorno;
    }

    public static void main(String args[]) {
	ArrayList<Atividade> listaAtividades = new ArrayList<Atividade>();
	// Inicializa atividade 1
	Atividade at1 = new Atividade();
	at1.setId(1);
	at1.setNome("Desenvolver modulo A1");
	at1.setTipo("Desenvolvimento");
	SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
	Date dataInicial = new Date();
	try {
	    dataInicial = formatador.parse("04/03/2011");
	} catch (ParseException e) {
	    System.out.println("Erro ao inicializar data inicial!");
	    e.printStackTrace();
	}
	at1.setDataInicial(dataInicial);
	Date dataEntrega = new Date();
	try {
	    dataEntrega = formatador.parse("05/03/2011");
	} catch (ParseException e) {
	    System.out.println("Erro ao inicializar data de entrega!");
	    e.printStackTrace();
	}
	at1.setDataEntrega(dataEntrega);
	at1.setOrcamento(305.5);
	Habilidade hab1 = new Habilidade(1, TipoHabilidade.JAVA, TipoNivel.PLENO);
	Habilidade hab2 = new Habilidade(2, TipoHabilidade.HTML, TipoNivel.PLENO);
	Habilidade hab3 = new Habilidade(3, TipoHabilidade.TRABALHO_EQUIPE, TipoNivel.JUNIOR);
	at1.getRequisitosHabilidades().add(hab1);
	at1.getRequisitosHabilidades().add(hab2);
	at1.getRequisitosHabilidades().add(hab3);
	at1.setEstado(1); // disponivel

	// Inicializa atividade 2
	Atividade at2 = new Atividade();
	at2.setId(2);
	at2.setNome("Analisar requisitos");
	at2.setTipo("Requisitos");
	Date dataInicialAt2 = new Date();
	try {
	    dataInicialAt2 = formatador.parse("04/03/2011");
	} catch (ParseException e) {
	    System.out.println("Erro ao inicializar data inicial At2!");
	    e.printStackTrace();
	}
	at2.setDataInicial(dataInicialAt2);
	Date dataEntregaAt2 = new Date();
	try {
	    dataEntregaAt2 = formatador.parse("05/03/2011");
	} catch (ParseException e) {
	    System.out.println("Erro ao inicializar data de entrega!");
	    e.printStackTrace();
	}
	at2.setDataEntrega(dataEntregaAt2);
	at2.setOrcamento(150.0);
	Habilidade hab11 = new Habilidade(1, TipoHabilidade.UML, TipoNivel.PLENO);
	Habilidade hab22 = new Habilidade(2, TipoHabilidade.RELACIONAMENTO_CLIENTE, TipoNivel.PLENO);
	Habilidade hab33 = new Habilidade(3, TipoHabilidade.GESTAO_TIME, TipoNivel.JUNIOR);
	at2.getRequisitosHabilidades().add(hab11);
	at2.getRequisitosHabilidades().add(hab22);
	at2.getRequisitosHabilidades().add(hab33);
	at2.setEstado(1); // disponivel

	// Adiciona as atividades na lista
	listaAtividades.add(at1);
	listaAtividades.add(at2);

	ArrayList<Habilidade> habilidades = new ArrayList<Habilidade>();
	habilidades.add(new Habilidade(1, TipoHabilidade.UML, TipoNivel.SENIOR));
	habilidades.add(new Habilidade(2, TipoHabilidade.RELACIONAMENTO_CLIENTE, TipoNivel.SENIOR));
	habilidades.add(new Habilidade(3, TipoHabilidade.GESTAO_TIME, TipoNivel.MASTER));

	Atividade atividade = selecionaAtividadeHabilidade(listaAtividades, habilidades, null);
	System.out.println(atividade.toString());
    }

}
