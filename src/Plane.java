import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.*;

public class Plane {

	public static final float MAX_SPEED = 0.035f;
	public static final float MIN_SPEED = 0;
	public static final float MAX_dSPEED = 0.001f;
	
	private Quaternion QResult;
	private Model model;
	
	private Vector3f position, forward, up, right;
	
	private boolean crashed;
	private boolean touchdown;
	
	public float speed, targetSpeed;
	public float pitch, dPitch;
	public float roll, dRoll;
	public float yaw, dYaw;
	
	private int displayListHandle;

	public Plane()
	{	
		QResult = new Quaternion();
		pitch = dPitch = 0;
		roll = dRoll = 0;
		yaw = dYaw = 0;
		speed = targetSpeed = 0;	
		position = new Vector3f(1, 5, 1);
        forward = new Vector3f(0, 0, 1);
        up = new Vector3f(0, 1, 0);
        right = new Vector3f(-1, 0, 0);       
        crashed = false;
        touchdown = true;
        model = OBJLoader.plane;
        init();
	}
	
	public void init()
	{
		displayListHandle = glGenLists(1);
		
		glNewList(displayListHandle, GL_COMPILE);	
			if(Game.SHOW_NORMALS)
			{
				glLineWidth(2.0f);
				glBegin(GL_LINES);
					/* Red - X */
					glColor3f(1, 0.5f, 0.5f);
					glVertex3f(0, 0, 0);
					glVertex3f(4, 0, 0);
					/* Blue - Y */	
					glColor3f(0.5f, 1, 0.5f);
					glVertex3f(0, 0, 0);
					glVertex3f(0, 4, 0);
					/* Green - Z */	
					glColor3f(0.5f, 0.5f, 1);
					glVertex3f(0, 0, 0);
					glVertex3f(0, 0, 4);
				glEnd();

			}
			
			glScalef(0.3f, 0.3f, 0.3f);
			glColor4f(1, 0, 0, 1);
			glBegin(GL_TRIANGLES);
				for (Face face : model.Faces)
				{
					Vector3f n1 = model.normals.get((int) face.normal.x - 1);
					glNormal3f(n1.x, n1.y, n1.z);
					Vector3f v1 = model.vertices.get((int) face.vertex.x - 1);
					glVertex3f(v1.x, v1.y, v1.z);
					Vector3f n2 = model.normals.get((int) face.normal.y - 1);
					glNormal3f(n2.x, n2.y, n2.z);
					Vector3f v2 = model.vertices.get((int) face.vertex.y - 1);
					glVertex3f(v2.x, v2.y, v2.z);
					Vector3f n3 = model.normals.get((int) face.normal.z - 1);
					glNormal3f(n3.x, n3.y, n3.z);
					Vector3f v3 = model.vertices.get((int) face.vertex.z - 1);
					glVertex3f(v3.x, v3.y, v3.z);
				}
			glEnd();		
		glEndList();
	}

	public void render()
	{				
		if(Game.CAMERA_VIEW != Game.COCKPIT)
		{	
			glPushMatrix();
				glTranslatef(position.x, position.y, position.z);

				float[] QMatrix = new float[16];
				createMatrix(QMatrix, QResult);
					        
				FloatBuffer Buffer = BufferUtils.createFloatBuffer(16);
				Buffer.put(QMatrix);
				Buffer.position(0);
					        
				glMultMatrix(Buffer);
				
				glCallList(displayListHandle);

			glPopMatrix();
		}		
	}
	
	public void update(int delta)
	{		      
		smoothSpeedChange();

		pitch += dPitch;
		roll += dRoll;	  
		yaw += dYaw;
		
		/**  =========== PITCH =========== **/
		forward.scale( (float) Math.cos(dPitch) );
		up.scale( (float) Math.sin(dPitch) );
		forward = Vector3f.add(forward, up, null);
		up = Vector3f.cross(right, forward, null);
		forward.normalise();
		up.normalise();

		/**  =========== ROLL =========== **/
		right.scale( (float) Math.cos(dRoll) );
		up.scale( (float) Math.sin(dRoll) );	
		right = Vector3f.add(right, up, null);
		up = Vector3f.cross(right, forward, null);
		right.normalise();
		up.normalise();

		/**  =========== YAW =========== **/
		forward.scale( (float) Math.cos(dYaw) );
		right.scale( (float) -Math.sin(dYaw) );		
		forward = Vector3f.add(forward, right, null);
		right = Vector3f.cross(forward, up, null);
		right.normalise();
		forward.normalise();	

		/** ROLL **/
		Quaternion QRoll = new Quaternion();
		QRoll.setFromAxisAngle(new Vector4f(forward.x, forward.y, forward.z, dRoll));
		
		/** PITCH **/
		Quaternion QPitch = new Quaternion();
		QPitch.setFromAxisAngle(new Vector4f(right.x, right.y, right.z, -dPitch));
		 
		/** YAW **/
		Quaternion QYaw = new Quaternion();
		QYaw.setFromAxisAngle(new Vector4f(up.x, up.y, up.z, -dYaw));

		Quaternion.mul(QResult, QRoll, QResult);
		Quaternion.mul(QResult, QPitch, QResult);
		Quaternion.mul(QResult, QYaw, QResult); //
		QResult.normalise();
		  
		Vector3f ForwardCopy = new Vector3f(forward);
        ForwardCopy.scale(speed*delta);		
		Vector3f.add(position, ForwardCopy, position);
		
		crashCollisionDetection();
	}

