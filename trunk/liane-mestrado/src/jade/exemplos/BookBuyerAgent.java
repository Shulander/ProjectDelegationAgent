package jade.exemplos;

import jade.core.*;

public class BookBuyerAgent extends Agent
{
	protected void setup ()
	{
		//printout a welcome message
		System.out.println ("Hello! Buyer-agent "+getAID().getName()+" is ready.");
	}
}
