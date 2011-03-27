package pacote.mestrado.behaviors;

import jade.core.behaviours.SimpleBehaviour;

public class BTeste extends SimpleBehaviour {

    private static final long serialVersionUID = -790765386546663883L;
    private boolean terminou;

    public BTeste() {
	terminou = false;
    }

    @Override
    public void action() {
	System.out.println("Teste de novo behavior");
	terminou = true;
    }

    @Override
    public boolean done() {
	return terminou;
    }
}