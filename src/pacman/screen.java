package pacman;
/**
 * @author Linh Tang & Yolanda Jiang
 * @version May 2020
 */
import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings({ "serial"})
public class screen extends JPanel implements ActionListener {

	/**
	 *  Pacman Variables
	 */
	private int pacman_x, pacman_y;
	// delta changes in x and y directions of pacman
	private int pacmand_x, pacmand_y;
	private final int PACMAN_SPEED = 6;
	// States of pacman
	private boolean dying = false;
	private boolean gainLife = false;
	private boolean cured = false;
	private boolean dead = false;
	private int req_dx, req_dy, view_dx, view_dy;

	/**
	 * Virus Variables
	 */
	private final int MAX_VIRUS = 10;
	private int N_VIRUS = 5;
	private int[] dx, dy;
	private int[] virus_x, virus_y, virus_dx, virus_dy, virusSpeed;
	private int currentSpeed = 3;
	private final int maxSpeed = 6;
	private final int validSpeeds[] = { 3, 4, 5, 6, 8, 10 };
	
	/**
	 * Item Variables
	 */
	int mask_x, mask_y;
	int sick_x, sick_y;
	int house_x, house_y;
	int hospital_x, hospital_y;
	
	/**
	 * Game Graphics and Music
	 */
	Image virus, mask, house, hospital, sick;
	Image pacmanfront, pacmanup, pacmandown, pacmanleft, pacmanright;
	Clip gameMusic, deadSound, getLifeSound, winSound;
    AudioInputStream gameInputStream, deadInputStream, lifeInputStream, winInputStream;
	
	/** 
	 * Screen Settings
	 */
	private Color wallColor = new Color(231, 231, 148);
	private Color fillColor = new Color(0, 41, 140);
	private Color bgColor = Color.BLACK;
	private final int N_BLOCKS = 15;
	private final int BLOCK_SIZE = 24;
	private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
	
	/**
	 * Game Settings
	 */
	private Timer timer;
	private Image ii;
	private int level = 0;
	private int pacsLeft, score;
	private boolean inGame = false;
	private boolean infoHelp = false;
	private boolean musicon = false;
 
	private short[] actualMaze, copyMaze;
		// 1 = left, 2 = top, 4 = right, 8 = bottom, 16 = block, 32 = point
	private final short screenMaze[] = {
			43, 42, 34, 42, 34, 38, 18, 35, 34, 38, 18, 35, 34, 34, 38,
			16, 16, 37, 16, 33, 36, 16, 33, 32, 36, 16, 33, 32, 40, 36,
			39, 16, 45, 16, 33, 32, 34, 32, 40, 32, 34, 32, 36, 16, 37,
			33, 38, 16, 16, 33, 32, 40, 44, 16, 41, 32, 32, 36, 16, 37,
			33, 32, 34, 34, 40, 36, 16, 16, 16, 16, 33, 32, 44, 16, 37,
			33, 32, 32, 36, 16, 33, 34, 34, 34, 34, 32, 36, 16, 16, 37,
			41, 40, 32, 36, 16, 41, 32, 32, 40, 32, 32, 32, 34, 34, 36,
			16, 16, 33, 36, 16, 16, 33, 36, 16, 41, 40, 32, 32, 40, 36,
			35, 34, 32, 36, 16, 35, 32, 36, 16, 16, 16, 33, 36, 16, 37,
			33, 40, 32, 36, 16, 33, 32, 32, 34, 34, 34, 32, 36, 16, 37,
			37, 16, 33, 32, 34, 40, 32, 32, 32, 40, 40, 32, 36, 16, 37,
			37, 16, 41, 32, 36, 16, 41, 32, 36, 16, 16, 33, 36, 16, 37,
			37, 16, 16, 33, 36, 16, 16, 33, 32, 38, 16, 33, 44, 16, 37,
			33, 34, 34, 32, 36, 16, 16, 33, 32, 36, 16, 37, 16, 16, 37,
			41, 40, 40, 40, 40, 42, 42, 40, 40, 44, 24, 41, 42, 42, 44
	};

/* ---------------------- SETTING UP THE GAME ---------------------- */	
	/**
	 * screen constructor
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	public screen() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		loadImage();
//		loadAudio();
		setVariable();
		setScreen();
		initGame();
	}

	/**
	 * get image from source
	 * @param path
	 * @return
	 */
	public Image getImage(String path) {
		return new ImageIcon(getClass().getResource("/resources/images/" + path)).getImage();
	}
	
