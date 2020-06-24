import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.util.vector.Vector3f;

public class Rain {
	
	public ArrayList<RainDrop> rDrop = new ArrayList<RainDrop>();
	private static final int NO_OF_RAINDROPS = 10000;
	
	Random random = new Random();
//	private int displayListHandle;
	
	public Rain() {
		generateRain();
	}
	
	public void render(Vector3f planePos)
	{
		for(int i = 0; i < rDrop.size(); i++)
		{
			if(!rDrop.get(i).isValid())
			{
				rDrop.remove(i);
				addRaindrop(planePos);
			}
			else rDrop.get(i).render();	
		}
	}
	
//	
//	public void init()
//	{
//		displayListHandle = glGenLists(1);	
//		glNewList(displayListHandle, GL_COMPILE);
//		
//		
//		
//		glEndList();
//	}
	
	
	public void addRaindrop(Vector3f pos)
	{				
		float randx = random.nextFloat() * (float) random.nextInt(120);
		float randz = random.nextFloat() * (float) random.nextInt(120);
		
		float randHeight = ( random.nextFloat() * random.nextFloat() ) / 1.7f; 

		int rand = random.nextInt(4);
		
		if(rand == 0) {
			randx = -randx;
			randz = -randz;
		}
		else if(rand == 1) randz = -randz;
		else if(rand == 2) randx = -randx;

		randx = randx + pos.getX();
		randz = randz + pos.getZ();
		
		float howHigh = 30f + (float) random.nextInt(20);
		
		rDrop.add(new RainDrop(randx, randz, randHeight, howHigh));	
	}
	
	public void generateRain()
	{	
		for(int i = 0 ; i < NO_OF_RAINDROPS ; i++)
		{
			addRaindrop(new Vector3f(1, 5, 1));
		}	
	}
	
}
