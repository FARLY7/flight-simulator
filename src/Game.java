import java.nio.FloatBuffer;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import static org.lwjgl.util.glu.GLU.*;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;


public class Game {

	
	/**
	 * TODO:
	 * 
	 * 		Implement proper textures.
	 * 		Sort out rain.
	 * 		Fractal trees.
	 * 		Roads
	 * 		Highly reflective windows/water.
	 * 		Restrict speed to always > 0 when flying.
	 * 		Little moving cars across bridge.
	 */
	
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 750;
	private static final float FOV = 70;
	private static final float ASPECT_RATIO = (float) WIDTH / (float) HEIGHT;
	private static final float NEAR_CLIPPING = 0.1f;
	private static final float FAR_CLIPPING = 2000f;
	private static final float[] DAYTIME = {107/255f, 166/255f, 205/255f, 0/255f};
	private static final float[] NIGHTIME = {33/255f, 50/255f, 80/255f, 0/255f};
	
	private static final float FOG_DENSITY = 0.008f;
	
	private static boolean FULLSCREEN;
	public static boolean SHOW_NORMALS;
	private static boolean SHOW_FOG;
	private static boolean SHOW_LIGHTING;
	private static boolean SHOW_RAIN;
	private static boolean DAY;
	private static float[] SKY_COLOUR;
	private static int GRAPHICS_TYPE = GL_NICEST;
	public static int CAMERA_VIEW = 3;
	public static final int PLANE = 0, COCKPIT = 1, TRACKER = 2, START = 3, LEFT = 4, RIGHT = 5, REVERSE = 6;
	
	/* Lighting */
	private float lightPosition[] = { 0f, 100f, 30f, 1 };
	private float lightAmbient[] = { 0.5f, 0.5f, 0.5f, 1 };
	private float lightDiffuse[] = { 1f, 1f, 1f, 1 };
	private float lightSpecular[] = { 1f, 1f, 1f, 1 };
	private float i = 0;
	
	/* Smooth Out Movement */
	int j = 0; int k = 0; int l = 0;
	
	private World world;
	private Plane plane;
	private UserInterface UI;
	private Cockpit cockpit;
	private Rain rain;
	
	/* Ship / Camera Variables */
    Vector3f CameraPos = new Vector3f();
    Vector3f CameraTarget = new Vector3f();
    Vector3f CameraUp = new Vector3f();
    Vector3f lastCamPos = new Vector3f();
    private float time;
	private long lastFrame;
	private int delta;
	