	/**
	 * load image
	 */
	public void loadImage() {
		virus = getImage("virus.png");
		house = getImage("house.png");
		pacmanfront = getImage("pacman.png");
		pacmanup = getImage("up.png");
		pacmandown = getImage("down.png");
		pacmanleft = getImage("left.png");
		pacmanright = getImage("right.png");
		mask = getImage("mask.png");
		hospital = getImage("hospital.png");
		sick = getImage("sick.png");
	}
	
	/**
	 * set up variables
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void setVariable() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		actualMaze = new short[N_BLOCKS * N_BLOCKS];
		copyMaze = new short[N_BLOCKS * N_BLOCKS];
		timer = new Timer(40, this);
		timer.start();
	 	virus_x = new int[MAX_VIRUS];
		virus_dx = new int[MAX_VIRUS];
		virus_y = new int[MAX_VIRUS];
		virus_dy = new int[MAX_VIRUS];
		virusSpeed = new int[MAX_VIRUS];
		dx = new int[4];
		dy = new int[4];
	}
	
	/**
	 * set up screen with key listener
	 */
	public void setScreen() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(bgColor);
		setDoubleBuffered(true);
	}

	/**
	 * initialize game with screen data pacman state
	 */
	private void initGame() {
		pacsLeft = 3;
		initLevel();
		currentSpeed = 3;
	}
	
	/**
	 * Copy screenMaze to underlying mazes
	 */
	private void initLevel() {
		int i;
		for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
			actualMaze[i] = screenMaze[i];
			copyMaze[i] = screenMaze[i];
		}
		continueLevel();
	}

	/**
	 * continue current game and assign random speed to virus
	 */
	private void continueLevel() {

		short i;
		int dx = 1;
		int random;

		for (i = 0; i < N_VIRUS; i++) {
			if (i < N_VIRUS / 4) {
				virus_y[i] = 4 * BLOCK_SIZE;
				virus_x[i] = 4 * BLOCK_SIZE;
			} else if (i < N_VIRUS / 2) {
				virus_y[i] = 10 * BLOCK_SIZE;
				virus_x[i] = 3 * BLOCK_SIZE;
			} else if (i < N_VIRUS * 3 / 4) {
				virus_y[i] = 10 * BLOCK_SIZE;
				virus_x[i] = 10 * BLOCK_SIZE;
			} else {
				virus_y[i] = 4 * BLOCK_SIZE;
				virus_x[i] = 10 * BLOCK_SIZE;
			}

			virus_dy[i] = 0;
			virus_dx[i] = dx;
			dx = -dx;
			random = (int) (Math.random() % 6);

			if (validSpeeds[random] > currentSpeed) {
				validSpeeds[random] = currentSpeed;
			}

			virusSpeed[i] = validSpeeds[random];
		}

		pacman_x = 7 * BLOCK_SIZE;
		pacman_y = 11 * BLOCK_SIZE;
		pacmand_x = 0;
		pacmand_y = 0;
		req_dx = 0;
		req_dy = 0;
		view_dx = 0;
		view_dy = 0;
		dying = false;
		infoHelp = false;
	}
	
