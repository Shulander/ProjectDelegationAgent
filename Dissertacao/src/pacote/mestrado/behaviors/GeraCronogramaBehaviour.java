package pacote.mestrado.behaviors;

import jade.core.behaviours.SimpleBehaviour;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pacote.mestrado.Gestor;
import pacote.mestrado.Membro;
import pacote.mestrado.controle.ControleCusto;
import pacote.mestrado.controle.ControleMembro;
import pacote.mestrado.controle.ControleXPMembro;
import pacote.mestrado.entidades.Atividade;
import pacote.mestrado.entidades.Habilidade;
import pacote.mestrado.services.DateUtil;
import pacote.mestrado.services.ExperienciaService;

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
	try {
	    Thread.sleep(500);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
//	if (ControleMembro.getInstance().verificaTodosTerminaram()) {
	    System.out.println("------------ Atividades -----------");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    DecimalFormat reaisFormat = new DecimalFormat();
	    reaisFormat.applyPattern("R$ #,##0.00");
	    for (Atividade atividade : gestor.getListaAtividades()) {
		System.out.print(atividade.getId() + "\t");
		System.out.print(atividade.getMembroNome() + "\t");
		System.out.print(atividade.getNome() + "\t");
		System.out.print(dateFormat.format(atividade.getDataInicioExecucao()) + "\t");
		System.out.print(dateFormat.format(DateUtil.subtraiDiasUteis(atividade.getDataTerminoExecucao(), 1)) + "\t");
		System.out.print(DateUtil.getDiferencaEmDiasUteis(atividade.getDataInicioExecucao(), atividade.getDataTerminoExecucao()) + "\t");
		System.out.print(reaisFormat.format(calculaCusto(atividade, ControleCusto.getInstance().getCusto(atividade.getMembroNome()))) + "\t");
		System.out.println("");
		imprimeExperienciasGanhaAtividade(atividade);
	    }
	    System.out.println("Total:");
	    for (String projeto : custoProjeto.keySet()) {
		System.out.println(projeto + ": " + reaisFormat.format(custoProjeto.get(projeto)));
	    }
	    System.out.println("------------ Atividades -----------");
	    System.out.println(ControleMembro.getInstance().toString());
	    
	    terminou = true;
//	} else {
//	    block(50);
//	}
    }
    
    private void imprimeExperienciasGanhaAtividade(Atividade atividade) {
	Collection<Habilidade> habilidades = ControleXPMembro.getInstance().getHabilidades(atividade.getMembroNome());
	int diasUteis = DateUtil.getDiferencaEmDiasUteis(atividade.getDataInicioExecucao(), atividade.getDataTerminoExecucao());
	for (Habilidade habMembro : habilidades) {
	    
	    if(habMembro.getArea().equals("Testes")) {
		habMembro.getId();
	    }
	    
	    // encontramos a habilidae da atividade que tenha melhor
	    // compatibilidade com a tarefa do membro
	    Habilidade habTarefa = ExperienciaService.encontraMelhorHabilidade(habMembro,
		    atividade.getRequisitosHabilidades());
	    
	    // calculamos a experiencia que ela dará
	    double experiencia = ExperienciaService.calculaExperienciaGanha(habTarefa, habMembro);
	    if(experiencia > 0 && habMembro.getNome().equals(habTarefa.getNome())) {
		experiencia = experiencia * diasUteis;
		System.out.println("\t"+habMembro.getArea() + "\t" +  habMembro.getNome() + "\t"+experiencia);
	    }
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
