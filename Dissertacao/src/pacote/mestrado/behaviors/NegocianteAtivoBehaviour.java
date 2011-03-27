package pacote.mestrado.behaviors;

import jade.core.behaviours.SimpleBehaviour;
import pacote.mestrado.Membro;

/**
 * Esse Behaviour se inicia quando o Agente encontrou uma tarefa para realizar,
 * porém ja esta alocada a outro agente. Entao o agente inicia a negociação pela
 * posse da tarefa.
 * 
 * @author Shulander
 * 
 */
public class NegocianteAtivoBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = 6495024652042573543L;

    // Membro ao qual o Behaviour esta associado
    private Membro membro;
    private boolean terminou;

    public NegocianteAtivoBehaviour(Membro membro) {
	this.membro = membro;
	terminou = false;
    }

    @Override
    public void action() {
	System.out.println("Teste de novo behavior: NegocianteAtivoBehaviour");
	terminou = true;
    }

    @Override
    public boolean done() {
	return terminou;
    }

}