/* -------------------------- MUSIC -------------------------- */
	/**
	 * get image from source
	 * @param path
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public AudioInputStream getAudioStream(String audio) throws UnsupportedAudioFileException, IOException {
		return AudioSystem.getAudioInputStream(getClass().getResource("/resources/music/" + audio));
	}
	
	/**
	 * load game music from source
	 */
	public void loadGameMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		gameInputStream = getAudioStream("pacman_beginning.wav");
		gameMusic = AudioSystem.getClip();
		gameMusic.open(gameInputStream);	
	}
	
	/**
	 * play game music
	 */
	public void playGameMusic() {
		if(!gameMusic.isRunning()) {
			gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	/**
	 * stop game music
	 */
	public void stopGameMusic() throws IOException {
		if (gameMusic.isRunning()) {
			gameMusic.stop();
		    gameMusic.close();
		    gameInputStream.close();
		}
	}

	/**
	 * load dead music
	 */
	public void loadDeadSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		deadInputStream = getAudioStream("pacman_death.wav");
		deadSound = AudioSystem.getClip();
		deadSound.open(deadInputStream);
	}
	
	/**
	 * play dead music
	 */
	public void playDeadSound() {
		if(!deadSound.isRunning()) {
			deadSound.start();
		}
	}
	
	/**
	 * stop dead music
	 */
	public void stopDeadSound() throws IOException {
		if (deadSound.isRunning()) {
			deadSound.stop();
		    deadSound.close();
		    deadInputStream.close();
		}
	}
	
	/**
	 * load gain life music
	 */
	public void loadGetLifeSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		lifeInputStream	= getAudioStream("gainlife.wav");
		getLifeSound = AudioSystem.getClip();
		getLifeSound.open(lifeInputStream);
		
	}
	
	/**
	 * play gain life music
	 */
	public void playGetLifeSound() {
		if(!getLifeSound.isRunning()) {
			getLifeSound.start();
		}
	}
	
	/**
	 * stop gain life music
	 */
	public void stopGetLifeSound() throws IOException {
		if (getLifeSound.isRunning()) {
			getLifeSound.stop();
		    getLifeSound.close();
		    lifeInputStream.close();
		}
	}
	
	/**
	 * load win life music
	 */
	public void loadWinMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		winInputStream = getAudioStream("pacman_win.wav");
		winSound = AudioSystem.getClip();
		winSound.open(winInputStream);
	}
	
	/**
	 * play win life music
	 */
	public void playWinMusic() {
		if(!winSound.isRunning()) {
			winSound.start();
		}
	}
	
	/**
	 * stop win life music
	 */
	public void stopWinMusic() throws IOException {
		if (winSound.isRunning()) {
			winSound.stop();
		    winSound.close();
		    winInputStream.close();
		}
	}
	
