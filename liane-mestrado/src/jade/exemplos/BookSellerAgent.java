package jade.exemplos;

import java.util.Hashtable;

import jade.core.*;

public class BookSellerAgent extends Agent 
{
	//the catalogue of books for sale (maps the title of a book to its price)
	private Hashtable catalogue;
	//the GUI by means of which the user can add books in the catalogue
	private BookSellerGui myGui;

	//put agent initializations here
	protected void setup ()
	{
		//create the catalogue
		catalogue = new Hashtable();
		
		//create and show the GUI
		myGui = new BookSellerGui (this);
		myGui.show ();
		
		//add the behaviour serving requests for offer from buyer agents
		addBehaviour(new OfferRequestsServer());
		
		//add the behaviour serving purchase orders from buyer agents
		addBehaviour(new PurchaseOrdersServer());
	}
	
	//put agent clean-up operations here
	protected void takeDown() 
	{
		//close the GUI
		myGui.dispose ();
		//printout a dismissal message
		System.out.println ("Seller-agent "+getAID().getName()+ " terminating.");
	}

}
