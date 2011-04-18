package pacote.mestrado.behaviors;

import jade.core.behaviours.SimpleBehaviour;

import java.text.SimpleDateFormat;

import pacote.mestrado.Gestor;
import pacote.mestrado.entidades.Atividade;

public class GeraCronogramaBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = -6753054189571569660L;
    
    boolean terminou;

    private Gestor gestor;
    
    public GeraCronogramaBehaviour(Gestor gestor) {
	this.gestor = gestor;
	terminou = false;
    }

    @Override
    public void action() {
	try {
	    Thread.sleep(500);
	} catch (InterruptedException e) {
	}
	System.out.println("------------ Atividades -----------");
	SimpleDateFormat format =new SimpleDateFormat("dd-MM-yyyy");
	for (Atividade atividade : gestor.getListaAtividades()) {
	    System.out.print(atividade.getId()+"\t");
	    System.out.print(atividade.getMembroNome()+"\t");
	    System.out.print(atividade.getNome()+"\t");
	    System.out.print(format.format(atividade.getDataInicioExecucao())+"\t");
	    System.out.print(format.format(atividade.getDataTerminoExecucao())+"\t");
	    System.out.println("");
	}
	System.out.println("------------ Atividades -----------");
	terminou = true;
    }

    @Override
    public boolean done() {
	return terminou;
    }

}
