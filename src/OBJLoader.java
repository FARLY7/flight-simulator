import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.*;

public class OBJLoader {
	
	private static BufferedReader reader;	

	public static final Model plane = loadModel("Plane.obj");
	
	
	public OBJLoader() {}
	
	public static Model loadModel(String name)
	{
		try {
			reader = new BufferedReader(new FileReader(new File("res/" + name)));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		
		Model m = new Model();
		String line;
		
		try {
			
			while((line = reader.readLine()) != null)
			{
				if(line.startsWith("v "))
				{
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					m.vertices.add(new Vector3f(x,y,z));
				} else if(line.startsWith("vn "))
				{
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					m.normals.add(new Vector3f(x,y,z));
				}
				else if (line.startsWith("f "))
				{
					Vector3f vertexIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]), 
								Float.valueOf(line.split(" ")[2].split("/")[0]),
								Float.valueOf(line.split(" ")[3].split("/")[0]));
					
					Vector3f vertexNormals = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]), 
							Float.valueOf(line.split(" ")[2].split("/")[2]),
							Float.valueOf(line.split(" ")[3].split("/")[2]));
					
					m.Faces.add(new Face(vertexIndices, vertexNormals));	
				}
			}
		} catch (NumberFormatException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		
		return m;		
	}
}
	
	
/*
public static Model loadModel(File f) throws FileNotFoundException, IOException
{
    BufferedReader reader = new BufferedReader(new FileReader(f));

    Model m = new Model();
    String line;
    Texture currentTexture = null;
    while((line=reader.readLine()) != null)
    {
        if(line.startsWith("v "))
        {
            float x = Float.valueOf(line.split(" ")[1]);
            float y = Float.valueOf(line.split(" ")[2]);
            float z = Float.valueOf(line.split(" ")[3]);
            m.vertices.add(new Vector3f(x,y,z));
        }else if(line.startsWith("vn "))
        {
            float x = Float.valueOf(line.split(" ")[1]);
            float y = Float.valueOf(line.split(" ")[2]);
            float z = Float.valueOf(line.split(" ")[3]);
            m.normals.add(new Vector3f(x,y,z));
        }else if(line.startsWith("vt "))
        {
            float x = Float.valueOf(line.split(" ")[1]);
            float y = Float.valueOf(line.split(" ")[2]);
            m.texVertices.add(new Vector2f(x,y));

        }else if(line.startsWith("f "))
        {
            Vector3f vertexIndicies = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]), 
                    Float.valueOf(line.split(" ")[2].split("/")[0]), 
                    Float.valueOf(line.split(" ")[3].split("/")[0]));
            Vector3f textureIndicies = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[1]), 
                    Float.valueOf(line.split(" ")[2].split("/")[1]), 
                    Float.valueOf(line.split(" ")[3].split("/")[1]));
            Vector3f normalIndicies = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]), 
                    Float.valueOf(line.split(" ")[2].split("/")[2]), 
                    Float.valueOf(line.split(" ")[3].split("/")[2]));

            m.faces.add(new Face(vertexIndicies, textureIndicies, normalIndicies, currentTexture.getTextureID()));
        }else if(line.startsWith("g "))
        {
            if(line.length()>2)
            {
                String name = line.split(" ")[1];
                currentTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/" + name + ".png"));
                System.out.println(currentTexture.getTextureID());
            }
        }
    }


    reader.close();

    System.out.println(m.verticies.size() + " verticies");
    System.out.println(m.normals.size() + " normals");
    System.out.println(m.texVerticies.size() + " texture coordinates");
    System.out.println(m.faces.size() + " faces");
    return m;
}

*/





