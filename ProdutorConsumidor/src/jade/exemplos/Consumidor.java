package jade.exemplos;

import jade.core.*;

public class Consumidor extends Agent
{
	protected void setup () 
	{
		//pergunta se o buffer esta vazio
		//??
		doWait (); //dorme
		//se nao, consome do buffer
	}

}