/* ---------------------- DISPLAYING MESSAGE ---------------------- */

	/**
	 * general setup for displaying message
	 * @param g2d
	 * @param s1
	 * @param s2
	 */
	private void showMessage(Graphics2D g2d, String s1, String s2, String s3) {
		Font small = new Font("Helvetica", Font.BOLD, 14);
		Font title = new Font("Helvetica", Font.BOLD, 18);
		
		int start_x = 60;
		int start_y = 100;
		int length = SCREEN_SIZE - 2*start_x;
		g2d.setColor(new Color(0, 32, 48));
		g2d.fillRect(start_x, start_y, length, SCREEN_SIZE - 270);
		g2d.setColor(Color.white);
		g2d.drawRect(start_x, start_y, length, SCREEN_SIZE - 270);
		
		g2d.setColor(Color.white);
		g2d.setFont(title);
		start_x += 10;
		int height = 20;
		g2d.drawString(s1, start_x, start_y + height);
		
		g2d.setFont(small);
		g2d.drawString(s2, start_x, start_y + 3*height);
		g2d.drawString(s3, start_x, start_y + 4*height);
	}
	
	/**
	 * show intro screen
	 * @param g2d
	 */
	private void showIntroScreen(Graphics2D g2d) {
		int start_x = 40;
		int start_y = 30;
		int length = SCREEN_SIZE - 2*start_x;
		g2d.setColor(new Color(0, 32, 48));
		g2d.fillRect(start_x, start_y, length, SCREEN_SIZE - 100);
		g2d.setColor(Color.white);
		g2d.drawRect(start_x, start_y, length, SCREEN_SIZE - 100);
		
		String s0 = "It's Corona Era!";
		String s1 = "Help Pacman go home.";
		String s2 = "Get mask to protect yourself.";
		String s3 = "Go to hospital if you are dying.";
		String s4 = "Remember:";
		String s5 = "having contact with infected person";
		String s6 = "will also make you sick.";
		String s7 = "\t\t\t\t\t\t\t\tPress \"i\" to read instructions";
		String s8 = "\t\t\t\t\t\t\t\tPress \"ENTER\" to start the game\n";
		
		Font small = new Font("Helvetica", Font.BOLD, 14);
		Font title = new Font("Helvetica", Font.BOLD, 20);

		g2d.setColor(Color.white);
		g2d.setFont(title);
		start_x += 10;
		int height = 20;
		g2d.drawString(s0, start_x, start_y + height);
		g2d.setFont(small);
		g2d.drawString(s1, start_x, start_y + 2*height);
		g2d.drawString(s2, start_x, start_y + 3*height);
		g2d.drawString(s3, start_x, start_y + 4*height);
		g2d.drawString(s4, start_x, start_y + 6*height);
		g2d.drawString(s5, start_x, start_y + 7*height);
		g2d.drawString(s6, start_x, start_y + 8*height);
		g2d.drawString(s7, start_x, start_y + 11*height);
		g2d.drawString(s8, start_x, start_y + 12*height);
		
		
	}
	
	/**
	 * show upgrade screen when upgrade to level 2
	 * @param g2d
	 */
	private void showUpgradeScreen(Graphics2D g2d) {
		String s0 = "\t\t\t\t\t\t\t\t\t\t\tSCORE: " + score;
		String s1 = "\t\t\t\t\t\t\t\t\t\t\t\tYou are safe now!\n";
		String s2 = "\t\t\tPress \"ENTER\" to play level 2.";
		showMessage(g2d, s0, s1, s2);
	}
	
	/**
	 * show ending screen when complete 2 levels
	 * @param g2d
	 */
	private void showEndingScreen(Graphics2D g2d) {
		
		
		String s0 = "\t\t\t\t\t\t\t\t\t\t\tSCORE: " + score;
		String s1 = "Hoorayy! Pacman is now home!\n";
		String s2 = "\t\t\tPress \"ENTER\" to play again.";
		
		showMessage(g2d, s0, s1, s2);

		
		continueLevel();
	}
	
	/**
	 * show dead screen when pacman die
	 * @param g2d
	 */
	private void showDeadScreen(Graphics2D g2d) {
		String s0 = "\t\t\t\t\t\t\t\t\t\t\tSCORE: " + score;
		String s1 = "\t\tGame over! Pacman now R.I.P\n";
		String s2 = "\t\t\t\t\t\t\t\tPress \"ENTER\" to restart.";
		
		showMessage(g2d, s0, s1, s2);
	}

	/**
	 * show instructions
	 * @param g2d
	 */
	private void showInstruction(Graphics2D g2d) {
		int start_x = 20;
		int start_y = 50;
		int length = SCREEN_SIZE - 2*start_x;
		g2d.setColor(new Color(0, 32, 48));
		g2d.fillRect(start_x, start_y, length, SCREEN_SIZE - 80);
		g2d.setColor(Color.white);
		g2d.drawRect(start_x, start_y, length, SCREEN_SIZE - 80);

		String s0 = "It's Corona Era! Pacman just wanna go home";
		String s1 = "Your goal is to get Pacman home safe and";
		String s2 = "\t\tsound, using 4 arrow keys";
		String s3 = "If you run into red virus, you lose 1 life";
		String s4 = "If you meet infected person, you lose 1 life";
		String s5 = "If you get mask, you get 1 life";
		String s6 = "If you enter hospital, you get 1 life";
		String s7 = "If you reach home, you are now safe! Yay!";
		String s8 = "In game, press \"ESP\" to quit";
		String s9 = "In game, press \"SPACE\" to pause/continue";
		String s10 = "Now, press \"enter\" to start.";
		Font small = new Font("Helvetica", Font.BOLD, 14);

		g2d.setColor(Color.white);
		g2d.setFont(small);
		start_x += 10;
		int height = 20;
		g2d.drawString(s0, start_x, start_y + height);
		g2d.drawString(s1, start_x, start_y + 2*height);
		g2d.drawString(s2, start_x, start_y + 3*height);
		g2d.drawString(s3, start_x, start_y + 5*height);
		g2d.drawString(s4, start_x, start_y + 6*height);
		g2d.drawString(s5, start_x, start_y + 7*height);
		g2d.drawString(s6, start_x, start_y + 8*height);
		g2d.drawString(s7, start_x, start_y + 9*height);
		g2d.drawString(s8, start_x, start_y + 11*height);
		g2d.drawString(s9, start_x, start_y + 12*height);
		g2d.drawString(s10, start_x, start_y + 13*height);
	}
	
