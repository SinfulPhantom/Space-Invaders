// Created by Spencer Miller
// linkedIn: https://www.linkedin.com/in/spencer-miller-079a0111b

package space_invaders;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;

public class SpaceInvaders extends JFrame {
		
	// Sets the font for the display
	Font font = new Font("Serif", Font.BOLD, 18);
	
	// Global variables
	public spacePanel sp;
	public int x_pic = 0, y_pic = 400, x_offSet, y_offSet;
	public int[] randomX = new int[10];
	public int[] randomY = new int[10];
	public boolean cannonTouched = false; 
	public int initialize = 0, started = 0;
	public int delete[] = new int[10];
	public int ball, ballX, ballY, ballI = 0, time = 0, fired = 0, shotsFired = 0;
	boolean win = false, lose = false, reset = false;
	public boolean timerbool = false;
	public java.util.Timer timer;
	public boolean rerun = false;
	public Timer clock;
	public int seconds = 0;
	public int minutes = 0;
	public int hours = 0;
	private DecimalFormat decimalformat = new DecimalFormat("00");
	int deleted = 0;
	
	// Global image declarations
	public Image cannon = Toolkit.getDefaultToolkit().getImage("space_invaders/cannon1.png");
	public Image background = Toolkit.getDefaultToolkit().getImage("space_invaders/spinvBG.png");
	public Image target = Toolkit.getDefaultToolkit().getImage("space_invaders/MarkSmith.png");
	public Image cannonball = Toolkit.getDefaultToolkit().getImage("space_invaders/cannonBall.png");
	Image[] targets = new Image[10];
	Image cannonballs;
	
	// Global Score and Label Declarations
	public int Score = 0;
	JLabel scoreText = new JLabel("Score: " + Score);
	JLabel timeText = new JLabel("0");
  JLabel displayText = new JLabel("Press down to begin!");
	JLabel loseLabel= new JLabel("YOU LOST!! Press down key to try again");
	JLabel winLabel = new JLabel("YOU WIN!!  Press down key to try again");

	// Class constructor
	public SpaceInvaders(boolean reset) {
	  
		//Sets display for the Frame/Panel
		super("SpaceInvaders");
		sp = new spacePanel(reset);
		sp.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.add(sp,BorderLayout.WEST);
		this.setSize(640, 480);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		boolean reset = false;
		new SpaceInvaders(reset);
	}
	
	// Class for the spacePanel so that paintComponent can be used later on
	public class spacePanel extends JPanel implements ActionListener {
		
	  public spacePanel(boolean reset) {
		  
			// Sets the panel for PaintComponent display
			super();
			setPreferredSize(new Dimension(640,480));
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			this.addMouseMotionListener(new MouseHandler());
			this.addMouseListener(new MouseClickedHandler());
			this.addKeyListener(new KeyHandler());
			this.setFocusable(true);
			
			// Adds the text to the display
			add(displayText);
      displayText.setLocation(270, 240);
      displayText.setForeground(Color.WHITE);
			scoreText.setFont(scoreText.getFont().deriveFont(14.0f));
			scoreText.setLocation(540+40, 320);
			scoreText.setForeground(Color.GREEN);
			winLabel.setForeground(Color.WHITE);
      loseLabel.setForeground(Color.RED);
      
			// Prepares the clock
			clock = new Timer(10, this);
			if (reset == true) {
				clock.start();
				timer = new java.util.Timer();
				TimerFired tf = new TimerFired();
				timer.scheduleAtFixedRate( tf, 0, 10 );
			}
		}	
		
		// ActionPerformed
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == clock) {
				seconds++;
			} 
			if (seconds == 60) {
				minutes++;
				seconds = 0;
			}
			if (minutes == 60) {
				hours++;
				minutes = 0;
				seconds = 0;
			}
			if (hours == 24) {
				hours = 0;
				minutes = 0;
				seconds = 0;
			}
			
			remove(displayText);
      add(scoreText);
      
