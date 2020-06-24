import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import java.util.ArrayList;
import java.util.Random;

public class World {

    public static float WORLD_SIZE = 400.0f;
    private static final int NO_OF_BUILDINGS = 700;    
    private ArrayList<WaterEffect> water;

    ArrayList<SimpleBuilding> buildings = new ArrayList<SimpleBuilding>();
    Random random = new Random();

    public World()
	{	    	
    	water = new ArrayList<WaterEffect>();	
    	generateWaterMass(25, 2, 25);
		generateRandomBuildings(0,0,0);	
	}
       
    public void generateWaterMass(float x, float y, float z)
    {    	
    	float tempZ = z;
    	
    	for(int i = 0 ; i < 5 ; i++, x += 8.35f)
    	{
    		z = tempZ;
        	for(int j = 0; j < 5 ; j++, z += 8.8f)
        	{	
        		water.add(new WaterEffect(x, y, z));
        	}
    	}
    }
	
	public void render()
	{
		renderGround(WORLD_SIZE);
		renderRunway();

		for(WaterEffect waterList : water) {
			waterList.renderScene();
		}
	}

	public void renderGround(float WorldLength)
	{	
			TextLoader.Grass.bind();
				Color.white.bind();
		        glBegin(GL_QUADS);
			        glTexCoord2f(0,0); glVertex3f(-WorldLength, 0, -WorldLength);
		        	glTexCoord2f(0,20); glVertex3f(WorldLength, 0, -WorldLength);
		        	glTexCoord2f(20,20); glVertex3f(WorldLength, 0, WorldLength);
		        	glTexCoord2f(20,0); glVertex3f(-WorldLength, 0, WorldLength);
		        glEnd();
    
        for( SimpleBuilding building : buildings){
        	building.render();
        }     
	}

	public void renderRunway()
	{
		glBegin(GL_QUADS);		
			//white border around runway
			glColor3f(1.0f, 1.0f, 1.0f);						
			glVertex3f( 1.1f, 0.05f,-50.1f);					
			glVertex3f(-1.1f, 0.05f,-50.1f);					
			glVertex3f(-1.1f, 0.05f, 50.1f);					
			glVertex3f( 1.1f, 0.05f, 50.1f);						
			
			//runway grey
			glColor3f(0.4f, 0.4f, 0.4f);						
			glVertex3f( 1.0f, 0.1f,-50.0f);					
			glVertex3f(-1.0f, 0.1f,-50.0f);					
			glVertex3f(-1.0f, 0.1f, 50.0f);					
			glVertex3f( 1.0f, 0.1f, 50.0f);
		
		
			//white lines in middle of runway
			glColor3f(1.0f,1.0f,1.0f);
			for(int i = -40 ; i < 50 ; i += 5)
			{							
				glVertex3f( 0.02f, 0.15f,-1.0f + i);					
				glVertex3f(-0.02f, 0.15f,-1.0f + i);					
				glVertex3f(-0.02f, 0.15f, 1.0f + i);					
				glVertex3f( 0.02f, 0.15f, 1.0f + i);
			}
			
			//red markers at end of runway
			glColor3f(1.0f,0.0f,0.0f);
			for(int i = 0, j = -1 ; i < 3 ; i++)
			{						
				glVertex3f( 0.02f + j, 0.1f,-1.0f +49);					
				glVertex3f(-0.02f + j, 0.1f,-1.0f +49);					
				glVertex3f(-0.02f + j, 0.1f, 1.0f +49);					
				glVertex3f( 0.02f + j, 0.1f, 1.0f +49);
				j+= 1;
			}
		glEnd();
	}
	
	public void generateRandomBuildings(float x, float y, float z)
	{	
		for(int i = 0 ; i < NO_OF_BUILDINGS ; i++)
		{
			float randx = (float) random.nextInt(150);
			float randz = (float) random.nextInt(150);
			float randHeight = random.nextFloat() * (float) random.nextInt(15) + 1;			
			float randWidth = (float) random.nextInt(2) + 3;
			float randDepth = (float) random.nextInt(2) + 3;

			if(random.nextInt(4) == 0){
				randx = -randx;
				randz = -randz;
			}
			else if(random.nextInt(4) == 1){
				randz = -randz;
			}
			else if(random.nextInt(4) == 2){
				randx = -randx;
			}
			
			buildings.add(new SimpleBuilding(randx + x, y, randz + z, randWidth, randDepth, randHeight));		
		}	
	}
}
