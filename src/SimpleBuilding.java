import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

public class SimpleBuilding {

	private float x, y, z, height, width, depth;
	private int displayListHandle;
	public static ArrayList<Float> crash = new ArrayList<Float>();

	public SimpleBuilding(float x, float y, float z, float width, float depth, float height) {
		this.x = x;
		this.z = z;
		this.y = y;
		this.height = height;
		this.width = width;
		this.depth = depth;
		
		/*top left front point*/
		crash.add(this.x);
		crash.add(this.height);
		crash.add(this.z);
				
		/*bottom right back point*/
		crash.add(this.x + this.width);
		crash.add(0.001f);
		crash.add(this.z + this.depth);
		
		init();		
	}

	public void render()
	{
		glPushMatrix();
			glCallList(displayListHandle);
		glPopMatrix();
	}
	
	public void init()
	{		
		displayListHandle = glGenLists(1);
		glNewList(displayListHandle, GL_COMPILE);	
			TextLoader.Glass.bind();
			glBegin(GL_QUADS);
				// Front Face
				glNormal3f(0,0,1);
				glTexCoord2f(0,0); glVertex3f(x, y+0.001f, z+depth);
				glTexCoord2f(0,1); glVertex3f(x, y+height, z+depth);
				glTexCoord2f(1,1); glVertex3f(x+width, y+height, z+depth);
				glTexCoord2f(1,0); glVertex3f(x+width, y+0.001f, z+depth);
				// Back Face
				glNormal3f(0,0,-1);
				glTexCoord2f(0,0); glVertex3f(x, y+0.001f, z);
				glTexCoord2f(0,1); glVertex3f(x, y+height, z);
				glTexCoord2f(1,1); glVertex3f(x+width, y+height, z);
				glTexCoord2f(1,0); glVertex3f(x+width, y+0.001f, z);		
				// Left Face
				glNormal3f(0,-1,0);
				glTexCoord2f(0,0); glVertex3f(x, y+0.001f, z);
				glTexCoord2f(0,1); glVertex3f(x, y+0.001f, z+depth);
				glTexCoord2f(1,1); glVertex3f(x, y+height, z+depth);
				glTexCoord2f(1,0); glVertex3f(x, y+height, z);
				// Right Face
				glNormal3f(0,1,0);
				glTexCoord2f(0,0); glVertex3f(x+width, y+0.001f, z);
				glTexCoord2f(0,1); glVertex3f(x+width, y+0.001f, z+depth);
				glTexCoord2f(1,1); glVertex3f(x+width, y+height, z+depth);
				glTexCoord2f(1,0); glVertex3f(x+width, y+height, z);
				// Bottom Face
				glNormal3f(-1,0,0);
				glTexCoord2f(0,0); glVertex3f(x, y+0.001f, z);
				glTexCoord2f(0,1); glVertex3f(x+width, y+0.001f, z);
				glTexCoord2f(1,1); glVertex3f(x+width, y+0.001f, z+depth);
				glTexCoord2f(1,0); glVertex3f(x, y+0.001f, z+depth);
				// Top Face
				glNormal3f(1,0,0);
				glTexCoord2f(0,0); glVertex3f(x, y+height, z);
				glTexCoord2f(0,1); glVertex3f(x+width, y+height, z);
				glTexCoord2f(1,1); glVertex3f(x+width, y+height, z+depth);
				glTexCoord2f(1,0); glVertex3f(x, y+height, z+depth);
			glEnd();
		glEndList();		
	}

}
