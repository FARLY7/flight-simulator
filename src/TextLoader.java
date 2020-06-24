import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextLoader {

	public static final Texture Glass = loadTexture("Glass.jpg");
	public static final Texture Grass = loadTexture("Grass.jpg");
	public static final Texture Water = loadTexture("Water.jpg");
	public static final Texture Cockpit = loadTexture("Cockpit.png");
	public static final Texture Cockpit2 = loadTexture("Cockpit2.png");
	
	public TextLoader() {}
	
	public static Texture loadTexture(String name)
	{
		try {
			return TextureLoader.getTexture("jpg", new FileInputStream(new File("res/" + name)));
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) {e.printStackTrace();}
		return null;	
	}
}
