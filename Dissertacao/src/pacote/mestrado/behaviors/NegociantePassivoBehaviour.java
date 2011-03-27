package pacote.mestrado.behaviors;

import pacote.mestrado.Membro;
import jade.core.behaviours.SimpleBehaviour;

/**
 * Esse Behaviour se inicia quando o Agente Membro ja encontrou uma tarefa. Ele
 * dará a oportunidade a outro agente de negociar com ele pela prioridade da
 * tarefa, por esse motivo se denomina Agente Passivo.
 * 
 * @author Shulander
 */
public class NegociantePassivoBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = 6495024652042573543L;

    // Membro ao qual o Behaviour esta associado
    private Membro membro;
    private boolean terminou;

    public NegociantePassivoBehaviour(Membro membro) {
	this.membro = membro;
	terminou = false;
    }

    @Override
    public void action() {
	System.out.println("Teste de novo behavior: NegociantePassivoBehaviour");
	terminou = true;
    }

    @Override
    public boolean done() {
	return terminou;
    }

}
