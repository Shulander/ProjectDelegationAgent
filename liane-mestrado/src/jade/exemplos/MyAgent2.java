package jade.exemplos;

import jade.core.*;
import jade.core.behaviours.TickerBehaviour;

public class MyAgent2 extends Agent
{
	protected void setup ()
	{
		System.out.println ("Adding ticker behaviour");
		addBehaviour(new TickerBehaviour(this, 1000) 
			{
				int i = 0;
				protected void onTick () {					
					//perform operation Y
					if (i < 10) {
						System.out.println ("Olá "+(i+1)+ " :)");
						i++;
					} else {
						System.out.println ("Tchau :(");
						doDelete();
					}
				}
			}
		);
	}
}
