import static org.lwjgl.opengl.GL11.*;

public class UserInterface {

	public UserInterface() { }
	
	public void render(float realV, float maxV)
	{
		glDisable(GL_BLEND);
		
    	glLoadIdentity();
    	
        float RScale = 1f - (realV / maxV);

        glColor3f(1f / 255f, 36f / 255f, 59f / 255f);
        glBegin(GL_QUADS);
            glVertex2f(32f, 200f);
            glVertex2f(64f, 200f);
            glVertex2f(64f, 32f);
            glVertex2f(32f, 32f);
        glEnd();

        glColor3f(157f / 255f, 167f / 255f, 178f / 255f);
        glBegin(GL_QUADS);
            if(RScale > 0.75) glColor3f(1.0f, 0, 0);
        	else if(RScale >= 0.25 && RScale <= 0.75) glColor3f(1.0f, 255/126f, 0);
        	else if(RScale >= 0 && RScale < 0.25 ) glColor3f(0, 1.0f, 0.25f);
            glVertex2f(37f, 195f);
            glVertex2f(59f, 195f);
            glVertex2f(59f, 37f + 158f * RScale);
            glVertex2f(37f, 37f + 158f * RScale);
        glEnd();
	}
}
