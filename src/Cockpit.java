import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;

public class Cockpit {

	public static int COCKPIT = 1;
	private int displayListHandle;
	
	
	public Cockpit() {
		init();
	}

	
	public void init(){
		
		
		displayListHandle = glGenLists(1);
		glNewList(displayListHandle, GL_COMPILE);	
		//glPushMatrix();
			Color.white.bind();
			if(COCKPIT == 1)TextLoader.Cockpit.bind();
			else if(COCKPIT == 2) TextLoader.Cockpit2.bind();
			glBegin(GL_QUADS);			
				glTexCoord2f(0,0);
				glVertex2f(0, 0);
				glTexCoord2f(1,0);
				
				if(COCKPIT == 1)
				{
					glVertex2f(TextLoader.Cockpit.getTextureWidth(), 0);
					glTexCoord2f(1, 1);
					glVertex2f(TextLoader.Cockpit.getTextureWidth(), TextLoader.Cockpit.getTextureHeight());
					glTexCoord2f(0, 1);
					glVertex2f(0, TextLoader.Cockpit.getTextureHeight());	
				}
				else if(COCKPIT == 2)
				{
					glVertex2f(TextLoader.Cockpit2.getTextureWidth(), 0);
					glTexCoord2f(1, 1);
					glVertex2f(TextLoader.Cockpit2.getTextureWidth(), TextLoader.Cockpit2.getTextureHeight());
					glTexCoord2f(0, 1);
					glVertex2f(0, TextLoader.Cockpit2.getTextureHeight());	
				}
			glEnd();
		//glPopMatrix();
		
	}
	
	public void render()
	{	
		if(Game.CAMERA_VIEW == Game.COCKPIT)
		{	
			glLoadIdentity();
			glPushMatrix();
			glCallList(displayListHandle);
		glPopMatrix();
		}
	}

}
