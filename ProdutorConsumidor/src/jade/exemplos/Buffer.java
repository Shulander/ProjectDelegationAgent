package jade.exemplos;

import java.util.Vector;
import jade.core.*;

public class Buffer extends Agent 
{
	private Vector buffer;
	private static final int TAM_MAX = 10;
	
	protected void setup () 
	{
		//se buffer esta quase cheio (MAX-1), acorda produtor
		//se buffer tem pelo menos um item, acorda consumidor
	}
	
	public static int getTamMax() 
	{
		return TAM_MAX;
	}
}