	public Game(boolean fog, boolean lighting, boolean rain, boolean day, boolean fullscreen, boolean normals, int graphics)
	{		
		SHOW_FOG = fog;
		SHOW_LIGHTING = lighting;
		SHOW_RAIN = rain;
		SHOW_NORMALS = normals;
		GRAPHICS_TYPE = graphics;
		FULLSCREEN = fullscreen;
		DAY = day;
		
		try {
			Display.setTitle("Flight Simulator: Sean Farrelly, Colin McNicol, David McNicol");
			Display.setVSyncEnabled(true);			
			if(FULLSCREEN)
				Display.setFullscreen(true);
			else
				Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));			
			Display.create();
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) { e.printStackTrace(); System.exit(0); }	
		
		init();
		
		/** ====== The Game Loop ====== **/
		while(!Display.isCloseRequested())
		{	
			update();
			render();
			Display.update();
			Display.sync(60);		
		}
		/** =========================== **/
		
		cleanUp();
	}
	
	public void cleanUp() {
		Display.destroy();	
	}

	public void init()
	{		   
        lastFrame = getTime();
		world = new World();
		plane = new Plane();
		UI = new UserInterface();
		cockpit = new Cockpit();
		if(SHOW_RAIN) rain = new Rain();
		if(DAY) SKY_COLOUR = DAYTIME;
		else SKY_COLOUR = NIGHTIME;
		time = 0;	
			
		glClearColor(SKY_COLOUR[0], SKY_COLOUR[1], SKY_COLOUR[2], SKY_COLOUR[3]);
		
		//glClearColor(0.1f, 0.1f, 0.1f, 1);
		
		/** ========== SET UP GRAPHICS QUALITY ========= **/
		glHint(GL_FOG_HINT, GRAPHICS_TYPE);
		glHint(GL_LINE_SMOOTH_HINT, GRAPHICS_TYPE);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GRAPHICS_TYPE);
		glHint(GL_POINT_SMOOTH_HINT, GRAPHICS_TYPE);
		glHint(GL_POLYGON_SMOOTH_HINT, GRAPHICS_TYPE);
		/** ============================================ **/
		
		/** ================ SET UP FOG ================ **/
		if(SHOW_FOG) {
		glFogi(GL_FOG_MODE, GL_EXP2);
		glFogf(GL_FOG_DENSITY, FOG_DENSITY);
		glFogf(GL_FOG_START, World.WORLD_SIZE);
		glFogf(GL_FOG_END, World.WORLD_SIZE * 2);
		glFog(GL_FOG_COLOR, asFloatBuffer(SKY_COLOUR));
		glEnable(GL_FOG); }
		/** ============================================= **/
		
		/** ============== SET UP LIGHTING ============== **/
		if(!DAY) {
			lightAmbient[0] = 0.5f; lightAmbient[1] = 0.5f; lightAmbient[2] = 0.5f;
			lightDiffuse[0] = 0.5f; lightDiffuse[1] = 0.5f; lightDiffuse[2] = 0.5f;
			lightSpecular[0] = 0; lightSpecular[1] = 0; lightSpecular[2] = 0;
		}
		glLight(GL_LIGHT0, GL_AMBIENT, asFloatBuffer(lightAmbient));
		glLightModel(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(lightAmbient));
		if(SHOW_LIGHTING) {
			glLight(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(lightDiffuse));
			glLight(GL_LIGHT0, GL_SPECULAR, asFloatBuffer(lightSpecular));
		}	
		glLightModel(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(lightAmbient));
		glEnable(GL_DEPTH_TEST);
	    glDepthFunc(GL_LESS);
		
		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHT0);  
		glEnable(GL_LIGHTING);		
		/** ============================================= **/	
	}
	
	public static FloatBuffer asFloatBuffer(float[] values) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
	
	public void update()
	{
		delta = getDelta();
		if(plane.hasCrashed())
		{
			plane = new Plane();
			
			plane.GetCameraVectors(CameraPos, CameraTarget, CameraUp);	
			
			Vector3f Dir = new Vector3f();
			Vector3f.sub(CameraPos, CameraTarget, Dir);
			Dir.normalise();
			Dir.scale(4);
			Dir.y += 0.1f;
			Vector3f.add(CameraPos, Dir, CameraPos);
			CameraPos.y += 0.5;
	        if(CameraPos.y < 0.1f) CameraPos.y = 0.1f;
	        
	        lastCamPos = new Vector3f(CameraPos);
		}
		checkInput();
		plane.update(delta);
	}
	
	public void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		
		init3DProjection();

        plane.GetCameraVectors(CameraPos, CameraTarget, CameraUp);
        Vector3f Dir;
        time += 0.01f;
        
        switch(CAMERA_VIEW)
        {   
        	case START: gluLookAt(10 * (float) Math.cos(time), 10, 10 * (float) Math.sin(time),
        							CameraPos.x, CameraPos.y, CameraPos.z, 0, 1, 0); break;
        
        	case PLANE:
				Dir = new Vector3f();
				Vector3f.sub(CameraPos, CameraTarget, Dir);
				Dir.normalise();
				Dir.scale(3);
				Dir.y += 0.1f;
				Vector3f.add(CameraPos, Dir, CameraPos);
				CameraPos.y += 0.5;
		        if(CameraPos.y < 0.1f) CameraPos.y = 0.1f;
				
				gluLookAt(CameraPos.x, CameraPos.y, CameraPos.z,
						  CameraTarget.x, CameraTarget.y, CameraTarget.z,
						  CameraUp.x, CameraUp.y, CameraUp.z); break;
        	case COCKPIT:
                Dir = new Vector3f();
                Vector3f.sub(CameraPos, CameraTarget, Dir);
                Dir.normalise();
                Vector3f.add(CameraPos, Dir, CameraPos);
                CameraPos.y += 0.4;
                if(CameraPos.y < 0.1f) CameraPos.y = 0.1f;
                
                gluLookAt(CameraPos.x, CameraPos.y, CameraPos.z,
                		  CameraTarget.x, CameraTarget.y, CameraTarget.z,
                		  CameraUp.x, CameraUp.y, CameraUp.z); break;			  
        	
        	case TRACKER: gluLookAt( lastCamPos.x, lastCamPos.y , lastCamPos.z,
        								CameraPos.x, CameraPos.y, CameraPos.z, 0, 1, 0); break;
        								
        	case LEFT: break;
        		
        	case RIGHT: break;
        }
        
        glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(lightPosition));
        
		world.render();	
		plane.render();
		if(SHOW_RAIN) rain.render(plane.getPosition());
		init2DProjection();
		if(CAMERA_VIEW == COCKPIT) cockpit.render();
		UI.render(plane.speed, Plane.MAX_SPEED);	
	}

	
	public void init3DProjection()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(FOV, ASPECT_RATIO, NEAR_CLIPPING, FAR_CLIPPING);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);	
		glDisable(GL_BLEND);
		glEnable(GL_LIGHTING);
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
	}

	public void init2DProjection()
	{
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);	
		glDisable(GL_LIGHTING);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D(0.0f, (float) WIDTH, (float) HEIGHT, 0.0f);
		glMatrixMode(GL_MODELVIEW);
		glDisable(GL_DEPTH_TEST);
	}
	
	public void checkInput()
	{						
		/* ================== MOVEMENT ================== */
		if(!(CAMERA_VIEW == START))
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
	            plane.dPitch -= 0.00015 * delta;
				j = 90; }
	        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
	            plane.dPitch += 0.00015 * delta;
	        	j = 90; }
	        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
	            plane.dRoll += 0.00015 * delta;
	        	k = 90; }
	        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
	            plane.dRoll -= 0.00015 * delta;
	        	k = 90; }
	        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
	        	plane.dYaw += 0.00003 * delta;
	        	l = 90; }
	        if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
	        	plane.dYaw -= 0.00003 * delta;
	        	l = 90; }
	        if(Keyboard.isKeyDown(Keyboard.KEY_ADD))
	            plane.targetSpeed += Plane.MAX_dSPEED / 1.5;          
	        if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT))
	            plane.targetSpeed -= Plane.MAX_dSPEED / 1.5 ;
		}
						
		/* ================= CHANGE VIEWS ================ */
		if(Keyboard.isKeyDown(Keyboard.KEY_1))
		{
			lastCamPos = new Vector3f();
			CAMERA_VIEW = PLANE;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_2))
		{
			CAMERA_VIEW = COCKPIT;
			Cockpit.COCKPIT = 1;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_3))
		{
			CAMERA_VIEW = COCKPIT;
			Cockpit.COCKPIT = 2;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_4))
		{			
			Vector3f.sub(CameraPos, CameraTarget, lastCamPos);
			lastCamPos.normalise();
			lastCamPos.scale(3);
			lastCamPos.y += 0.1f;
			Vector3f.add(CameraPos, lastCamPos, lastCamPos);
			lastCamPos.y += 0.5;
			time = 0;
			CAMERA_VIEW = TRACKER;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6))
		{
			CAMERA_VIEW = RIGHT;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7))
		{
			CAMERA_VIEW = LEFT;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8))
		{
			CAMERA_VIEW = REVERSE;
		}
			
		/* =========== QUIT GAME =========== */
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			Display.destroy();
			System.exit(0);
		}			
		
		/* Testing the light source */
		if(Keyboard.isKeyDown(Keyboard.KEY_L)){
			lightPosition[0] = 20f+i;
			i++;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
		}			
		smoothOutMovement();
	}	
	
	private void smoothOutMovement()
	{
		plane.dPitch *= (float) Math.sin(Math.toRadians(j)) * (float) 0.9;		
		plane.dRoll *= (float) Math.sin(Math.toRadians(k)) * (float) 0.9;
		plane.dYaw *= (float) Math.sin(Math.toRadians(l)) * (float) 0.9;
		
		if(j >= 90 && j < 180) j++;
		else { j = 0; plane.dPitch = 0; }
		
		if(k >= 90 && k < 180) k++;
		else { k = 0; plane.dRoll = 0; }
		
		if(l >= 90 && l < 180) l++;
		else { l = 0; plane.dYaw = 0; }
	}
	
	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}
}