			timeText.setText(decimalformat.format(hours) + ":" + 
							decimalformat.format(minutes) + ":" + 
							decimalformat.format(seconds));
		}
		
		// Location of paintComponent method
		public void paintComponent(Graphics g) {
		  
			super.paintComponent(g);
			
			// Draws the items to the panel, which in turn draws to the screen
			g.drawImage(background,0,0,640,480,this);
			g.setFont(font);
			g.drawString("Time: " + timeText.getText(), 100, 40);
			g.drawString("Shots:  " + shotsFired, 100, 55);

			// Control for assigning random numbers to the target point
			if (rerun == false) {  
				for (int i = 0; i < 10; i++) {
					Random randomGenerator = new Random();
					randomX[i] = displayRandom(40,590,randomGenerator);
					randomY[i] = displayRandom(50,150,randomGenerator);
				}
			}
			
			rerun = true;

			if (fired == 1) {
				// Controls the cannonball display
				cannonballs = cannonball;
				g.drawImage(cannonballs, ballX, ballY = ballY--, 10, 10, this);
				ballY = ballY - 2;
				if (ballY < 0) { 
					fired = 0;
				}
			}
			
			for (int i =0; i < 10; ++i) { 
				if ((ballX >= randomX[i] - 10) && (ballX <= randomX[i] + 30) && (ballY <= randomY[i] + 30)
				    && (ballY >= randomY[i] - 10) && (delete[i] == 0)) {
					fired = 0;
					delete[i] = 1;
					deleted++;
					ScoreKeeper();
				}
				if (randomY[i] >= 400 && win == false) {
					g.drawString(loseLabel.getText(), 180, 20);
					GlorifiedLoser();
				}
				if (delete[i] == 0) {
					targets[i] = target;
					g.drawImage(targets[i],randomX[i],randomY[i],30,30,this);
				}
			}
			g.drawImage(cannon, x_pic, y_pic, this);
		}
		
		private void ScoreKeeper() {
		  
			Score++;
			scoreText.setText("Score: " + Score);
			if (Score == 10) {
				clock.stop();
				SmugWinner();
			} else if (Score > 10) {
			  GlorifiedLoser();
			}
		}
		
		public void SmugWinner() {
		  
			add(winLabel);
			remove(scoreText);
			win = true;
		}
		
		public void GlorifiedLoser() {
		  
		  add(loseLabel);
		  clock.stop();
			remove(scoreText);
			lose = true;	
		}
	}

	// MouseClickedHandler
	class MouseClickedHandler extends MouseAdapter {
	  
		public void mousePressed(MouseEvent me) {
		  
			if ((me.getX() > x_pic) && (me.getX() < x_pic + 100) && (me.getY() > y_pic) 
			    && (me.getY() < y_pic + 100)) {
				cannonTouched = true;
				x_offSet = me.getX() - x_pic;
				y_offSet = me.getY() - y_pic;
			} else
				cannonTouched = false;
		}
	}
	
	// MouseHandler
	class MouseHandler extends MouseMotionAdapter {

		public void mouseDragged(MouseEvent e) {
		  
			if ((cannonTouched)) {
				x_pic = e.getX() - x_offSet;
				if (x_pic > 580)
					x_pic = 580;
				else if (x_pic <= 0)
					x_pic = 0;
			}
			sp.repaint();
		}
	}
	
	private static int displayRandom(int first, int last, Random rand) {
	  
		// This is where the random numbers are generated for the targets to be displayed
		if (first > last) 
			throw new IllegalArgumentException("");
		
		long range = (long)last - (long)first + 1;
		long fraction = (long)(range * rand.nextDouble());
		int randomNumber = (int)(fraction + first);
		return randomNumber;
	}

	public class TimerFired extends TimerTask {
		
		int dummy = 0;
		
		public void run() {
		  
			dummy++;
			for (int i = 0; i < 10; ++i) {
				if ((randomY[i] >= 30) && (dummy%10 == 0))
					randomY[i]++;
			}
			repaint();
		}
	}
	
	// Keyhandler
	class KeyHandler implements KeyListener {
		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				clock.start();
				timer = new java.util.Timer();
				TimerFired tf = new TimerFired();
				timer.scheduleAtFixedRate( tf, 0, 10 );
				sp.repaint();
				
				if (win == true || lose == true)
					reset();
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (fired == 0) {
				  shotsFired++;
					ballX = x_pic + 15;
					ballY = 400;
					fired = 1;
				}
				sp.repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {		  
				x_pic = x_pic+5;			
				if (x_pic > 580)
					x_pic = 580;
				sp.repaint();
			}
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {			  
				x_pic = x_pic-5;
				if (x_pic <= 0)
					x_pic = 0;
				sp.repaint();
			}
		}
		
		// Method stubs
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}

		// Reset method
		public void reset() {
			reset = true;
			new SpaceInvaders(reset);
		}
	}
}