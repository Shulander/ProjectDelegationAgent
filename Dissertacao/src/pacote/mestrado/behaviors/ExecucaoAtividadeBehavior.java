package pacote.mestrado.behaviors;

import pacote.mestrado.Membro;
import pacote.mestrado.dominios.TipoEtapaNegociacao;
import pacote.mestrado.entidades.ControleMembro;
import jade.core.behaviours.SimpleBehaviour;

public class ExecucaoAtividadeBehavior extends SimpleBehaviour {
    private static final long serialVersionUID = 2605429686659053550L;
    
    private Membro membro;
    private boolean terminou;

    public ExecucaoAtividadeBehavior(Membro membro) {
	ControleMembro.getInstance().notifica(membro.getAID().getLocalName(), TipoEtapaNegociacao.EXECUCAO_ATIVIDADE);
	this.membro = membro;
	terminou = false;
    }

    @Override
    public void action() {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean done() {
	// TODO Auto-generated method stub
	return false;
    }

}