/* ---------------------- DRAWING ITEMS & MAZE ---------------------- */	
	
	/**
	 * draw box around to avoid virus from enter
	 * @param g2d
	 * @param x
	 * @param y
	 */
	private void drawBox(Graphics2D g2d, int x, int y) {
			int pos = x / BLOCK_SIZE + N_BLOCKS * (y / BLOCK_SIZE);
			int right = pos + 1;
			int left = pos - 1;
			int up = pos - N_BLOCKS;
			int down = pos + N_BLOCKS;
			actualMaze[pos] = 0;
			// if there is no wall on the right
			if ((actualMaze[right] & 1) == 0)
				actualMaze[right] += 1;
			// if there is no wall on the left
			if ((actualMaze[left] & 4) == 0)
				actualMaze[left] += 4;
			// if there is no wall up
			if ((actualMaze[up] & 8) == 0)
				actualMaze[up] += 8;
			// if there is no wall down
			if ((actualMaze[down] & 2) == 0)
				actualMaze[down] += 2;
	}
	
	/**
	 * draw mask
	 * @param g2d
	 * @param x
	 * @param y
	 */
	private void drawMask(Graphics2D g2d, int x, int y) {
		mask_x = x - x % BLOCK_SIZE;
		mask_y = y - y % BLOCK_SIZE;
		g2d.drawImage(mask, mask_x, mask_y, this);
		drawBox(g2d, mask_x, mask_y);
		drawBox(g2d, mask_x+BLOCK_SIZE, mask_y);
	}
	
	/**
	 * draw infected pacman
	 * @param g2d
	 * @param x
	 * @param y
	 */
	private void drawSick(Graphics2D g2d, int x, int y) {
		sick_x = x - x % BLOCK_SIZE;
		sick_y = y - y % BLOCK_SIZE;
		g2d.drawImage(sick, sick_x, sick_y, this);
		drawBox(g2d, sick_x, sick_y);
	}

	/**
	 * draw hospital
	 * @param g2d
	 * @param x
	 * @param y
	 */
	private void drawHospital(Graphics2D g2d, int x, int y) {
		hospital_x = x - x % BLOCK_SIZE;
		hospital_y = y - y % BLOCK_SIZE;
		g2d.drawImage(hospital, hospital_x, hospital_y, this);
		drawBox(g2d, hospital_x, hospital_y);
	}
	
	/**
	 * draw house
	 * @param g2d
	 * @param x
	 * @param y
	 */
	private void drawHouse(Graphics2D g2d, int x, int y) {
		house_x = x;
		house_y = y;
		g2d.drawImage(house, house_x - 35, house_y - 30, this);
		drawBox(g2d, house_x - 35, house_y - 30);
		drawBox(g2d, 50, 25);
	}
	
	/**
	 * draw maze
	 * @param g2d
	 */
	private void drawMaze(Graphics2D g2d) {

		short i = 0;
		int x, y;
		for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
			for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
				
				if ((copyMaze[i] & 16) != 0) {
                    g2d.setColor(fillColor);
					g2d.fillRect(x, y, BLOCK_SIZE-1, BLOCK_SIZE-1);
				}
				
				BasicStroke stroke = new BasicStroke(2,
				                        BasicStroke.CAP_ROUND,
				                        BasicStroke.JOIN_ROUND);
				g2d.setColor(wallColor);
				g2d.setStroke(stroke);

				if ((copyMaze[i] & 1) != 0) {
					g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
				}

				if ((copyMaze[i] & 2) != 0) {
					g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
				}

				if ((copyMaze[i] & 4) != 0) {
					g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
				}

				if ((copyMaze[i] & 8) != 0) {
					g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
				}
				
				if ((copyMaze[i] & 32) != 0) {
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }
				
				i++;
			}
		}
	}

	/**
	 * draw life, level and score
	 * @param g2d
	 */
	private void drawLife(Graphics2D g2d) {

		int i;

		for (i = 0; i < pacsLeft; i++) {
			g2d.drawImage(pacmanleft, i * 28 + 8, SCREEN_SIZE + 1, this);
		}
		int lev = level;
		if(level != 2) {
			 lev = level + 1;
		} 
		String s1 = "Level " + lev;
		String s2 = "Score: " + score;
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		g2d.setColor(Color.white);
		g2d.setFont(small);	
		g2d.drawString(s1, SCREEN_SIZE/2, SCREEN_SIZE + 20);
        g2d.drawString(s2, SCREEN_SIZE - metr.stringWidth(s2) - 10, SCREEN_SIZE + 20);
	}

	/**
	 * draw pacman with four directions
	 * @param g2d
	 */
	private void drawPacman(Graphics2D g2d) {
		if (view_dx == 0 && view_dy == 0) {
			g2d.drawImage(pacmanfront, pacman_x + 1, pacman_y + 1, this);
		} else if (view_dx == -1) {
			g2d.drawImage(pacmanleft, pacman_x + 1, pacman_y + 1, this);
		} else if (view_dx == 1) {
			g2d.drawImage(pacmanright, pacman_x + 1, pacman_y + 1, this);
		} else if (view_dy == -1) {
			g2d.drawImage(pacmanup, pacman_x + 1, pacman_y + 1, this);
		} else {
			g2d.drawImage(pacmandown, pacman_x + 1, pacman_y + 1, this);
		}
	}

	/**
	 * draw virus
	 * @param g2d
	 * @param x
	 * @param y
	 */
	private void drawVirus(Graphics2D g2d, int x, int y) {
		g2d.drawImage(virus, x, y, this);
	}
	
	
