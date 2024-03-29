package examples.ex1;

import jade.core.*;

public class BookBuyerAgent extends Agent 
{
	//The title of the book to buy
	private String targetBookTitle;
	//The list ok known seller agents
	private AID[] selletAgents = 
		{new AID ("seller1", AID.ISLOCALNAME),
		new AID ("seller2", AID.ISLOCALNAME)};
	//Put agents initializations here
	protected void setup ()
	{
		//Printout a welcome message
		System.out.println("Hello! BookBuyerAgent " +getAID().getName()+ " is ready!");
		
		//Get the title of the book to buy as a start-up argument
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			targetBookTitle = (String) args[0];
			System.out.println ("Trying to buy "+targetBookTitle);
		} else {
			//Make the agent terminate immediately
			System.out.println("No book title specified!");
			doDelete();
		}
	}
	
	//Put agent clean-up operations here
	protected void takeDown ()
	{
		//Printout a dismissal message
		System.out.println("Buyer-agent " +getAID().getName()+ " is terminating.");
	}
}