	private void smoothSpeedChange()
	{
		if(targetSpeed >= MAX_SPEED)
			targetSpeed = MAX_SPEED;
		else if(targetSpeed <= MIN_SPEED)
			targetSpeed = MIN_SPEED;       
		
		/* Close enough to either limit */
		if(speed / MAX_SPEED > 1) speed = MAX_SPEED;
		if(speed < 0.0000000001) speed = MIN_SPEED;  
		
		if(targetSpeed > speed)
			speed += MAX_dSPEED * 0.3f;
		else if(targetSpeed < speed)
			speed -= MAX_dSPEED * 0.3f;
	}
	
	private void crashCollisionDetection()
	{	
		if(position.getY() <= 0.3f)
		{
			/* Runway Bounds */
			if( !(position.getX() <= 1 && position.getX() >= -1 && position.getZ() <= 50 && position.getZ() >= -50) )
			{
				crashed = true;	
			}
			position.setY(0.3f);
		}
		else{
			for(int i = 0; i < SimpleBuilding.crash.size(); i+=6){
				if((getPosition().getX() >= SimpleBuilding.crash.get(i) && getPosition().getX() <= SimpleBuilding.crash.get(i+3)) && 
						(getPosition().getY() <= SimpleBuilding.crash.get(i+1) && getPosition().getY() >= SimpleBuilding.crash.get(i+4)) &&
							(getPosition().getZ() >= SimpleBuilding.crash.get(i+2) && getPosition().getZ() <= SimpleBuilding.crash.get(i+5))){
					crashed = true;	
				}
			}
		}	
	}
	
    public void createMatrix(float[] pMatrix, Quaternion q)
    {
        pMatrix[ 0] = 1.0f - 2.0f * ( q.y * q.y + q.z * q.z );
        pMatrix[ 1] = 2.0f * ( q.x * q.y - q.w * q.z );
        pMatrix[ 2] = 2.0f * ( q.x * q.z + q.w * q.y );
        pMatrix[ 3] = 0.0f;
        pMatrix[ 4] = 2.0f * ( q.x * q.y + q.w * q.z );
        pMatrix[ 5] = 1.0f - 2.0f * ( q.x * q.x + q.z * q.z );
        pMatrix[ 6] = 2.0f * ( q.y * q.z - q.w * q.x );
        pMatrix[ 7] = 0.0f;
        pMatrix[ 8] = 2.0f * ( q.x * q.z - q.w * q.y );
        pMatrix[ 9] = 2.0f * ( q.y * q.z + q.w * q.x );
        pMatrix[10] = 1.0f - 2.0f * ( q.x * q.x + q.y * q.y );
        pMatrix[11] = 0.0f;
        pMatrix[12] = 0;
        pMatrix[13] = 0;
        pMatrix[14] = 0;
        pMatrix[15] = 1.0f;
    }

    public void GetCameraVectors(Vector3f CameraPos, Vector3f CameraTarget, Vector3f CameraUp)
    {
        CameraPos.set(position.x, position.y, position.z);
        CameraTarget.set(forward.x + position.x, forward.y + position.y, forward.z + position.z);
        CameraUp.set(up.x, up.y, up.z);
    }
    
	public void addX(float x) {
		position.setX(position.getX() + x);
	}
	public void addY(float y) {
		position.setY(position.getY() + y);
	}
	public void addZ(float z) {
		position.setZ(position.getZ() + z);
	}
	public boolean hasCrashed() {
		return crashed;
	}
	public Vector3f getPosition() {
		return position;
	}
	
}
