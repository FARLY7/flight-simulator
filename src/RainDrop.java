import static org.lwjgl.opengl.GL11.*;

public class RainDrop {
	
	private float x, y, z;
	private float height;	
	private boolean valid = false;
	
	public RainDrop(float x, float z, float height, float howHigh){
		this.x = x;
		this.z = z;
		this.height = height;
		valid = true;
		y = howHigh;
	}
	
	public void render()
	{		
		glBegin(GL_QUADS);
			glColor3f(1, 0, 0);
			glVertex3f(x, y , z+0.01f);
			glVertex3f(x, y + height, z+0.01f);
			glVertex3f(x+0.01f, y + height, z+0.01f);
			glVertex3f(x+0.01f, y , z+0.01f);
			// Back Face
			glVertex3f(x, y , z);
			glVertex3f(x, y + height, z);
			glVertex3f(x+0.01f, y + height, z);
			glVertex3f(x+0.01f, y , z);
			// Bottom Face
			 glVertex3f(x, y , z);
			glVertex3f(x, y , z+0.01f);
			glVertex3f(x, y + height, z+0.01f);
			glVertex3f(x, y + height, z);
			// Top Face
			glVertex3f(x+0.01f, y , z);
			glVertex3f(x+0.01f, y , z+0.01f);
			glVertex3f(x+0.01f, y + height, z+0.01f);
			glVertex3f(x+0.01f, y + height, z);
			// Left Face
			glVertex3f(x, y , z);
			glVertex3f(x+0.01f, y , z);
			glVertex3f(x+0.01f, y , z+0.01f);
			glVertex3f(x, y , z+0.01f);
			// Right Face
			glVertex3f(x, y + height, z);
			glVertex3f(x+0.01f, y + height, z);
			glVertex3f(x+0.01f, y + height, z+0.01f);
			glVertex3f(x, y + height, z+0.01f);	
		glEnd();
		
		if(y - height < 0) valid = false;
		y -= 0.5;

	}
	
	public boolean isValid(){
		return valid;
	}	
}

