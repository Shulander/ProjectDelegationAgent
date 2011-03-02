package jade.exemplos;

import jade.core.*;

public class BookBuyerAgent3 extends Agent 
{
	//the title of the book to buy
	private String targetBookTitle;
	////The list of known seller agents
	private AID[] sellerAgents = {
			new AID ("seller1", AID.ISLOCALNAME),
			new AID ("seller2", AID.ISLOCALNAME)
	};
	
	//put agent initializations here
	protected void setup ()
	{
		//printout a welcome message
		System.out.println ("Hello! Buyer-agent " +getAID().getName()+ " is ready.");
		//get the title of the book to buy as a start-up argument
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			targetBookTitle = (String) args[0];
			System.out.println ("Trying to buy " +targetBookTitle);
		} else {
			//make the agent terminate immediately
			System.out.println ("No book title specified!");
			doDelete();
		}
	}
	
	//put agent clean-up operations here
	protected void takeDown ()
	{
		//printout a dismissal message
		System.out.println ("Buyer-agent " +getAID().getName()+ " terminating.");
	}
}
