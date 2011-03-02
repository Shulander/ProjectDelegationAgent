package jade.exemplos;

import jade.core.*;

public class Produtor extends Agent 
{
	protected void setup () 
	{
		double produto = produz ();
		//se buffer esta cheio, dorme
		//se nao, coloca item no buffer
		
	}
	
	double produz ()
	{
		return Math.random();
	}

}
