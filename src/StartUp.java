import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.lwjgl.opengl.GL11;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StartUp extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnNicest;
	private JRadioButton rdbtnDontCare;
	private JRadioButton rdbtnFastest;
	private JButton btnNewButton;
	private JLabel lblInstructions;
	private JCheckBox chckbxFog;
	private JCheckBox chckbxLighting;
	private JCheckBox chckbxFullscreen;
	private JCheckBox chckbxShowNormals;
	private JCheckBox chckbxShowRain;
	private JRadioButton rdbtnDay;
	private JRadioButton rdbtnNight;
	private JLabel lblScene;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartUp frame = new StartUp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public void runGame()
	{
		boolean fog, lighting, fullscreen, normals, rain, day;	
		int graphics = 0;

		if(buttonGroup.isSelected( rdbtnNicest.getModel())) graphics = GL11.GL_NICEST;
		else if(buttonGroup.isSelected( rdbtnDontCare.getModel())) graphics = GL11.GL_DONT_CARE;
		else if(buttonGroup.isSelected( rdbtnFastest.getModel())) graphics = GL11.GL_FASTEST;
		
		if(buttonGroup_1.isSelected(rdbtnDay.getModel())) day = true;
		else day = false;
		
		
		fog = chckbxFog.isSelected();
		lighting = chckbxLighting.isSelected();
		fullscreen = chckbxFullscreen.isSelected();
		normals = chckbxShowNormals.isSelected();
		rain = chckbxShowRain.isSelected();	
		
		/** Create the game **/
		Game game = new Game(fog, lighting, rain, day, fullscreen, normals, graphics);
		
		/** Cleanup **/
		System.exit(0);
	}

	public StartUp()
	{	
		setTitle("Java Flight Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnNewButton = new JButton("Start Game");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runGame();				
			}
		});
		btnNewButton.setBounds(298, 192, 107, 43);
		contentPane.add(btnNewButton);		
		
		
		/** =============== GRAPHICS =============== **/
		JLabel lblGraphics = new JLabel("Graphics");
		lblGraphics.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblGraphics.setBounds(24, 11, 75, 23);
		contentPane.add(lblGraphics);
		
		rdbtnNicest = new JRadioButton("Nicest");
		rdbtnNicest.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnNicest.setSelected(true);
		buttonGroup.add(rdbtnNicest);
		rdbtnNicest.setBounds(24, 35, 61, 23);
		contentPane.add(rdbtnNicest);
		
		rdbtnDontCare = new JRadioButton("Dont Care");
		rdbtnDontCare.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonGroup.add(rdbtnDontCare);
		rdbtnDontCare.setBounds(93, 35, 89, 23);
		contentPane.add(rdbtnDontCare);
		
		rdbtnFastest = new JRadioButton("Fastest");
		rdbtnFastest.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonGroup.add(rdbtnFastest);
		rdbtnFastest.setBounds(189, 35, 69, 23);
		contentPane.add(rdbtnFastest);
		/** ======================================== **/
		
		/** ================== FOG ================= **/
		chckbxFog = new JCheckBox("Enable Fog");
		chckbxFog.setSelected(true);
		chckbxFog.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chckbxFog.setBounds(284, 23, 97, 23);
		contentPane.add(chckbxFog);
		
		/** =============== LIGHTING =============== **/
		chckbxLighting = new JCheckBox("Enable Lighting");
		chckbxLighting.setSelected(true);
		chckbxLighting.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chckbxLighting.setBounds(284, 49, 121, 23);
		contentPane.add(chckbxLighting);
		
		/** ============== FULLSCREEN ============== **/
		chckbxFullscreen = new JCheckBox("Full Screen");
		chckbxFullscreen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chckbxFullscreen.setBounds(284, 101, 97, 23);
		contentPane.add(chckbxFullscreen);
		
		/** =============== NORMALS =============== **/
		chckbxShowNormals = new JCheckBox("Show Normals");
		chckbxShowNormals.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chckbxShowNormals.setBounds(284, 127, 114, 23);
		contentPane.add(chckbxShowNormals);
		
		/** ============ INSTRUCTIONS ============= **/
		lblInstructions = new JLabel("Instructions: ");
		lblInstructions.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblInstructions.setBounds(24, 111, 89, 23);
		contentPane.add(lblInstructions);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 145, 247, 106);
		contentPane.add(scrollPane);
		
		JTextArea txtrHello = new JTextArea();
		txtrHello.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtrHello.setText("Numpad +/-\t- Increase/Decrease Speed\r\nArrow UP\t- Pitch Down\r\nArrow DOWN\t- Pitch Up\r\nArrow LEFT\t- Bank Left\r\nArrow DOWN\t- Bank Right\r\nKey A\t\t- Rudder Left\r\nKey D\t\t- Rudder Right\r\nESC \t\t- Exit/Quit Game\r\n\r\nViews:\r\n1\t- Plane\r\n2\t- Cockpit 1\r\n3\t- Cockpit 2\r\n4\t- Tracker");
		scrollPane.setViewportView(txtrHello);
		
		chckbxShowRain = new JCheckBox("Enable Rain");
		chckbxShowRain.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chckbxShowRain.setBounds(284, 75, 97, 23);
		contentPane.add(chckbxShowRain);
		
		rdbtnDay = new JRadioButton("Day");
		buttonGroup_1.add(rdbtnDay);
		rdbtnDay.setSelected(true);
		rdbtnDay.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnDay.setBounds(90, 75, 61, 23);
		contentPane.add(rdbtnDay);
		
		rdbtnNight = new JRadioButton("Night");
		buttonGroup_1.add(rdbtnNight);
		rdbtnNight.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rdbtnNight.setBounds(153, 75, 75, 23);
		contentPane.add(rdbtnNight);
		
		lblScene = new JLabel("Scene:");
		lblScene.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblScene.setBounds(24, 69, 65, 35);
		contentPane.add(lblScene);
		/** ========================================= **/		
	}
}
