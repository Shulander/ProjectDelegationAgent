package jade.exemplos;

import jade.core.*;
import jade.core.behaviours.WakerBehaviour;

public class MyAgent extends Agent
{
	protected void setup ()
	{
		System.out.println ("Adding waker behaviour");
		addBehaviour(new WakerBehaviour(this, 10000) 
			{
				protected void handleElapsedTimeout () {
					//perform operation X
					System.out.println ("Blá :)");
				}
			}
		);
	}
}