/* ---------------------- PACMAN & VIRUS CONTROLLER ---------------------- */
	
	/**
	 * move pacman with keys and modify lives 
	 */
	private void movePacman() {

		int pos;
		short ch;

		if (req_dx == -pacmand_x && req_dy == -pacmand_y) {
			pacmand_x = req_dx;
			pacmand_y = req_dy;
			view_dx = pacmand_x;
			view_dy = pacmand_y;
		}

		if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
			pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
			ch = copyMaze[pos];
			
			if ((ch & 32) != 0) {
                copyMaze[pos] = (short) (ch & 31);
                score++;
            }
			
			if (req_dx != 0 || req_dy != 0) {
				if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0) || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
						|| (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
						|| (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
					pacmand_x = req_dx;
					pacmand_y = req_dy;
					view_dx = pacmand_x;
					view_dy = pacmand_y;
				}
			}

			// Check for standstill
			if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
					|| (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
					|| (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
					|| (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
				pacmand_x = 0;
				pacmand_y = 0;
			}
		}
		pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
		pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;

		// get mask
		if (!gainLife) {
			if (pacman_x > mask_x - BLOCK_SIZE && pacman_x < mask_x + BLOCK_SIZE + 20 && pacman_y > mask_y - BLOCK_SIZE
					&& pacman_y < mask_y + BLOCK_SIZE && inGame) {
				pacsLeft++;
				gainLife = true;
				try {
					loadGetLifeSound();
				} catch (Exception e) {
					e.printStackTrace();
				} 
				playGetLifeSound();
				try {
					stopGetLifeSound();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// go to hospital
		if (!cured) {
			if (pacman_x > hospital_x - BLOCK_SIZE && pacman_x < hospital_x + BLOCK_SIZE
					&& pacman_y > hospital_y - BLOCK_SIZE && pacman_y < hospital_y + BLOCK_SIZE && inGame) {
				pacsLeft++;
				cured = true;
				try {
					loadGetLifeSound();
				} catch (Exception e) {
					e.printStackTrace();
				} 
				playGetLifeSound();
				try {
					stopGetLifeSound();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// get infected
		if (pacman_x > sick_x - BLOCK_SIZE && pacman_x < sick_x + BLOCK_SIZE && pacman_y > sick_y - BLOCK_SIZE
				&& pacman_y < sick_y + BLOCK_SIZE && inGame) {
			dying = true;
		}

		// get into house
		if (pacman_x > house_x - BLOCK_SIZE && pacman_x < house_x + BLOCK_SIZE && pacman_y > house_y - BLOCK_SIZE
				&& pacman_y < house_y + BLOCK_SIZE && inGame) {
			inGame = false;
			if(level != 2) {
				level++;
			}
			try {
				loadWinMusic();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			playWinMusic();
			try {
				stopWinMusic();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * if pacman die
	 */
	private void death() {
		try {
			loadDeadSound();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		playDeadSound();
		try {
			stopDeadSound();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pacsLeft--;
		
		if (pacsLeft == 0) {
			inGame = false;
			dead = true;
			
		}

		continueLevel();
	}

	/**
	 * move virus 
	 * @param g2d
	 */
	private void moveVIRUS(Graphics2D g2d) {

		short i;
		int pos;
		int count;

		for (i = 0; i < N_VIRUS; i++) {
			if (virus_x[i] % BLOCK_SIZE == 0 && virus_y[i] % BLOCK_SIZE == 0) {
				pos = virus_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (virus_y[i] / BLOCK_SIZE);

				count = 0;

				if ((actualMaze[pos] & 1) == 0 && virus_dx[i] != 1) {
					dx[count] = -1;
					dy[count] = 0;
					count++;
				}

				if ((actualMaze[pos] & 2) == 0 && virus_dy[i] != 1) {
					dx[count] = 0;
					dy[count] = -1;
					count++;
				}

				if ((actualMaze[pos] & 4) == 0 && virus_dx[i] != -1) {
					dx[count] = 1;
					dy[count] = 0;
					count++;
				}

				if ((actualMaze[pos] & 8) == 0 && virus_dy[i] != -1) {
					dx[count] = 0;
					dy[count] = 1;
					count++;
				}

				if (count == 0) {

					if ((actualMaze[pos] & 15) == 15) {
						virus_dx[i] = 0;
						virus_dy[i] = 0;
					} else {
						virus_dx[i] = -virus_dx[i];
						virus_dy[i] = -virus_dy[i];
					}

				} else {

					count = (int) (Math.random() * count);

					if (count > 3) {
						count = 3;
					}

					virus_dx[i] = dx[count];
					virus_dy[i] = dy[count];
				}

			}

			virus_x[i] = virus_x[i] + (virus_dx[i] * virusSpeed[i]);
			virus_y[i] = virus_y[i] + (virus_dy[i] * virusSpeed[i]);
			drawVirus(g2d, virus_x[i] + 1, virus_y[i] + 1);

			// check for death
			if (pacman_x > (virus_x[i] - 12) && pacman_x < (virus_x[i] + 12) && pacman_y > (virus_y[i] - 12)
					&& pacman_y < (virus_y[i] + 12) && inGame) {

				dying = true;
			}
		}
	}

/* ---------------------- GAME CONTROLLER ---------------------- */

	/**
	 * main controller of game
	 * @param g2d
	 */
	private void playGame(Graphics2D g2d) {

		if (dying) {

			death();

		} else {

			movePacman();
			drawPacman(g2d);
			moveVIRUS(g2d);
		}
	}

	/**
	 * upgrade to level 2 with more virus and higher speed
	 */
	private void upgrade() {
		if (level == 1) {

			if (N_VIRUS < MAX_VIRUS) {
				N_VIRUS++;
			}

			if (currentSpeed < maxSpeed) {
				currentSpeed++;
			}

			
			initLevel();
		}
	}

/* ---------------------- ACTION CONTROLLER ---------------------- */	
	
	/**
	 * continuously painting 
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	@Override
	public void addNotify() {
		super.addNotify();

		initGame();
	}

	/**
	 * draw images on screen, show changes of life, score and level
	 * @param g
	 */
	private void doDrawing(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		drawMaze(g2d);

		if (gainLife) {
			drawLife(g2d);
		} else {
			drawMask(g2d, 240, 144);
		}

		if (cured) {
			drawLife(g2d);
		} else {
			drawHospital(g2d, 24, 312);
		}

		drawLife(g2d);
		drawHouse(g2d, 60, 60);
		drawSick(g2d, 120, 24);

		if (inGame) {
			playGame(g2d);
		} else if (dead) {
			showDeadScreen(g2d);
		} else if (level == 2) {
			showEndingScreen(g2d);
		} else if (infoHelp) {
			showInstruction(g2d);
		} else if (level == 1) {
			showUpgradeScreen(g2d);
			upgrade();
		} else {
			showIntroScreen(g2d);
		}

		g2d.drawImage(ii, 5, 5, this);
		Toolkit.getDefaultToolkit().sync();
		g2d.dispose();

	}

	/**
	 * modify game with keys
	 */
	class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			
			if (inGame) {
				
				if (key == KeyEvent.VK_LEFT) {
					req_dx = -1;
					req_dy = 0;
				} else if (key == KeyEvent.VK_RIGHT) {
					req_dx = 1;
					req_dy = 0;
				} else if (key == KeyEvent.VK_UP) {
					req_dx = 0;
					req_dy = -1;
				} else if (key == KeyEvent.VK_DOWN) {
					req_dx = 0;
					req_dy = 1;
				} else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
					try {
						stopGameMusic();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					musicon = false;
					inGame = false;
					
				} else if (key == KeyEvent.VK_SPACE) {
					if (timer.isRunning()) {
						timer.stop();
						try {
							stopGameMusic();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						timer.start();
						try {
							loadGameMusic();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						playGameMusic();
					}
				}
			} else {
				if ((!dead && level < 2) && (key == KeyEvent.VK_ENTER)) {
					
					if(!musicon) {	
						try {
							loadGameMusic();
						} catch (Exception e1) {
							e1.printStackTrace();
						} 
					}
					playGameMusic();
					musicon = true;
					inGame = true;
					gainLife = false;
					cured = false;
					infoHelp = false;
					dead = false;
					score = 0;
					initGame();
				}
				if ((dead || level == 2) && (key == KeyEvent.VK_ENTER)) {
					inGame = true;
					level = 0;
					N_VIRUS = 5;
					gainLife = false;
					cured = false;
					dead = false;
					score = 0;
					level = 0;
					initGame();
				}
				if (key == 'i' || key == 'I') {
					infoHelp = true;
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}
