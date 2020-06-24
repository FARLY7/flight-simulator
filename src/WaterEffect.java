import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;


public class WaterEffect {
	
	private final static int WATER_RESOLUTION = 45;
	private final static float WATER_DENSITY = 5.0f;

	private float points[][][] = new float[WATER_RESOLUTION][3][WATER_RESOLUTION];    
    private float xCoord, yCoord, zCoord;   
    private int frameCount = 0;
    private int waveEveryNFrames = 1;   // corresponds to "wiggle_count" in tutorial
    private Texture water;
    
    
    public WaterEffect(float x, float y, float z)
    {
    	water = TextLoader.Water;
    	this.xCoord = x;
    	this.yCoord = y;
    	this.zCoord = z;
    	
    	createWater();
    	drawWater();    	
    }  
    
	public void renderScene()
	{	
		if (++frameCount % waveEveryNFrames == 0) waveEffect();		
		drawWater();  
    }
	
	public void createWater()
	{
		float offsetX = (float) -WATER_RESOLUTION / 10;
		float offsetZ = (float) -WATER_RESOLUTION / 10;  
		
		for (int x = 0; x < WATER_RESOLUTION; x++)
		{       	
			float height = 0.3f * ((float) Math.sin(Math.toRadians(x / WATER_DENSITY * 40.0f)));
			
			for (int z = 0; z < WATER_RESOLUTION; z++)
			{                     
				points[x][0][z] = (x / WATER_DENSITY) + offsetX;
				points[x][1][z] = (z / WATER_DENSITY) + offsetZ;
				points[x][2][z] = height;
			}
		}
	}
	 
	public void waveEffect()
	{    
		int upper = WATER_RESOLUTION - 1;
		
		for (int z = 0 ; z < WATER_RESOLUTION ; z++)
		{
			float height = points[0][2][z];
			for (int x = 0 ; x < upper ; x++) 
			{
				points[x][2][z] = points[x + 1][2][z];
			}
			points[upper][2][z] = height;
		}     	        
	}
	 
	public void drawWater()
	{		 
		water.bind();
		float bound = (float) WATER_RESOLUTION - 1;
		
		//int displayListHandle = glGenLists(1);
		//glNewList(displayListHandle, GL_COMPILE);	
			      
				for (int x = 0 ; x < bound ; x++)
				{
					for (int z = 0 ; z < bound ; z++) 
					{	 
						float s1 = (float) (x) / bound;
						float t1 = (float) (z) / bound;
						float s2 = (float) (x + 1) / bound;
						float t2 = (float) (z + 1) / bound;
						
						glBegin(GL_QUADS);  
							glTexCoord2f(s1, t1);
							glVertex3f(points[x][0][z] + xCoord, points[x][2][z] + yCoord,  points[x][1][z] + zCoord);
							glTexCoord2f(s1, t2);
							glVertex3f(points[x][0][z + 1] + xCoord, points[x][2][z + 1] + yCoord, points[x][1][z + 1] + zCoord);
							glTexCoord2f(s2, t2);
							glVertex3f(points[x + 1][0][z + 1] + xCoord, points[x + 1][2][z + 1] + yCoord, points[x + 1][1][z + 1] + zCoord);
							glTexCoord2f(s2, t1);
							glVertex3f(points[x + 1][0][z] + xCoord, points[x + 1][2][z] + yCoord, points[x + 1][1][z] + zCoord); 
						glEnd();
					}
				}	       
			
		//glEndList();
		}	 
	}
