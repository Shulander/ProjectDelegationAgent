package jade.exemplos;

import jade.core.*;

public class BookBuyerAgent2 extends Agent
{
	protected void setup ()
	{
		String nickname = "Peter";
		AID id = new AID (nickname, AID.ISLOCALNAME);
		//printout a welcome message
		System.out.println ("Hello! Buyer-agent "+id.getLocalName()+" is ready.");
	}

}
