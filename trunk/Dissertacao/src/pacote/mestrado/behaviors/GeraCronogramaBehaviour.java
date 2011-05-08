package pacote.mestrado.behaviors;

import jade.core.behaviours.SimpleBehaviour;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import pacote.mestrado.Gestor;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.services.ControleMembro;
import pacote.mestrado.services.CustoService;
import pacote.mestrado.services.DateUtil;

public class GeraCronogramaBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = -6753054189571569660L;

    boolean terminou;

    private Gestor gestor;

    private static Map<String, Double> custoProjeto;

    public GeraCronogramaBehaviour(Gestor gestor) {
	custoProjeto = new HashMap<String, Double>();
	this.gestor = gestor;
	terminou = false;
    }

    @Override
    public void action() {
	if (ControleMembro.getInstance().verificaTodosTerminaram()) {
	    System.out.println("------------ Atividades -----------");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    DecimalFormat reaisFormat = new DecimalFormat();
	    reaisFormat.applyPattern("R$ #,##0.00");
	    for (Atividade atividade : gestor.getListaAtividades()) {
		System.out.print(atividade.getId() + "\t");
		System.out.print(atividade.getMembroNome() + "\t");
		System.out.print(atividade.getNome() + "\t");
		System.out.print(dateFormat.format(atividade.getDataInicioExecucao()) + "\t");
		System.out.print(dateFormat.format(DateUtil.subtraiDiasUteis(atividade.getDataTerminoExecucao(), 1))
			+ "\t");
		System.out.print(DateUtil.getDiferencaEmDiasUteis(atividade.getDataInicioExecucao(),
			atividade.getDataTerminoExecucao())
			+ "\t");
		System.out.print(reaisFormat.format(calculaCusto(atividade,
			CustoService.getInstance().getCusto(atividade.getMembroNome())))
			+ "\t");
		System.out.println("");
	    }
	    System.out.println("Total:");
	    for (String projeto : custoProjeto.keySet()) {
		System.out.println(projeto + ": " + reaisFormat.format(custoProjeto.get(projeto)));
	    }
	    System.out.println("------------ Atividades -----------");
	    terminou = true;
	} else {
	    block(50);
	}
    }

    private Double calculaCusto(Atividade atividade, double custo) {

	double diferencaEmDiasUteis = DateUtil.getDiferencaEmDiasUteis(atividade.getDataInicioExecucao(),
		atividade.getDataTerminoExecucao());
	double retorno = diferencaEmDiasUteis * custo * 8; // assumindo 8 horas
							   // diarias

	String projeto = atividade.getNome().substring(0, 2);
	if (!custoProjeto.containsKey(projeto)) {
	    custoProjeto.put(projeto, 0.0);
	}
	custoProjeto.put(projeto, custoProjeto.get(projeto) + retorno);

	return retorno;
    }

    @Override
    public boolean done() {
	return terminou;
    }

}
