/*
 * Copyright 2024 Usbong
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @company: Usbong
 * @author: SYSON, MICHAEL B.
 * @date created: 20240522
 * @last updated: 20240806; from 20240805
 * @website: www.usbong.ph
 *
 */
/*
 Additional Notes:
 1) compile on Windows Machine;
	javac *.java

 2) Execute
	java UsbongMain
*/

/*
current error; 20240608; from 20240606
LWJGL; JVM platform Windows x86
however, Platform available on classpath: windows/x64
Failed to locate library: lwjgl.dll

answer: download Java SE 8 SDK; Windows x64

https://www.oracle.com/ph/java/technologies/javase/javase8-archive-downloads.html; last accessed: 20240608
*/

/*
//Reference:
1) https://docs.oracle.com/javase/tutorial/uiswing/painting/refining.html; last accessed: 20240622
SwingPaintDemo4.java; last accessed: 20240622; from 20240623

2) https://github.com/usbong/game-off-2023; last accessed: 20240623
*/

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

//added by Mike, 20240622
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

//added by Mike, 20240622
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import java.util.TimerTask;
import java.util.Timer;

import java.awt.Toolkit;
import java.awt.Dimension;

//added by Mike, 20240719
import java.awt.event.WindowStateListener;

//added by Mike, 20240622
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

//added by Mike, 20240623
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

//sound
/* //edited by Mike, 20240623
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;
*/
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

//TODO: -verify: in another computer with Java Virtual Machine

public class UsbongMain {

  //edited by Mike, 20240622
  //reference: Usbong Game Off 2023 Main
  //1000/60=16.66; 60 frames per second
  //1000/30=33.33; 30 frames
  //const fFramesPerSecondDefault=16.66;
  //const fFramesPerSecondDefault=33.33;
  private static final int updateDelay = 16;//20;
  private static MyPanel mp;

  //added by Mike, 20240628
  private static int iScreenWidth;
  private static int iScreenHeight;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        //System.out.println("Created GUI on EDT? "+
        //SwingUtilities.isEventDispatchThread());

		JFrame f = new JFrame("Usbong Main Demo");		
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//edited by Mike, 20240622
        //f.setSize(250,250);

		//edited by Mike, 20240618
	    //setSize(windowSize, windowSize);
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    double dWidth = screenSize.getWidth();
        double dHeight = screenSize.getHeight();

	    iScreenWidth = (int) Math.round(dWidth);
	    iScreenHeight = (int) Math.round(dHeight);

		//added by Mike, 20240718; debug
		//iScreenHeight=512;
		
/*	//removed by Mike, 20240722		
	    System.out.println("width: "+iScreenWidth);
	    System.out.println("height: "+iScreenHeight);
*/
      //edited by Mike, 20240622
      //macOS still has menu row, etc.
      //f.setSize(iWidth, iHeight);

	  //edited by Mike, 20240722; from 20240719
	  //reminder: in Java21, shall still need to switch between windows;
	  f.setUndecorated(true); //removes close, minimize, maxime buttons in window
/*
	  //f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	  f.addWindowStateListener(new WindowStateListener() {
	    public void windowStateChanged(WindowEvent e) {
		   // minimized
		   if ((e.getNewState() & f.ICONIFIED) == f.ICONIFIED){
		     //f.setUndecorated(false); //displays close, minimize, maxime buttons in window
			 //System.out.println("MINIMIZED!");
		   }
		   // maximized
		   else if ((e.getNewState() & f.MAXIMIZED_BOTH) == f.MAXIMIZED_BOTH){
			 f.setUndecorated(true); //removes close, minimize, maxime buttons in window
			 //System.out.println("MAXIMIZED!");
		   }
	    }
  	  });
*/

      //Reference: https://stackoverflow.com/questions/1155838/how-can-i-do-full-screen-in-java-on-osx; last accessed: 20240622
      //answered by: Michael Myers, 20090720T2105
      GraphicsDevice gd =
                  GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

      //macOS, Windows both have "Full Screen Support"
      if (gd.isFullScreenSupported()) {
          gd.setFullScreenWindow(f);
      } else { //note: in case, no "Full Screen Support"
          f.setSize(iScreenWidth, iScreenHeight);
          f.setVisible(true);
      }

		//edited by Mike, 20240622
		//f.add(new MyPanel());
		//edited by Mike, 20240628
		//mp = new MyPanel(f);
		mp = new MyPanel(f, iScreenWidth, iScreenHeight);

		mp.setFocusable(true); //necessary to receive key presses
		f.add(mp);

    //removed by Mike, 20240622
    //f.setVisible(true);

/*  //edited by Mike, 20240623
    final JFXPanel fxPanel = new JFXPanel();
    String sCanonFilename = "../assets/audio/usbongGameOff2023AudioEffectsCannon.mp3";
    Media canonSound = new Media(new File(sCanonFilename).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(canonSound);
    mediaPlayer.play();
*/
	//playSound("../assets/audio/usbongGameOff2023AudioEffectsCannon.wav");
	//https://en.wikipedia.org/wiki/File:MIDI_sample.mid?qsrc=3044; last accessed: 20240623
	//midi files playable;
	//however, known warning appears:
	//https://stackoverflow.com/questions/23720446/java-could-not-open-create-prefs-error; last accessed: 20240623
	//...java.util.prefs.WindowsPreferences <init>
	//WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs at root
    //0x80000002. Windows RegCreateKeyEx(...) returned error code 5.
	//edited by Mike, 20240720
	//playSound("../assets/audio/MIDI_sample.mid");
	playSound("./assets/audio/MIDI_sample.mid");

		//timer.scheduleAtFixedRate(() -> mainRun(), 0, updateDelay, MILLISECONDS);

		TimerTask myTimerTask = new TimerTask(){
		   public void run() {
		     mp.update();

			 mp.repaint();
		   }
		};

		Timer myTimer = new Timer(true);
		myTimer.scheduleAtFixedRate(myTimerTask, 0, updateDelay); //60 * 1000);
    }

	//reference: https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java;
	//last accessed: 20240623
	//answered by: Andrew Jenkins, 20160608T0441
	//edited by: Sebastian, 20200608T1929
	//note: .mp3 format not available
	private static void playSound(String soundFile) {
		  try {
			Clip clip = AudioSystem.getClip();
			File f = new File("" + soundFile);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());

			clip.open(audioIn);
			clip.start();
		  } catch (Exception e) {
			System.err.println(e.getMessage());
		  }
	}
}

//note JPanel? JFrame? confusing at the start, without working sample code;
class MyPanel extends JPanel {
	//added by Mike, 20240708
	int iScreenWidth;
	int iScreenHeight;
	int iOffsetScreenWidthLeftMargin;
	int iOffsetScreenHeightTopMargin; //added by Mike, 20240718

  //added by Mike, 20240718
  int iStageWidth;
  int iStageHeight;

  //added by Mike, 20240722
  //note when titlebar appears; stage height decreases;
  //iScreenHeight=iScreenHeight-iTileHeight/2;


	//edited by Mike, 20240714
/*
	final int iTileWidth=64;//128;
	final int iTileHeight=64;//128;
*/
	int iTileWidth=64;//default
	int iTileHeight=64;//default

	final int iTileWidthCountMax=13;
	final int iTileHeightCountMax=13;

    RedSquare redSquare;

	//added by Mike, 20240622
	RobotShip myRobotShip;
	
	//edited by Mike, 20240804; from 20240802
	//EnemyRobotShip myEnemyShip;
	Level2D myLevel2D;

	//added by Mike, 20240711
	BackgroundCanvas myBackgroundCanvas;

	JFrame myJFrameInstance;

	//edited by Mike, 20240628
    //public MyPanel(JFrame f) {
    public MyPanel(JFrame f, int iScreenWidth, int iScreenHeight) {

		//added by Mike, 20240622
		myJFrameInstance = f;

		//added by Mike, 20240708
		this.iScreenWidth=iScreenWidth;
		this.iScreenHeight=iScreenHeight;

		//added by Mike, 20240706
		//TODO: square frame; margins, the excess
		//iScreenWidth=iScreenHeight;

		//added by Mike, 20240708
		//square screen; make the excess, margins
		System.out.println("iScreenWidth: "+iScreenWidth);
		System.out.println("iScreenHeight: "+iScreenHeight);

    //edited by Mike, 20240722; from 20240718
    //reminder: screen width must be greater than the screen height
	//iScreenWidth will be set to be iScreenHeight
    iOffsetScreenWidthLeftMargin=(iScreenWidth-iScreenHeight)/2;
    iScreenWidth=iScreenHeight;

/*		//edited by Mike, 20240714
    iTileWidthCountMax=iScreenWidth/iTileWidth;
    iTileHeightCountMax=iScreenHeight/iTileHeight;
*/
    iTileWidth=iScreenWidth/iTileWidthCountMax;
    iTileHeight=iScreenHeight/iTileHeightCountMax;
	
	//System.out.println("iTileHeight: "+iTileHeight);

		//edited by Mike, 20240720; from 20240718
		iOffsetScreenHeightTopMargin=(iScreenHeight-(iTileHeight*iTileHeightCountMax))/2;

		System.out.println("iOffsetScreenWidthLeftMargin: "+iOffsetScreenWidthLeftMargin);

		System.out.println("iOffsetScreenHeightTopMargin: "+iOffsetScreenHeightTopMargin);

    //added by Mike, 20240718
    //TODO: -use: this as input parameter, instead of screenWidth and screenHeight
    iStageWidth=iTileWidth*iTileWidthCountMax;
    iStageHeight=iTileHeight*iTileHeightCountMax;

		//added by Mike, 20240720
		System.out.println("iStageWidth: "+iStageWidth);
		System.out.println("iStageHeight: "+iStageHeight);


		redSquare  = new RedSquare();
		//edited by Mike, 20240714; from 20240628
		//myRobotShip = new RobotShip();
		//edited by Mike, 20240719
		//myRobotShip = new RobotShip(iOffsetScreenWidthLeftMargin,0,iScreenWidth, iScreenHeight, iTileWidth, iTileHeight);
		myRobotShip = new RobotShip(iOffsetScreenWidthLeftMargin,iOffsetScreenHeightTopMargin,iStageWidth, iStageHeight, iTileWidth, iTileHeight);
		
		//edited by Mike, 20240804; from 20240802
/*		
		myEnemyShip = new EnemyRobotShip(iOffsetScreenWidthLeftMargin,iOffsetScreenHeightTopMargin,iStageWidth, iStageHeight, iTileWidth, iTileHeight);
		myEnemyShip.setX(0+iOffsetScreenWidthLeftMargin);
		myEnemyShip.setY(0);
*/		
		myLevel2D = new Level2D(0+iOffsetScreenWidthLeftMargin,0+iOffsetScreenHeightTopMargin,iStageWidth, iStageHeight, iTileWidth, iTileHeight); 

		//edited by Mike, 20240719; from 20240714
		//myBackgroundCanvas = new BackgroundCanvas(0+iOffsetScreenWidthLeftMargin,0+iOffsetScreenHeightTopMargin,iScreenWidth, iScreenHeight, iTileWidth, iTileHeight);
		myBackgroundCanvas = new BackgroundCanvas(0+iOffsetScreenWidthLeftMargin,0+iOffsetScreenHeightTopMargin,iStageWidth, iStageHeight, iTileWidth, iTileHeight);

        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                moveSquare(e.getX(),e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent e){
                moveSquare(e.getX(),e.getY());
            }
        });

		//added by Mike, 20240622
		addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent key) {
                //System.out.println("Key pressed.");

				if (key.getKeyCode() == KeyEvent.VK_ESCAPE){
					System.out.println("ESC.");

					//TODO: -add: open exit menu
					myJFrameInstance.dispatchEvent(new WindowEvent(myJFrameInstance, WindowEvent.WINDOW_CLOSING));
				}

				//added by Mike, 20240629
				myRobotShip.keyPressed(key);
				
				//added by Mike, 20240729
				myBackgroundCanvas.keyPressed(key);
				
				//edited by Mike, 20240804; from 20240802
				myLevel2D.keyPressed(key);
            }

			public void keyReleased(KeyEvent key) {
				myRobotShip.keyReleased(key);

				//added by Mike, 20240729
				myBackgroundCanvas.keyReleased(key);
				
				//edited by Mike, 20240804; from 20240802
				myLevel2D.keyReleased(key);
			}

            public void keyTyped(KeyEvent key) {}
		});
    }

    private void moveSquare(int x, int y){

        // Current square state, stored as final variables
        // to avoid repeat invocations of the same methods.
        final int CURR_X = redSquare.getX();
        final int CURR_Y = redSquare.getY();
        final int CURR_W = redSquare.getWidth();
        final int CURR_H = redSquare.getHeight();
        final int OFFSET = 1;

        if ((CURR_X!=x) || (CURR_Y!=y)) {

            // The square is moving, repaint background
            // over the old square location.
			//removed by Mike, 20240627
			//repaint already done by TimerTask
            //repaint(CURR_X,CURR_Y,CURR_W+OFFSET,CURR_H+OFFSET);

            // Update coordinates.
            redSquare.setX(x);
            redSquare.setY(y);

            // Repaint the square at the new location.
/*			//removed by Mike, 20240627;
			//repaint already done by TimerTask
            repaint(redSquare.getX(), redSquare.getY(),
                    redSquare.getWidth()+OFFSET,
                    redSquare.getHeight()+OFFSET);
*/
        }
    }

	//added by Mike, 20240719
	@Override
    public Dimension getPreferredSize() {
        //edited by Mike, 20240720
		//note iScreenWidth and iScreenHeight may not yet have been updated
		//return new Dimension(250,200);
		return new Dimension(800,800);
    }

	public void update() {
	  myRobotShip.update();
	  
	  //added by Mike, 20240729
	  myBackgroundCanvas.update();

	  //edited by Mike, 20240804; from 20240802
/*
	  myEnemyShip.update();
	  
	  //added by Mike, 20240803
	  myRobotShip.collideWith(myEnemyShip);
*/
	  myLevel2D.update();
	  myLevel2D.collideWith(myRobotShip);	  
	}

	//added by Mike, 20240719
	@Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

//System.out.println(">>>>> DITO");

		//TODO: -add: background
		//array-based mapping

		//edited by Mike, 20240722; from 20240708
		//entire available screen
		g.setColor(Color.decode("#adb2b6")); //gray
		g.fillRect(0,0,iScreenWidth,iScreenHeight);		
		//g.drawLine(iTileWidth,iTileHeight,iScreenWidth,iTileHeight);

		//square screen; make the excess, margins
		//g.setColor(Color.black);
		//g.setColor(Color.gray); //white
		g.setColor(Color.decode("#31363a")); //lubuntu, BreezeModified theme; dark terminal

		//edited by Mike, 20240718		//g.fillRect(0+iOffsetScreenWidthLeftMargin,0,iTileWidth*iTileWidthCountMax,iScreenHeight);
		//g.fillRect(0+iOffsetScreenWidthLeftMargin,0+iOffsetScreenHeightTopMargin,iTileWidth*iTileWidthCountMax,iTileHeight*iTileHeightCountMax-iOffsetScreenHeightTopMargin);
    //g.fillRect(0+iOffsetScreenWidthLeftMargin,0+iOffsetScreenHeightTopMargin,iStageWidth,iStageHeight-iOffsetScreenHeightTopMargin);
    g.fillRect(0+iOffsetScreenWidthLeftMargin,0+iOffsetScreenHeightTopMargin,iStageWidth,iStageHeight);

/*
		System.out.println("iScreenWidth: "+iScreenWidth);
		System.out.println("iTileWidth*iTileWidthCountMax: "+iTileWidth*iTileWidthCountMax);

        //iScreenWidth not yet set to be equal to iScreenHeight; multithreaded?
		//g.fillRect(0+iOffsetScreenWidthLeftMargin,0,iScreenWidth-iOffsetScreenWidthLeftMargin*2,iScreenHeight);
*/
		//edited by Mike, 20240722
		//g.setColor(Color.green);
		g.setColor(Color.decode("#33c699")); //lubuntu, BreezeModified theme; dark terminal
		
		
    //added by Mike, 20240720
/*
System.out.println("iTileHeightCountMax: "+iTileHeightCountMax);
System.out.println("iTileWidthCountMax: "+iTileWidthCountMax);
*/
		//draw horizontal line
		//include the last line
		for (int i=0; i<=iTileHeightCountMax; i++) {
			//g.drawLine(0+iOffsetScreenWidthLeftMargin,iTileHeight*i,iScreenWidth-iOffsetScreenWidthLeftMargin,iTileHeight*i);
      //g.drawLine(0+iOffsetScreenWidthLeftMargin,iTileHeight*i,iOffsetScreenWidthLeftMargin+iTileWidth*iTileWidthCountMax,iTileHeight*i);
	  //edited by Mike, 20240719
      //g.drawLine(0+iOffsetScreenWidthLeftMargin,iTileHeight*i,iOffsetScreenWidthLeftMargin+iStageWidth,iTileHeight*i);
	  g.drawLine(0+iOffsetScreenWidthLeftMargin,iOffsetScreenHeightTopMargin+iTileHeight*i,iOffsetScreenWidthLeftMargin+iStageWidth,iOffsetScreenHeightTopMargin+iTileHeight*i);
		}
		//draw vertical line
		//include the last line
		for (int j=0; j<=iTileWidthCountMax; j++) {
			//edited by Mike, 20240720; from 20240718
      //g.drawLine(iOffsetScreenWidthLeftMargin+iTileWidth*j,0+iOffsetScreenHeightTopMargin,iOffsetScreenWidthLeftMargin+iTileWidth*j,iStageHeight);
      g.drawLine(iOffsetScreenWidthLeftMargin+iTileWidth*j,0+iOffsetScreenHeightTopMargin,iOffsetScreenWidthLeftMargin+iTileWidth*j,iOffsetScreenHeightTopMargin+iStageHeight);
		}

        //edited by Mike, 20240622
		//g.drawString("This is my custom Panel!",10,20);
		//g.drawString("HALLO!",150,20);

        redSquare.paintSquare(g);
		
		//added by Mike, 20240711
		//put this here; uses Graphics2D; with scale, etc.
		//Graphics2D shared;
		myBackgroundCanvas.draw(g);

		//added by Mike, 20240802
		myLevel2D.draw(g);

		myRobotShip.draw(g);		
    }
}

class RedSquare{

    private int xPos = 50;
    private int yPos = 50;
    private int width = 20;
    private int height = 20;

    public void setX(int xPos){
        this.xPos = xPos;
    }

    public int getX(){
        return xPos;
    }

    public void setY(int yPos){
        this.yPos = yPos;
    }

    public int getY(){
        return yPos;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void paintSquare(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(xPos,yPos,width,height);
        g.setColor(Color.BLACK);
        g.drawRect(xPos,yPos,width,height);
    }
}

//added by Mike, 20240802
//Reference: UsbongPagong
class Actor {
	//added by Mike, 20240804
	protected final int INITIALIZING_STATE = 0;
	protected final int MOVING_STATE = 1;
	protected final int IN_TITLE_STATE = 2; //TODO: -verify this
	protected final int DYING_STATE = 3;

	protected final int HIDDEN_STATE = 4;
	protected final int ACTIVE_STATE = 5;
	
	protected int currentState=ACTIVE_STATE;
	
	protected boolean isCollidable; 
	
	protected int iWidth=0;
	protected int iHeight=0;
	protected int iXPos=0;
	protected int iYPos=0;
	
	//edited by Mike, 20240804; from 20240730
	protected final int ISTEP_X_DEFAULT=1;
	protected final int ISTEP_Y_DEFAULT=1;
	
	protected int iStepX=ISTEP_X_DEFAULT; //4;
	protected int iStepY=ISTEP_Y_DEFAULT; //4;
	
	//added by Mike, 20240629
	protected final int KEY_W=0; //same as key UP
	protected final int KEY_S=1; //same as key DOWN
	protected final int KEY_D=2; //same as key RIGHT
	protected final int KEY_A=3; //same as key LEFT
	protected final int iNumOfKeyTypes=4;
	protected boolean myKeysDown[];

	protected int iFrameCount=0;
	protected int iFrameCountMax=4;
	protected int iFrameCountDelay=0;
	protected int iFrameCountDelayMax=20;
	
	//added by Mike, 20240626
	protected int iRotationDegrees=0;
	protected int iFrameWidth=128;
	protected int iFrameHeight=128;

	protected BufferedImage myBufferedImage;

	//added by Mike, 20240708
	protected int iOffsetScreenWidthLeftMargin=0;
	protected int iOffsetScreenHeightTopMargin=0;

	//edited by Mike, 20240719; from 20240628
/*
	private int iScreenWidth=0;
	private int iScreenHeight=0;
*/
	protected int iStageWidth=0;
	protected int iStageHeight=0;

	//added by Mike, 20240714
	protected int iTileWidth=0;
	protected int iTileHeight=0;
	
	//added by Mike, 20240804
	protected int[][] tileMap; 
	protected final int MAX_TILE_MAP_HEIGHT=13;
	protected final int MAX_TILE_MAP_WIDTH=26; 

	//added by Mike, 20240714
	protected final int FACING_UP=0;
	protected final int FACING_DOWN=1;
	protected final int FACING_LEFT=2;
	protected final int FACING_RIGHT=3;
	protected int currentFacingState=0;
	
	//added by Mike, 20240730
	protected boolean bHasStarted=false;
	
	//added by Mike, 20240806
	protected int iViewPortX;
	protected int iViewPortY;
	protected int iViewPortWidth;
	protected int iViewPortHeight;
	
    public Actor(int iOffsetScreenWidthLeftMargin, int iOffsetScreenHeightTopMargin, int iStageWidth, int iStageHeight, int iTileWidth, int iTileHeight) {
	  try {
		  //edited by Mike, 20240706
		  //myBufferedImage = ImageIO.read(new File("../res/count.png"));
		  //edited by Mike, 20240720
		  //myBufferedImage = ImageIO.read(new File("../res/robotship.png"));
		  //app executed from base directory
		  myBufferedImage = ImageIO.read(new File("./res/robotship.png"));
      } catch (IOException ex) {
      }

	  //added by Mike, 20240708
	  this.iOffsetScreenWidthLeftMargin=iOffsetScreenWidthLeftMargin;
	  this.iOffsetScreenHeightTopMargin=iOffsetScreenHeightTopMargin;

	  //added by Mike, 20240628
	  this.iStageWidth=iStageWidth;
	  this.iStageHeight=iStageHeight;

	  //added by Mike, 20240714
	  this.iTileWidth=iTileWidth;
	  this.iTileHeight=iTileHeight;

	  //added by Mike, 20240629
	  myKeysDown = new boolean[iNumOfKeyTypes];
	  
	  //added by Mike, 20240804
	  tileMap = new int[MAX_TILE_MAP_HEIGHT][MAX_TILE_MAP_WIDTH];

	  reset();
	}

	//added by Mike, 20240628
	public void reset() {
		//added by Mike, 20240804
		//reminder: trans.scale((iTileWidth*1.0)/iFrameWidth,(iTileHeight*1.0)/iFrameHeight);
		iWidth=iFrameWidth;
		iHeight=iFrameHeight;

		//added by Mike, 20240714
		currentFacingState=FACING_RIGHT;

		//edited by Mike, 20240719
		int iStageCenterWidth=(iStageWidth/2/iTileWidth)*iTileWidth; //iTileWidth*6
		int iStageCenterHeight=(iStageHeight/2/iTileHeight)*iTileHeight; //iTileHeight*6

		setX(iOffsetScreenWidthLeftMargin+0+iStageCenterWidth);
		setY(iOffsetScreenHeightTopMargin+0+iStageCenterHeight);

		iFrameCount=0;
		iFrameCountDelay=0;
		iRotationDegrees=0;

		//added by Mike, 20240629
		int iMyKeysDownLength = myKeysDown.length;
		for (int i=0; i<iMyKeysDownLength; i++) {
			myKeysDown[i]=false;
		}		
		//added by Mike, 20240730
		myKeysDown[KEY_D]=true;
		
		//added by Mike, 20240804
		currentState=ACTIVE_STATE;
		isCollidable=true;
	}

	//added by Mike, 20240804
    public boolean isActive() {
        if (currentState==ACTIVE_STATE) {
            return true;
		}
        else {
			return false;
		}
    }

    public void setX(int iXPos ){
        this.iXPos = iXPos;
    }

    public int getX(){
        return iXPos;
    }

    public void setY(int iYPos){
        this.iYPos = iYPos;
    }

    public int getY(){
        return iYPos;
    }

    public int getWidth(){
        return iWidth;
    }

    public int getHeight(){
        return iHeight;
    }
	
	public int getStepX(){
        return iStepX;
    }

	public int getStepY(){
        return iStepY;
    }
	
	//added by Mike, 20240804
	public boolean checkIsCollidable() {
		return isCollidable;
	}

	//added by Mike, 20240804
	public void setCollidable(boolean c) {
		 isCollidable=c;
	}
	
	//added by Mike, 20240805
	public void setCurrentFacingState(int f) {
		currentFacingState=f;
	}

	public void update() {	
		//edited by Mike, 20240729
		if (myKeysDown[KEY_A])
		{
			currentFacingState=FACING_LEFT;
		}

		if (myKeysDown[KEY_D])
		{
			currentFacingState=FACING_RIGHT;
		}
		
		//added by Mike, 20240730; from 20240729
		switch(currentFacingState) {
			case FACING_RIGHT:
				//setX(getX()+iStepX);
				break;
			case FACING_LEFT:
				//setX(getX()-iStepX);
				break;			
		}
		
		//added by Mike, 20240730
		if (!bHasStarted) {
			myKeysDown[KEY_D]=false;
			bHasStarted=true;
		}
		
		return;

/* 	//edited by Mike, 20240706; OK
		//animation
		if (iFrameCountDelay<iFrameCountDelayMax) {
			iFrameCountDelay++;
		}
		else {
			iFrameCount=(iFrameCount+1)%iFrameCountMax;
			iFrameCountDelay=0;
		}
		iFrameCount=0;
*/		
	}
	
/*
	//added by Mike, 20240629
	public void move(KeyEvent key) {
		//horizontal movement
		if ((key.getKeyCode() == KeyEvent.VK_A) || (key.getKeyCode() == KeyEvent.VK_LEFT)) {
			setX(getX()-iStepX);
		}

		if ((key.getKeyCode() == KeyEvent.VK_D) || (key.getKeyCode() == KeyEvent.VK_RIGHT)) {
			setX(getX()+iStepX);
		}

		//vertical movement
		if ((key.getKeyCode() == KeyEvent.VK_W) || (key.getKeyCode() == KeyEvent.VK_UP)) {
			setY(getY()-iStepY);
		}

		if ((key.getKeyCode() == KeyEvent.VK_S) || (key.getKeyCode() == KeyEvent.VK_DOWN)) {
			setY(getY()+iStepY);
		}
	}
*/

	public void keyPressed(KeyEvent key) {
		//added by Mike, 20240629
		//horizontal movement
		if ((key.getKeyCode() == KeyEvent.VK_A) || (key.getKeyCode() == KeyEvent.VK_LEFT)) {
			myKeysDown[KEY_A]=true;
		}

		if ((key.getKeyCode() == KeyEvent.VK_D) || (key.getKeyCode() == KeyEvent.VK_RIGHT)) {
			myKeysDown[KEY_D]=true;
		}

		//vertical movement
		if ((key.getKeyCode() == KeyEvent.VK_W) || (key.getKeyCode() == KeyEvent.VK_UP)) {
			myKeysDown[KEY_W]=true;
		}

		if ((key.getKeyCode() == KeyEvent.VK_S) || (key.getKeyCode() == KeyEvent.VK_DOWN)) {
			myKeysDown[KEY_S]=true;
		}
	}

	public void keyReleased(KeyEvent key) {
		//added by Mike, 20240629
		//horizontal movement
		if ((key.getKeyCode() == KeyEvent.VK_A) || (key.getKeyCode() == KeyEvent.VK_LEFT)) {
			myKeysDown[KEY_A]=false;
		}

		if ((key.getKeyCode() == KeyEvent.VK_D) || (key.getKeyCode() == KeyEvent.VK_RIGHT)) {
			myKeysDown[KEY_D]=false;
		}

		//vertical movement
		if ((key.getKeyCode() == KeyEvent.VK_W) || (key.getKeyCode() == KeyEvent.VK_UP)) {
			myKeysDown[KEY_W]=false;
		}

		if ((key.getKeyCode() == KeyEvent.VK_S) || (key.getKeyCode() == KeyEvent.VK_DOWN)) {
			myKeysDown[KEY_S]=false;
		}
	}

	//added by Mike, 20240803	
	//reference: UsbongPagong
	public void hitBy(Actor a) {
	}

	//edited by Mike, 20240804
/*	//UsbongPagong
	public boolean isIntersectingRect(Actor a1, Actor a2) {     
		if (a2.getY()+a2.getHeight() < a1.getY() || //is the bottom of a2 above the top of a1?
			a2.getY() > a1.getY()+a1.getHeight() || //is the top of a2 below the bottom of a1?
			a2.getX()+a2.getWidth() < a1.getX()  || //is the right of a2 to the left of a1?
			a2.getX() > a1.getX()+a1.getWidth()) { //is the left of a2 to the right of a1?
			return false;
		}
		
		return true;
	}
*/
	//UsbongV2 
/*	
	if ((fTilePosX > getXPos()+getWidth()-iOffsetXPosAsPixel+getStepX()) || 
					//tile position at left of object
		(fTilePosX+fTileWidth < getXPos()+iOffsetXPosAsPixel -getStepX()) ||
		//tile position at top of object
		(fTilePosY+fTileHeight < getYPos()+iOffsetYPosAsPixel -getStepY()) || 
					//tile position at bottom of object            
		(fTilePosY > getYPos()+getHeight()-iOffsetYPosAsPixel +getStepY())) { 
*/
//Example values: iOffsetXPosAsPixel=10; iMyWidthAsPixel=64; 10/64=0.15625
	public boolean isIntersectingRect(Actor a1, Actor a2) {
/*		
		if ((a2.getY()+a2.getHeight() < a1.getY()-getStepY()) || //is the bottom of a2 above the top of a1?
			(a2.getY() > a1.getY()+a1.getHeight()+getStepY()) || //is the top of a2 below the bottom of a1?
			(a2.getX()+a2.getWidth() < a1.getX()-getStepX())  || //is the right of a2 to the left of a1?
			(a2.getX() > a1.getX()+a1.getWidth()+getStepX())) { //is the left of a2 to the right of a1?
*/

		//iOffsetXPosAsPixel=12; a1.getHeight()=83; 12/83=0.14457
/*		
		int iOffsetYPosAsPixel=new Double(a1.getHeight()*0.15625).intValue();
		int iOffsetXPosAsPixel=new Double(a1.getWidth()*0.15625).intValue();
*/
		//object and object; not object and tile
		int iOffsetYPosAsPixel=a1.getHeight()/3;//4; //diagonal hit; image has margin
		int iOffsetXPosAsPixel=a1.getWidth()/3;//4;

		//System.out.println(">>>> a1.getHeight(): "+a1.getHeight());
		//System.out.println(">>>> iOffsetYPosAsPixel: "+iOffsetYPosAsPixel);

		if ((a2.getY()+a2.getHeight() < a1.getY()+iOffsetYPosAsPixel-a1.getStepY()) || //is the bottom of a2 above the top of a1?
			(a2.getY() > a1.getY()+a1.getHeight()-iOffsetYPosAsPixel+a1.getStepY()) || //is the top of a2 below the bottom of a1?
			(a2.getX()+a2.getWidth() < a1.getX()+iOffsetXPosAsPixel-a1.getStepX())  || //is the right of a2 to the left of a1?
			(a2.getX() > a1.getX()+a1.getWidth()-iOffsetXPosAsPixel+a1.getStepX())) { //is the left of a2 to the right of a1?

			return false;
		}
		
		return true;
	}
			
	public void collideWith(Actor a) {

		if ((!checkIsCollidable())||(!a.checkIsCollidable()))    
		{
			//not collidable
			return;
		}

		if (isIntersectingRect(this, a))
		{
			//System.out.println("COLLISION!");
			
			this.hitBy(a);
			a.hitBy(this);
		}
	}


//Additional Reference: 	https://docs.oracle.com/javase/tutorial/2d/advanced/examples/ClipImage.java; last accessed: 20240625
  public void draw(Graphics g) {
	//TODO: -verify: if clip still has to be cleared
	Rectangle2D rect = new Rectangle2D.Float();

    //added by Mike, 20240623
    AffineTransform identity = new AffineTransform();

    Graphics2D g2d = (Graphics2D)g;
    AffineTransform trans = new AffineTransform();
    trans.setTransform(identity);
    //300 is object position;
    //trans.translate(300-iFrameCount*128, 0);
    //trans.translate(-iFrameCount*128, 0);

	//added by Mike, 20240625
	//note clip rect has to also be updated;
	//trans.scale(2,2); //1.5,1.5
	//put scale after translate position;

/*  //reference: https://stackoverflow.com/questions/8721312/java-image-cut-off; last accessed: 20240623
    //animating image doable, but shall need more computations;
    //sin, cos; Bulalakaw Wars;
    trans.translate(128/2,128/2);
    trans.rotate(Math.toRadians(45)); //input in degrees
    trans.translate(-128/2,-128/2);
*/
	//reminder: when objects moved in x-y coordinates, rotation's reference point also moves with the update;
	//"compounded translate and rotate"
	//https://stackoverflow.com/questions/32513508/rotating-java-2d-graphics-around-specified-point; last accessed: 20240625
	//answered by: MadProgrammer, 20150911T00:48; from 20150911T00:41

	//update x and y positions before rotate
	//object position x=300, y=0;
    //trans.translate(300,0);
    //edited by Mike, 20240628
	//trans.translate(300,300);
	//edited by Mike, 20240714
	trans.translate(getX(),getY());

	//scales from top-left as reference point
    //trans.scale(2,2);
	//rotates using top-left as anchor
    //trans.rotate(Math.toRadians(45)); //input in degrees

/* //removed by Mike, 20240706; OK
	//rotate at center; put translate after rotate
	iRotationDegrees=(iRotationDegrees+10)%360;
    trans.rotate(Math.toRadians(iRotationDegrees));
    trans.translate(-iFrameWidth/2,-iFrameHeight/2);
*/

	//edited by Mike, 20240714; from 20240708
/*
	System.out.println("iTileWidth: "+iTileWidth);
	System.out.println("iTileHeight: "+iTileHeight);
*/
	//scale to size of iTileWidth and iTileHeight;
	//example: iTileWidth=83
	//iFrameWidth=128;
	//trans.scale(0.5,0.5);
	//double temp = iTileWidth*1.0/iFrameWidth;
	//System.out.println("temp: "+temp);
	trans.scale((iTileWidth*1.0)/iFrameWidth,(iTileHeight*1.0)/iFrameHeight);

	//added by Mike, 20240714
	//put this after scale;
	//move the input image to the correct row of the frame
	//TODO: -add: collision detection; UsbongV2
	if (currentFacingState==FACING_RIGHT) {
		//trans.translate(getX(),getY());
	}
	else { //FACING_LEFT
		trans.translate(0,0-iFrameHeight);
	}

	//added by Mike, 20240625
	g2d.setTransform(trans);

	if (currentFacingState==FACING_RIGHT) {
		//no animation yet; 0+iFrameCount*iFrameWidth-iFrameCount*iFrameWidth
	    rect.setRect(0, 0, iFrameWidth, iFrameHeight);
	}
	else { //FACING_LEFT
	   rect.setRect(0, 0+iFrameHeight, iFrameWidth, iFrameHeight);
	}

	Area myClipArea = new Area(rect);

    //edited by Mike, 20240625; from 20240623
    g2d.setClip(myClipArea);
    
	//edited by Mike, 20240714
    g2d.drawImage(myBufferedImage,-iFrameCount*iFrameWidth, 0, null);

	//removed by Mike, 20240711; from 20240625
	//put after the last object to be drawn
	//g2d.dispose();
  }
}

//added by Mike, 20240622
class RobotShip extends Actor {

    public RobotShip(int iOffsetScreenWidthLeftMargin, int iOffsetScreenHeightTopMargin, int iStageWidth, int iStageHeight, int iTileWidth, int iTileHeight) {
	  super(iOffsetScreenWidthLeftMargin, iOffsetScreenHeightTopMargin, iStageWidth, iStageHeight, iTileWidth, iTileHeight);
		
	  try {
		  //edited by Mike, 20240706
		  //myBufferedImage = ImageIO.read(new File("../res/count.png"));
		  //edited by Mike, 20240720
		  //myBufferedImage = ImageIO.read(new File("../res/robotship.png"));
		  //app executed from base directory
		  myBufferedImage = ImageIO.read(new File("./res/robotship.png"));
      } catch (IOException ex) {
      }
	}

	//added by Mike, 20240628
	@Override
	public void reset() {		
		//edited by Mike, 20240804
		//reminder: trans.scale((iTileWidth*1.0)/iFrameWidth,(iTileHeight*1.0)/iFrameHeight);
/*		
		iWidth=iFrameWidth;
		iHeight=iFrameHeight;
*/
		iWidth=iTileWidth;
		iHeight=iTileHeight;
		
		//added by Mike, 20240714
		currentFacingState=FACING_RIGHT;

		//edited by Mike, 20240719
		int iStageCenterWidth=(iStageWidth/2/iTileWidth)*iTileWidth; //iTileWidth*6
		int iStageCenterHeight=(iStageHeight/2/iTileHeight)*iTileHeight; //iTileHeight*6

		setX(iOffsetScreenWidthLeftMargin+0+iStageCenterWidth);
		setY(iOffsetScreenHeightTopMargin+0+iStageCenterHeight);

		iFrameCount=0;
		iFrameCountDelay=0;
		iRotationDegrees=0;

		//added by Mike, 20240629
		int iMyKeysDownLength = myKeysDown.length;
		for (int i=0; i<iMyKeysDownLength; i++) {
			myKeysDown[i]=false;
		}		
		//added by Mike, 20240730
		myKeysDown[KEY_D]=true;
		
		//added by Mike, 20240804
		currentState=ACTIVE_STATE;
		isCollidable=true;
		
/*		
		//TODO: -add: , etc.
		iStepX=ISTEP_X_DEFAULT*2; //faster by 1 than the default
		iStepY=ISTEP_Y_DEFAULT*2; //faster by 1 than the default
*/		
	}
	
	@Override
	public void update() {	
		//edited by Mike, 20240729
		if (myKeysDown[KEY_A])
		{
			currentFacingState=FACING_LEFT;
		}

		if (myKeysDown[KEY_D])
		{
			currentFacingState=FACING_RIGHT;
		}		
		
		//added by Mike, 20240730; from 20240729
		switch(currentFacingState) {
			case FACING_RIGHT:
				//setX(getX()+iStepX);
				break;
			case FACING_LEFT:
				//setX(getX()-iStepX);
				break;			
		}
		
		//added by Mike, 20240730
		if (!bHasStarted) {
			myKeysDown[KEY_D]=false;
			bHasStarted=true;
		}
		
		return;
/*		
		//removed by Mike, 20240629
		//movement
		//setX(getX()+iStepX);

		if (myKeysDown[KEY_A])
		{
			setX(getX()-iStepX);

			//added by Mike, 20240714
			currentFacingState=FACING_LEFT;
		}

		if (myKeysDown[KEY_D])
		{
			setX(getX()+iStepX);

			//added by Mike, 20240714
			currentFacingState=FACING_RIGHT;
		}

		if (myKeysDown[KEY_W])
		{
			setY(getY()-iStepY);
		}

		if (myKeysDown[KEY_S])
		{
			setY(getY()+iStepY);
		}
*/		

/* 	//edited by Mike, 20240706; OK
		//animation
		if (iFrameCountDelay<iFrameCountDelayMax) {
			iFrameCountDelay++;
		}
		else {
			iFrameCount=(iFrameCount+1)%iFrameCountMax;
			iFrameCountDelay=0;
		}
		iFrameCount=0;
*/		
	}
	

//Additional Reference: 	https://docs.oracle.com/javase/tutorial/2d/advanced/examples/ClipImage.java; last accessed: 20240625
  @Override
  public void draw(Graphics g) {
	//TODO: -verify: if clip still has to be cleared
	Rectangle2D rect = new Rectangle2D.Float();

	//added by Mike, 20240621
	//iFrameCount=2;

	//rect.setRect(0, 0, 128, 128);
	//rect.setRect(iFrameCount*128, 0, 128, 128);

/*
	g.setClip(myClipArea);
	//added by Mike, 20240621
	//g.drawImage(myBufferedImage, 0, 0, this);
	g.drawImage(myBufferedImage, 0-iFrameCount*128, 0, null);
*/
    //added by Mike, 20240623
    AffineTransform identity = new AffineTransform();

    Graphics2D g2d = (Graphics2D)g;
    AffineTransform trans = new AffineTransform();
    trans.setTransform(identity);
    //300 is object position;
    //trans.translate(300-iFrameCount*128, 0);
    //trans.translate(-iFrameCount*128, 0);

	//added by Mike, 20240625
	//note clip rect has to also be updated;
	//trans.scale(2,2); //1.5,1.5
	//put scale after translate position;

/*  //reference: https://stackoverflow.com/questions/8721312/java-image-cut-off; last accessed: 20240623
    //animating image doable, but shall need more computations;
    //sin, cos; Bulalakaw Wars;
    trans.translate(128/2,128/2);
    trans.rotate(Math.toRadians(45)); //input in degrees
    trans.translate(-128/2,-128/2);
*/
	//reminder: when objects moved in x-y coordinates, rotation's reference point also moves with the update;
	//"compounded translate and rotate"
	//https://stackoverflow.com/questions/32513508/rotating-java-2d-graphics-around-specified-point; last accessed: 20240625
	//answered by: MadProgrammer, 20150911T00:48; from 20150911T00:41

	//update x and y positions before rotate
	//object position x=300, y=0;
    //trans.translate(300,0);
    //edited by Mike, 20240628
	//trans.translate(300,300);
	//edited by Mike, 20240714
	trans.translate(getX(),getY());

	//scales from top-left as reference point
    //trans.scale(2,2);
	//rotates using top-left as anchor
    //trans.rotate(Math.toRadians(45)); //input in degrees

/* //removed by Mike, 20240706; OK
	//rotate at center; put translate after rotate
	iRotationDegrees=(iRotationDegrees+10)%360;
    trans.rotate(Math.toRadians(iRotationDegrees));
    trans.translate(-iFrameWidth/2,-iFrameHeight/2);
*/

	//edited by Mike, 20240714; from 20240708
/*
	System.out.println("iTileWidth: "+iTileWidth);
	System.out.println("iTileHeight: "+iTileHeight);
*/
	//scale to size of iTileWidth and iTileHeight;
	//example: iTileWidth=83
	//iFrameWidth=128;
	//trans.scale(0.5,0.5);
	//double temp = iTileWidth*1.0/iFrameWidth;
	//System.out.println("temp: "+temp);
	trans.scale((iTileWidth*1.0)/iFrameWidth,(iTileHeight*1.0)/iFrameHeight);

	//added by Mike, 20240714
	//put this after scale;
	//move the input image to the correct row of the frame
	//TODO: -add: collision detection; UsbongV2
	if (currentFacingState==FACING_RIGHT) {
		//trans.translate(getX(),getY());
	}
	else { //FACING_LEFT
		trans.translate(0,0-iFrameHeight);
	}

	//added by Mike, 20240625
	g2d.setTransform(trans);

	//note: clip rect has to move with object position
    //edited by Mike, 20240623
    //reminder:
    //300 is object position;
    //iFrameCount*128 is the animation frame to show;
    //-iFrameCount*128 is move the object position to the current frame;
    //rect.setRect(300+iFrameCount*128-iFrameCount*128, 0, 128, 128);
	//edited by Mike, 20240714; from 20240714
    //rect.setRect(0+iFrameCount*iFrameWidth-iFrameCount*iFrameWidth, 0, iFrameWidth, iFrameHeight);

	if (currentFacingState==FACING_RIGHT) {
		//no animation yet; 0+iFrameCount*iFrameWidth-iFrameCount*iFrameWidth
	    rect.setRect(0, 0, iFrameWidth, iFrameHeight);
	}
	else { //FACING_LEFT
	   rect.setRect(0, 0+iFrameHeight, iFrameWidth, iFrameHeight);
	}

	Area myClipArea = new Area(rect);

    //edited by Mike, 20240625; from 20240623
    g2d.setClip(myClipArea);
    //g.setClip(myClipArea);

    //g2d.drawImage(image, trans, this);
    //g2d.drawImage(myBufferedImage, trans, null);
    //g2d.drawImage(myBufferedImage,300-iFrameCount*128, 0, null);

	//edited by Mike, 20240714
    g2d.drawImage(myBufferedImage,-iFrameCount*iFrameWidth, 0, null);

	//removed by Mike, 20240711; from 20240625
	//put after the last object to be drawn
	//g2d.dispose();
  }
}

//edited by Mike, 20240805; from 20240802
class EnemyAircraft extends Actor {

    public EnemyAircraft(int iOffsetScreenWidthLeftMargin, int iOffsetScreenHeightTopMargin, int iStageWidth, int iStageHeight, int iTileWidth, int iTileHeight) {
	  super(iOffsetScreenWidthLeftMargin, iOffsetScreenHeightTopMargin, iStageWidth, iStageHeight, iTileWidth, iTileHeight);
		
	  try {
		  //edited by Mike, 20240706
		  //myBufferedImage = ImageIO.read(new File("../res/count.png"));
		  //edited by Mike, 20240720
		  //myBufferedImage = ImageIO.read(new File("../res/robotship.png"));
		  //app executed from base directory
		  myBufferedImage = ImageIO.read(new File("./res/robotship.png"));
      } catch (IOException ex) {
      }
	}

	//added by Mike, 20240628
	@Override
	public void reset() {
		//edited by Mike, 20240804
		//reminder: trans.scale((iTileWidth*1.0)/iFrameWidth,(iTileHeight*1.0)/iFrameHeight);
/*		
		iWidth=iFrameWidth;
		iHeight=iFrameHeight;
*/
		iWidth=iTileWidth;
		iHeight=iTileHeight;		

		//added by Mike, 20240805; from 20240714
		currentFacingState=FACING_LEFT;

/*		//removed by Mike, 20240804; set in Level2D
		//edited by Mike, 20240719
		int iStageCenterWidth=(iStageWidth/2/iTileWidth)*iTileWidth; //iTileWidth*6
		int iStageCenterHeight=(iStageHeight/2/iTileHeight)*iTileHeight; //iTileHeight*6

		setX(iOffsetScreenWidthLeftMargin+0+iStageCenterWidth);
		setY(iOffsetScreenHeightTopMargin+0+iStageCenterHeight);
*/		

		iFrameCount=0;
		iFrameCountDelay=0;
		iRotationDegrees=0;

		//added by Mike, 20240629
		int iMyKeysDownLength = myKeysDown.length;
		for (int i=0; i<iMyKeysDownLength; i++) {
			myKeysDown[i]=false;
		}		
		//removed by Mike, 20240805; from 20240730
		//myKeysDown[KEY_D]=true; //false;
		
		//added by Mike, 20240804		
		currentState=ACTIVE_STATE;
		isCollidable=true;
/*		
		setX(0);
		setY(0);
*/		
		//added by Mike, 20240806
		iStepX=ISTEP_X_DEFAULT;//*2; //faster by 1 than the default
		iStepY=ISTEP_Y_DEFAULT;//*2; //faster by 1 than the default

	}
	
	@Override
	public void hitBy(Actor a) {
		currentState=HIDDEN_STATE;
		isCollidable=false;
		
		System.out.println("HIT!!! SET TO HIDDEN STATE");
	}
	
	//added by Mike, 20240805
	@Override
	public void keyPressed(KeyEvent key) {		
	}
	
	@Override
	public void keyReleased(KeyEvent key) {		
	}
		
	@Override
	public void update() {
//		System.out.println(">>>>>>>>>>>>>>DITO");
		setX(getX()-getStepX());
		
/*		
		//removed by Mike, 20240629
		//movement
		//setX(getX()+iStepX);
	
		if (myKeysDown[KEY_A])
		{
			setX(getX()-iStepX);
			
			//added by Mike, 20240730
			currentFacingState=FACING_LEFT;			
		}

		if (myKeysDown[KEY_D])
		{
			setX(getX()+iStepX);
			
			//added by Mike, 20240730
			currentFacingState=FACING_RIGHT;	
		}

		if (myKeysDown[KEY_W])
		{
			setY(getY()-iStepY);
		}

		if (myKeysDown[KEY_S])
		{
			setY(getY()+iStepY);
		}
		
		//added by Mike, 20240730
		switch(currentFacingState) {
			case FACING_RIGHT:
				setX(getX()+iStepX);
				break;
			case FACING_LEFT:
				setX(getX()-iStepX);
				break;			
		}
		
		//added by Mike, 20240730
		if (!bHasStarted) {
			myKeysDown[KEY_D]=false;
			bHasStarted=true;
		}
*/		

		currentFacingState=FACING_LEFT;	
		//setX(getX()-getStepX());
		
		//return;		

/* 	//edited by Mike, 20240706; OK
		//animation
		if (iFrameCountDelay<iFrameCountDelayMax) {
			iFrameCountDelay++;
		}
		else {
			iFrameCount=(iFrameCount+1)%iFrameCountMax;
			iFrameCountDelay=0;
		}
*/
		iFrameCount=0;
	}	

//Additional Reference: 	https://docs.oracle.com/javase/tutorial/2d/advanced/examples/ClipImage.java; last accessed: 20240625
  @Override
  public void draw(Graphics g) {
	  
	if (currentState==HIDDEN_STATE) {
		return;
	}
	  
	//TODO: -verify: if clip still has to be cleared
	Rectangle2D rect = new Rectangle2D.Float();

	//added by Mike, 20240621
	//iFrameCount=2;

	//rect.setRect(0, 0, 128, 128);
	//rect.setRect(iFrameCount*128, 0, 128, 128);

/*
	g.setClip(myClipArea);
	//added by Mike, 20240621
	//g.drawImage(myBufferedImage, 0, 0, this);
	g.drawImage(myBufferedImage, 0-iFrameCount*128, 0, null);
*/
    //added by Mike, 20240623
    AffineTransform identity = new AffineTransform();

    Graphics2D g2d = (Graphics2D)g;
    AffineTransform trans = new AffineTransform();
    trans.setTransform(identity);
    //300 is object position;
    //trans.translate(300-iFrameCount*128, 0);
    //trans.translate(-iFrameCount*128, 0);

	//added by Mike, 20240625
	//note clip rect has to also be updated;
	//trans.scale(2,2); //1.5,1.5
	//put scale after translate position;

/*  //reference: https://stackoverflow.com/questions/8721312/java-image-cut-off; last accessed: 20240623
    //animating image doable, but shall need more computations;
    //sin, cos; Bulalakaw Wars;
    trans.translate(128/2,128/2);
    trans.rotate(Math.toRadians(45)); //input in degrees
    trans.translate(-128/2,-128/2);
*/
	//reminder: when objects moved in x-y coordinates, rotation's reference point also moves with the update;
	//"compounded translate and rotate"
	//https://stackoverflow.com/questions/32513508/rotating-java-2d-graphics-around-specified-point; last accessed: 20240625
	//answered by: MadProgrammer, 20150911T00:48; from 20150911T00:41

	//update x and y positions before rotate
	//object position x=300, y=0;
    //trans.translate(300,0);
    //edited by Mike, 20240628
	//trans.translate(300,300);
	//edited by Mike, 20240714
	trans.translate(getX(),getY());

	//scales from top-left as reference point
    //trans.scale(2,2);
	//rotates using top-left as anchor
    //trans.rotate(Math.toRadians(45)); //input in degrees

/* //removed by Mike, 20240706; OK
	//rotate at center; put translate after rotate
	iRotationDegrees=(iRotationDegrees+10)%360;
    trans.rotate(Math.toRadians(iRotationDegrees));
    trans.translate(-iFrameWidth/2,-iFrameHeight/2);
*/

	//edited by Mike, 20240714; from 20240708
/*
	System.out.println("iTileWidth: "+iTileWidth);
	System.out.println("iTileHeight: "+iTileHeight);
*/
	//scale to size of iTileWidth and iTileHeight;
	//example: iTileWidth=83
	//iFrameWidth=128;
	//trans.scale(0.5,0.5);
	//double temp = iTileWidth*1.0/iFrameWidth;
	//System.out.println("temp: "+temp);
	trans.scale((iTileWidth*1.0)/iFrameWidth,(iTileHeight*1.0)/iFrameHeight);

	//added by Mike, 20240714
	//put this after scale;
	//move the input image to the correct row of the frame
	//TODO: -add: collision detection; UsbongV2
	if (currentFacingState==FACING_RIGHT) {
		//trans.translate(getX(),getY());
	}
	else { //FACING_LEFT
		trans.translate(0,0-iFrameHeight);
	}

	//added by Mike, 20240625
	g2d.setTransform(trans);

	//note: clip rect has to move with object position
    //edited by Mike, 20240623
    //reminder:
    //300 is object position;
    //iFrameCount*128 is the animation frame to show;
    //-iFrameCount*128 is move the object position to the current frame;
    //rect.setRect(300+iFrameCount*128-iFrameCount*128, 0, 128, 128);
	//edited by Mike, 20240714; from 20240714
    //rect.setRect(0+iFrameCount*iFrameWidth-iFrameCount*iFrameWidth, 0, iFrameWidth, iFrameHeight);

	if (currentFacingState==FACING_RIGHT) {
		//no animation yet; 0+iFrameCount*iFrameWidth-iFrameCount*iFrameWidth
	    rect.setRect(0, 0, iFrameWidth, iFrameHeight);
	}
	else { //FACING_LEFT
	   rect.setRect(0, 0+iFrameHeight, iFrameWidth, iFrameHeight);
	}

	Area myClipArea = new Area(rect);

    //edited by Mike, 20240625; from 20240623
    g2d.setClip(myClipArea);
    //g.setClip(myClipArea);

    //g2d.drawImage(image, trans, this);
    //g2d.drawImage(myBufferedImage, trans, null);
    //g2d.drawImage(myBufferedImage,300-iFrameCount*128, 0, null);

	//edited by Mike, 20240714
    g2d.drawImage(myBufferedImage,-iFrameCount*iFrameWidth, 0, null);

	//removed by Mike, 20240711; from 20240625
	//put after the last object to be drawn
	//g2d.dispose();
  }
}

//added by Mike, 20240711
class BackgroundCanvas extends Actor {
	
	//added by Mike, 20240727
	//private int[][] tileMap;
	private final int MAX_TILE_MAP_HEIGHT=13;
	//TODO: -add: draw two tiles beyond the max; reduce pop-out
	private final int MAX_TILE_MAP_WIDTH=26; 
	private final int TILE_BLANK=0;
	private final int TILE_TREE=1;
		
    public BackgroundCanvas(int iOffsetScreenWidthLeftMargin, int iOffsetScreenHeightTopMargin, int iStageWidth, int iStageHeight, int iTileWidth, int iTileHeight) {
	
	super(iOffsetScreenWidthLeftMargin, iOffsetScreenHeightTopMargin, iStageWidth, iStageHeight, iTileWidth, iTileHeight);
		
	//added by Mike, 20240727
	tileMap = new int[MAX_TILE_MAP_HEIGHT][MAX_TILE_MAP_WIDTH];
/*	  
	  for (int i=0; i<MAX_TILE_MAP_HEIGHT; i++) {
		for (int k=0; k<MAX_TILE_MAP_WIDTH; k++) {
			tileMap[i][k]=TILE_BLANK;
		}
	  }
*/
	  try {
		  //edited by Mike, 20240706
		  //myBufferedImage = ImageIO.read(new File("../res/count.png"));
		  //edited by Mike, 20240720
		  //myBufferedImage = ImageIO.read(new File("../res/background.png"));
		  //app executed from base directory
		  myBufferedImage = ImageIO.read(new File("./res/background.png"));
      } catch (IOException ex) {
      }

	  //added by Mike, 20240708
	  this.iOffsetScreenWidthLeftMargin=iOffsetScreenWidthLeftMargin;
	  this.iOffsetScreenHeightTopMargin=iOffsetScreenHeightTopMargin;

	  //added by Mike, 20240628
	  this.iStageWidth=iStageWidth;
	  this.iStageHeight=iStageHeight;

	  //added by Mike, 20240714
	  this.iTileWidth=iTileWidth;
	  this.iTileHeight=iTileHeight;

	  //added by Mike, 20240629
	  myKeysDown = new boolean[iNumOfKeyTypes];

	  reset();
	}

	//added by Mike, 20240628
	@Override
	public void reset() {
		iWidth=iFrameWidth;
		iHeight=iFrameHeight;

		//setX(0);
/*
		setX(iOffsetScreenWidthLeftMargin+0+iScreenWidth/2);
		setY(iOffsetScreenHeight+0+iScreenHeight/2);
*/
		setX(iOffsetScreenWidthLeftMargin+0);
		setY(iOffsetScreenHeightTopMargin+0);

		iFrameCount=0;
		iFrameCountDelay=0;
		iRotationDegrees=0;

		//added by Mike, 20240629
		int iMyKeysDownLength = myKeysDown.length;
		for (int i=0; i<iMyKeysDownLength; i++) {
			myKeysDown[i]=false;
		}
		//added by Mike, 20240730
		myKeysDown[KEY_D]=true;
		
		
		//added by Mike, 20240727
	    for (int i=0; i<MAX_TILE_MAP_HEIGHT; i++) {
		  for (int k=0; k<MAX_TILE_MAP_WIDTH; k++) {
			tileMap[i][k]=TILE_BLANK;
		  }
	    }	
		
		//start values in default view port position;
		tileMap[0][0]=TILE_TREE;		
		tileMap[1][1]=TILE_TREE;	
		tileMap[2][2]=TILE_TREE;	

		//added by Mike, 20240729
		//tileMap[1][10]=TILE_TREE;	

		tileMap[MAX_TILE_MAP_HEIGHT-1][0]=TILE_TREE;		
		tileMap[MAX_TILE_MAP_HEIGHT-1][13-1]=TILE_TREE;		
		tileMap[0][13-1]=TILE_TREE;		
		
		//added by Mike, 20240729
		iViewPortX=0;
		iViewPortY=0;
		iViewPortWidth=iStageWidth;
		iViewPortHeight=iStageHeight;	
		
		//added by Mike, 20240806
		iStepX=ISTEP_X_DEFAULT*2; //faster by 1 than the default
		iStepY=ISTEP_Y_DEFAULT*2; //faster by 1 than the default
		
/*
		System.out.println("iViewPortWidth: "+iViewPortWidth);
		System.out.println("iTileWidth*13: "+iTileWidth*13);
*/	
	}

	@Override
	public void update() {
		//removed by Mike, 20240629
		//movement
		//setX(getX()+iStepX);
		
		//TODO: -add: auto-movement forward; robotship, viewport
/*	
		if (myKeysDown[KEY_A])
		{
			//setX(getX()-iStepX);
			
			//added by Mike, 20240730
			currentFacingState=FACING_LEFT;			
		}

		if (myKeysDown[KEY_D])
		{
			//setX(getX()+iStepX);
			
			//added by Mike, 20240730
			currentFacingState=FACING_RIGHT;	
		}

		if (myKeysDown[KEY_W])
		{
			setY(getY()-iStepY);
		}

		if (myKeysDown[KEY_S])
		{
			setY(getY()+iStepY);
		}
		
		//TODO: -add: set to true myKeysDown[KEY_D], myKeysDown[KEY_A] 
		//added by Mike, 20240730
		switch(currentFacingState) {
			case FACING_RIGHT:
				setX(getX()+iStepX);
				break;
			case FACING_LEFT:
				setX(getX()-iStepX);
				break;			
		}
*/
		
		if (myKeysDown[KEY_A])
		{
			setX(getX()-iStepX);
		}

		if (myKeysDown[KEY_D])
		{
			setX(getX()+iStepX);
		}

		if (myKeysDown[KEY_W])
		{
			setY(getY()-iStepY);
		}

		if (myKeysDown[KEY_S])
		{
			setY(getY()+iStepY);
		}
		
		
		//added by Mike, 20240730
		if (!bHasStarted) {
			myKeysDown[KEY_D]=false;
			bHasStarted=true;
		}

		
		//return;		

/* 	//edited by Mike, 20240706; OK
		//animation
		if (iFrameCountDelay<iFrameCountDelayMax) {
			iFrameCountDelay++;
		}
		else {
			iFrameCount=(iFrameCount+1)%iFrameCountMax;
			iFrameCountDelay=0;
		}
*/
		iFrameCount=0;
	}
/*
	//added by Mike, 20240629
	public void move(KeyEvent key) {
		//horizontal movement
		if ((key.getKeyCode() == KeyEvent.VK_A) || (key.getKeyCode() == KeyEvent.VK_LEFT)) {
			setX(getX()-iStepX);
		}

		if ((key.getKeyCode() == KeyEvent.VK_D) || (key.getKeyCode() == KeyEvent.VK_RIGHT)) {
			setX(getX()+iStepX);
		}

		//vertical movement
		if ((key.getKeyCode() == KeyEvent.VK_W) || (key.getKeyCode() == KeyEvent.VK_UP)) {
			setY(getY()-iStepY);
		}

		if ((key.getKeyCode() == KeyEvent.VK_S) || (key.getKeyCode() == KeyEvent.VK_DOWN)) {
			setY(getY()+iStepY);
		}
	}
*/

	
	//edited by Mike, 20240806; from 20240729
	private boolean isTileInsideViewport(int iViewPortX, int iViewPortY, int iCurrTileX, int iCurrTileY)
{     
	//System.out.println(">>>iViewPortWidth: "+iViewPortWidth);
	
	//TODO: -verify: why we need to multiply by 2 the viewport width and height; 
	//iViewPortWidth*2; iViewPortHeight*2

		//add 1 row before making the tile disappear	
		if (iCurrTileY+iTileHeight*2 < iViewPortY || //is above the top of iViewPortY?
			iCurrTileY > iViewPortY+iViewPortHeight*2 || //is at the bottom of iViewPortY?
			//add 1 column before making the tile disappear
			iCurrTileX+iTileWidth*2 < iViewPortX || //is at the left of iViewPortX?
			iCurrTileX > iViewPortX+iViewPortWidth*2) { //is at the right of iViewPortX?
			return false;
		}
		
/*
//		System.out.println(">>>iViewPortY: "+iViewPortY);
		System.out.println(">>>iViewPortX: "+iViewPortX);

//		System.out.println(">>>iViewPortHeight: "+iViewPortHeight);
		System.out.println(">>>iViewPortWidth: "+iViewPortWidth);

		System.out.println(">>>>>>iCurrTileX: "+iCurrTileX);
		System.out.println(">>>>>>iTileWidth: "+iTileWidth);
*/
/*
		//add 1 row before making the tile disappear	
		if (iCurrTileY+iTileHeight*2 < iViewPortY || //is above the top of iViewPortY?
			iCurrTileY > iViewPortY+(iViewPortY+iViewPortHeight) || //is at the bottom of iViewPortY?
			//add 1 column before making the tile disappear
			iCurrTileX+iTileWidth*2 < iViewPortX || //is at the left of iViewPortX?
			iCurrTileX > iViewPortX+(iViewPortX+iViewPortWidth)) { //is at the right of iViewPortX?
			return false;
		}
*/	
		return true;
	}

	//added by Mike, 20240726
	public void drawTree(Graphics g, int iInputTileWidthCount, int iInputTileHeightCount) {
	//TODO: -verify: if clip still has to be cleared
		Rectangle2D rect = new Rectangle2D.Float();

		//added by Mike, 20240621
		//iFrameCount=2;

		//rect.setRect(0, 0, 128, 128);
		//rect.setRect(iFrameCount*128, 0, 128, 128);

	/*
		g.setClip(myClipArea);
		//added by Mike, 20240621
		//g.drawImage(myBufferedImage, 0, 0, this);
		g.drawImage(myBufferedImage, 0-iFrameCount*128, 0, null);
	*/
		//added by Mike, 20240623
		AffineTransform identity = new AffineTransform();

		Graphics2D g2d = (Graphics2D)g;
		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		//300 is object position;
		//trans.translate(300-iFrameCount*128, 0);
		//trans.translate(-iFrameCount*128, 0);

		//added by Mike, 20240625
		//note clip rect has to also be updated;
		//trans.scale(2,2); //1.5,1.5
		//put scale after translate position;

	/*  //reference: https://stackoverflow.com/questions/8721312/java-image-cut-off; last accessed: 20240623
		//animating image doable, but shall need more computations;
		//sin, cos; Bulalakaw Wars;
		trans.translate(128/2,128/2);
		trans.rotate(Math.toRadians(45)); //input in degrees
		trans.translate(-128/2,-128/2);
	*/
		//reminder: when objects moved in x-y coordinates, rotation's reference point also moves with the update;
		//"compounded translate and rotate"
		//https://stackoverflow.com/questions/32513508/rotating-java-2d-graphics-around-specified-point; last accessed: 20240625
		//answered by: MadProgrammer, 20150911T00:48; from 20150911T00:41

		//update x and y positions before rotate
		//object position x=300, y=0;
		//trans.translate(300,0);
		//edited by Mike, 20240628
		//trans.translate(300,300);
/* 		//edited by Mike, 20240729; from 20240726
		setX(iOffsetScreenWidthLeftMargin+0+iInputTileWidthCount);
		setY(iOffsetScreenHeightTopMargin+0+iInputTileWidthCount);

		trans.translate(getX(),getY());
*/		
		trans.translate(iInputTileWidthCount,iInputTileHeightCount);

		//scales from top-left as reference point
		//trans.scale(2,2);
		//rotates using top-left as anchor
		//trans.rotate(Math.toRadians(45)); //input in degrees

	/* //removed by Mike, 20240706; OK
		//rotate at center; put translate after rotate
		iRotationDegrees=(iRotationDegrees+10)%360;
		trans.rotate(Math.toRadians(iRotationDegrees));
		trans.translate(-iFrameWidth/2,-iFrameHeight/2);
	*/

		//edited by Mike, 20240714; from 20240708
		//scale to size of iTileWidth and iTileHeight;
		//example: iTileWidth=83
		//iFrameWidth=128;
		//trans.scale(0.5,0.5);
		//double temp = iTileWidth*1.0/iFrameWidth;
		//System.out.println("temp: "+temp);
		trans.scale((iTileWidth*1.0)/iFrameWidth,(iTileHeight*1.0)/iFrameHeight);

		//added by Mike, 20240625
		g2d.setTransform(trans);

		//note: clip rect has to move with object position
		//edited by Mike, 20240623
		//reminder:
		//300 is object position;
		//iFrameCount*128 is the animation frame to show;
		//-iFrameCount*128 is move the object position to the current frame;
		//rect.setRect(300+iFrameCount*128-iFrameCount*128, 0, 128, 128);
		rect.setRect(0+iFrameCount*iFrameWidth-iFrameCount*iFrameWidth, 0, iFrameWidth, iFrameHeight);

		Area myClipArea = new Area(rect);

		//edited by Mike, 20240625; from 20240623
		g2d.setClip(myClipArea);
		//g.setClip(myClipArea);

		//g2d.drawImage(image, trans, this);
		//g2d.drawImage(myBufferedImage, trans, null);
		//g2d.drawImage(myBufferedImage,300-iFrameCount*128, 0, null);
		g2d.drawImage(myBufferedImage,-iFrameCount*iFrameWidth, 0, null);

		//removed by Mike, 20240711; from 20240625
		//put after the last object to be drawn
		//g2d.dispose();					
	}

//Additional Reference: 	https://docs.oracle.com/javase/tutorial/2d/advanced/examples/ClipImage.java; last accessed: 20240625
  @Override
  public void draw(Graphics g) {
	//edited by Mike, 20240727; from 20240726
/*	
	drawTree(g, 0, 0);

	drawTree(g, iTileWidth, iTileHeight);
	
	drawTree(g, iTileWidth*2, iTileHeight*2);	
*/

/*	//edited by Mike, 20240729
	for (int i=0; i<MAX_TILE_MAP_HEIGHT; i++) {
	  for (int k=0; k<MAX_TILE_MAP_WIDTH; k++) {
		if (tileMap[i][k]==TILE_TREE) {
			drawTree(g, iTileWidth*k, iTileHeight*i);	
		}
	  }
	}
*/
/*
	viewPortWidth=iStageWidth;
	viewPortHeight=iStageHeight;
*/	
	//identify the current tile in horizontal axis
	iViewPortX=getX();
	iViewPortY=getY();
/*	
	System.out.println("iViewPortX: "+iViewPortX);
	System.out.println("iViewPortY: "+iViewPortY);
*/
	for (int i=0; i<MAX_TILE_MAP_HEIGHT; i++) {
	  for (int k=0; k<MAX_TILE_MAP_WIDTH; k++) {
	
			int iDifferenceInXPos=iViewPortX-(iOffsetScreenWidthLeftMargin+iTileWidth*k);

			int iDifferenceInYPos=iViewPortY-(iOffsetScreenHeightTopMargin+iTileHeight*i);
	
			//if (isTileInsideViewport(iViewPortX,iViewPortY, iOffsetScreenWidthLeftMargin+iTileWidth*k,iOffsetScreenHeightTopMargin+iTileHeight*i)) {
			
			//if (isTileInsideViewport(iViewPortX,iViewPortY, iOffsetScreenWidthLeftMargin+iTileWidth*k-iDifferenceInXPos,iOffsetScreenHeightTopMargin+iTileHeight*i-iDifferenceInYPos)) {

			if (isTileInsideViewport(iViewPortX,iViewPortY, iOffsetScreenWidthLeftMargin+iTileWidth*k-iDifferenceInXPos,iOffsetScreenHeightTopMargin+iTileHeight*i-iDifferenceInYPos)) {				
	
//System.out.println("iDifferenceInXPos: "+iDifferenceInXPos);
				
//System.out.println("HALLO!");

				if (tileMap[i][k]==TILE_TREE) {		
					//drawTree(g, iOffsetScreenWidthLeftMargin+iTileWidth*k, iOffsetScreenHeightTopMargin+iTileHeight*i);	
					
					//drawTree(g, iViewPortX+iTileWidth*k, iViewPortY+iTileHeight*i);
					
					//drawTree(g, iOffsetScreenWidthLeftMargin+iTileWidth*k-iDifferenceInXPos, iOffsetScreenHeightTopMargin+iTileHeight*i-iDifferenceInYPos);	
					
					drawTree(g, iOffsetScreenWidthLeftMargin-iDifferenceInXPos, iOffsetScreenHeightTopMargin-iDifferenceInYPos);	
					
				}
			}	  
	  }
	}
/*	
	//removed by Mike, 20240805; from 20240730
	//put in Level2D
	drawMargins(g);	
*/	
  }
}

//added by Mike, 20240804; from 20240803
//for Actor object positions
class Level2D extends Actor {	
	//added by Mike, 20240727
	//protected int[][] tileMap; 
	
	private final int MAX_TILE_MAP_HEIGHT=13;
	//TODO: -add: draw two tiles beyond the max; reduce pop-out
	private final int MAX_TILE_MAP_WIDTH=26; 
	private final int TILE_BLANK=0;
	private final int TILE_AIRCRAFT=1;
	private final int TILE_SHIP=2;
	
	//added by Mike, 20240729
	private int iViewPortX;
	private int iViewPortY;
	private int iViewPortWidth;
	private int iViewPortHeight;
	
	//added by Mike, 20240804
	//private EnemyAircraft myEnemyAircraft;
	private final int MAX_ENEMY_AIRCRAFT_COUNT=2; //5;
	private EnemyAircraft[] myEnemyAircraftContainer;
		
    public Level2D(int iOffsetScreenWidthLeftMargin, int iOffsetScreenHeightTopMargin, int iStageWidth, int iStageHeight, int iTileWidth, int iTileHeight) {
	  super(iOffsetScreenWidthLeftMargin, iOffsetScreenHeightTopMargin, iStageWidth, iStageHeight, iTileWidth, iTileHeight);
		
	  //added by Mike, 20240727
	  //tileMap = new int[MAX_TILE_MAP_HEIGHT][MAX_TILE_MAP_WIDTH];
	  
/*	  
	  for (int i=0; i<MAX_TILE_MAP_HEIGHT; i++) {
		for (int k=0; k<MAX_TILE_MAP_WIDTH; k++) {
			tileMap[i][k]=TILE_BLANK;
		}
	  }
*/
/*
	  try {
		  //edited by Mike, 20240706
		  //myBufferedImage = ImageIO.read(new File("../res/count.png"));
		  //edited by Mike, 20240720
		  //myBufferedImage = ImageIO.read(new File("../res/background.png"));
		  //app executed from base directory
		  myBufferedImage = ImageIO.read(new File("./res/background.png"));
      } catch (IOException ex) {
      }
*/
	  //added by Mike, 20240708
	  this.iOffsetScreenWidthLeftMargin=iOffsetScreenWidthLeftMargin;
	  this.iOffsetScreenHeightTopMargin=iOffsetScreenHeightTopMargin;

	  //added by Mike, 20240628
	  this.iStageWidth=iStageWidth;
	  this.iStageHeight=iStageHeight;

	  //added by Mike, 20240714
	  this.iTileWidth=iTileWidth;
	  this.iTileHeight=iTileHeight;

	  //added by Mike, 20240629
	  myKeysDown = new boolean[iNumOfKeyTypes];
		
	  //added by Mike, 20240804
	  //myEnemyAircraft = new EnemyAircraft(iOffsetScreenWidthLeftMargin,iOffsetScreenHeightTopMargin,iStageWidth, iStageHeight, iTileWidth, iTileHeight);

	  myEnemyAircraftContainer = new EnemyAircraft[MAX_ENEMY_AIRCRAFT_COUNT];
	  
	  for (int i=0; i<MAX_ENEMY_AIRCRAFT_COUNT; i++) {
		  myEnemyAircraftContainer[i] = new EnemyAircraft(iOffsetScreenWidthLeftMargin,iOffsetScreenHeightTopMargin,iStageWidth, iStageHeight, iTileWidth, iTileHeight);
	  }

	  reset();
	  		
	  //added by Mike, 20240804
	  initLevel();
	}

	//added by Mike, 20240628
	@Override
	public void reset() {
		iWidth=iFrameWidth;
		iHeight=iFrameHeight;
		
		//put tileMap in Actor class; otherwise null pointer exception here
		//tileMap[0][0]=TILE_BLANK;

		//setX(0);
/*
		setX(iOffsetScreenWidthLeftMargin+0+iScreenWidth/2);
		setY(iOffsetScreenHeight+0+iScreenHeight/2);
*/
		setX(iOffsetScreenWidthLeftMargin+0);
		setY(iOffsetScreenHeightTopMargin+0);

		iFrameCount=0;
		iFrameCountDelay=0;
		iRotationDegrees=0;

		//added by Mike, 20240629
		int iMyKeysDownLength = myKeysDown.length;
		for (int i=0; i<iMyKeysDownLength; i++) {
			myKeysDown[i]=false;
		}
		//edited by Mike, 20240805; from 20240730
		//myKeysDown[KEY_D]=true;
				
		//added by Mike, 20240727
	    for (int i=0; i<MAX_TILE_MAP_HEIGHT; i++) {
		  for (int k=0; k<MAX_TILE_MAP_WIDTH; k++) {
			tileMap[i][k]=TILE_BLANK;
		  }
	    }	
	
		//start values in default view port position;
		tileMap[1][6]=TILE_AIRCRAFT;	
		//tileMap[1][7]=TILE_AIRCRAFT;	
		//tileMap[1][MAX_TILE_MAP_WIDTH-10]=TILE_AIRCRAFT;	

		//tileMap[1][11]=TILE_AIRCRAFT;	
		tileMap[1][14]=TILE_AIRCRAFT;	

		
/*		
		tileMap[1][7]=TILE_AIRCRAFT;	
		tileMap[1][8]=TILE_AIRCRAFT;		
*/
		
		//added by Mike, 20240729
		iViewPortX=0;
		iViewPortY=0;
		iViewPortWidth=iStageWidth;
		iViewPortHeight=iStageHeight;	
		
		//added by Mike, 20240806
		iStepX=ISTEP_X_DEFAULT*2; //faster by 1 than the default
		iStepY=ISTEP_Y_DEFAULT*2; //faster by 1 than the default

	}
	
	//added by Mike, 20240805
/*	
	@Override
	public void keyPressed(KeyEvent key) {		
	}
	
	@Override
	public void keyReleased(KeyEvent key) {		
	}
*/
	@Override
	public void update() {		

/*		
		//added by Mike, 20240730
		if (!bHasStarted) {
			myKeysDown[KEY_D]=false;
			bHasStarted=true;
		}
*/		
/*	
		//note: AI; not yet liberated from user inputs?
		for (int i=0; i<MAX_ENEMY_AIRCRAFT_COUNT; i++) {
		    myEnemyAircraftContainer[i].setY(myEnemyAircraftContainer[i].getY()+myEnemyAircraftContainer[i].getStepY());

			switch(currentFacingState) {
				case FACING_RIGHT:
					myEnemyAircraftContainer[i].setX(myEnemyAircraftContainer[i].getX()-myEnemyAircraftContainer[i].getStepX());
					break;
				case FACING_LEFT:
					myEnemyAircraftContainer[i].setX(myEnemyAircraftContainer[i].getX()+myEnemyAircraftContainer[i].getStepX());
					break;			
			}
		}
*/				
		//added by Mike, 20240806
		//identify the current tile in horizontal axis
		iViewPortX=getX();
		iViewPortY=getY();
	
		//note: AI; not yet liberated from user inputs?
		//TODO: -update: movement if in viewport
		for (int i=0; i<MAX_ENEMY_AIRCRAFT_COUNT; i++) {
		    
			//added by Mike, 20240806
			//TODO: -reverify: this; viewport moves? or fixed at 0,0?
			
			if (isActorInsideViewport(iViewPortX, iViewPortY, myEnemyAircraftContainer[i])) {		
				myEnemyAircraftContainer[i].update();			
			}
		//myEnemyAircraftContainer[i].setX(myEnemyAircraftContainer[i].getX()-myEnemyAircraftContainer[i].getStepX());
					//myEnemyAircraftContainer[i].setCurrentFacingState(FACING_LEFT);
				
				//move the object in relation to Level2D
				//edited by Mike, 20240806
				if (myKeysDown[KEY_A])
				{
					//currentFacingState=FACING_LEFT;			
					//myEnemyAircraftContainer[i].setX(myEnemyAircraftContainer[i].getX()+myEnemyAircraftContainer[i].getStepX());
					myEnemyAircraftContainer[i].setX(myEnemyAircraftContainer[i].getX()+this.getStepX());	

					//TODO: -reverify: this
					//this.setX(this.getX()+this.getStepX());
				}

				if (myKeysDown[KEY_D])
				{
				//currentFacingState=FACING_RIGHT;					
					//myEnemyAircraftContainer[i].setX(myEnemyAircraftContainer[i].getX()-myEnemyAircraftContainer[i].getStepX());
					myEnemyAircraftContainer[i].setX(myEnemyAircraftContainer[i].getX()-this.getStepX());
					
					//TODO: -reverify: this
					//this.setX(this.getX()-this.getStepX());
				}

				//level continuously scrolls, along with the background
				if (myKeysDown[KEY_W])
				{
					//currentFacingState=FACING_UP;				
					//myEnemyAircraftContainer[i].setY(myEnemyAircraftContainer[i].getY()+myEnemyAircraftContainer[i].getStepY());
					myEnemyAircraftContainer[i].setY(myEnemyAircraftContainer[i].getY()+this.getStepY());
					
					//TODO: -reverify: this
					//this.setY(this.getY()+this.getStepY());
				}

				if (myKeysDown[KEY_S])
				{					  
//currentFacingState=FACING_DOWN;							
					//myEnemyAircraftContainer[i].setY(myEnemyAircraftContainer[i].getY()-myEnemyAircraftContainer[i].getStepY());
					myEnemyAircraftContainer[i].setY(myEnemyAircraftContainer[i].getY()-this.getStepY());					

					//TODO: -reverify: this
					//this.setY(this.getY()-this.getStepY());
				}	
		}
			
			
/*		
		//added by Mike, 20240730
		if (!bHasStarted) {
			myKeysDown[KEY_D]=false;
			bHasStarted=true;
		}
		
		//return;		
*/		

/* 	//edited by Mike, 20240706; OK
		//animation
		if (iFrameCountDelay<iFrameCountDelayMax) {
			iFrameCountDelay++;
		}
		else {
			iFrameCount=(iFrameCount+1)%iFrameCountMax;
			iFrameCountDelay=0;
		}
*/
		iFrameCount=0;
	}
	
	//added by Mike, 20240804
	@Override
	public void collideWith(Actor a) {
/*		
		//if (isIntersectingRect(this, a))
		if (isIntersectingRect(myEnemyAircraft, a))		
		{
			//System.out.println("COLLISION!");
			
			myEnemyAircraft.hitBy(a);
			a.hitBy(myEnemyAircraft);
		}
*/
		for (int i=0; i<MAX_ENEMY_AIRCRAFT_COUNT; i++) {
			
			if (!myEnemyAircraftContainer[i].isActive()) {
				//edited by Mike, 20240805
				//return;
				continue;
			}
			
/*			
			if ((!myEnemyAircraftContainer[i].checkIsCollidable())||(!a.checkIsCollidable()))    
			{
				//not collidable
				return;
			}
*/		
			if (isIntersectingRect(myEnemyAircraftContainer[i], a))		
			{
				myEnemyAircraftContainer[i].hitBy(a);
				a.hitBy(myEnemyAircraftContainer[i]);
				
				System.out.println("iEnemyAircraft i: "+i);
				System.out.println(">>>>>>>>>>>>>>>>>");
			}
		}
	}
		
	//added by Mike, 20240806
	private boolean isActorInsideViewport(int iViewPortX, int iViewPortY, Actor a)
{     
/*
		System.out.println(">>>iViewPortX: "+iViewPortX);
		System.out.println(">>>iViewPortY: "+iViewPortY);

		System.out.println(">>>iViewPortHeight: "+iViewPortHeight);
		System.out.println(">>>iViewPortWidth: "+iViewPortWidth);

		System.out.println(">>>>>>a.getX(): "+a.getX());
		System.out.println(">>>>>>a.getWidth(): "+a.getWidth());
*/
			
		//add 1 row before making the tile disappear	
		if (a.getY()+a.getHeight() < iViewPortY || //is above the top of iViewPortY?
			a.getY() > iViewPortY+iViewPortHeight || //is at the bottom of iViewPortY?
			//add 1 column before making the tile disappear
			a.getX()+a.getWidth() < iViewPortX || //is at the left of iViewPortX?
			a.getX() > iViewPortX+iViewPortWidth) { //is at the right of iViewPortX?
			return false;
		}
	
		return true;
	}	
	
	//pre-set enemy positions, but do not draw them all yet;
	public void initLevel() {
		int iEnemyAircraftCount=0;
		
		for (int i=0; i<MAX_TILE_MAP_HEIGHT; i++) {
			for (int k=0; k<MAX_TILE_MAP_WIDTH; k++) {
				if (tileMap[i][k]==TILE_AIRCRAFT) {		

					System.out.println("TILE_AIRCRAFT!");

					//for (int iEnemyAircraftCount=0; iEnemyAircraftCount<MAX_ENEMY_AIRCRAFT_COUNT; iEnemyAircraftCount++) {
					if (iEnemyAircraftCount<MAX_ENEMY_AIRCRAFT_COUNT) {
						//System.out.println("isActive?"+myEnemyAircraftContainer[iEnemyAircraftCount].isActive());
					
						if (myEnemyAircraftContainer[iEnemyAircraftCount].isActive()) {
							//System.out.println("iEnemyAircraftCount: "+iEnemyAircraftCount);
							
							myEnemyAircraftContainer[iEnemyAircraftCount].setX(0+iOffsetScreenWidthLeftMargin+iTileWidth*k);			
							myEnemyAircraftContainer[iEnemyAircraftCount].setY(0+iOffsetScreenHeightTopMargin+iTileHeight*i);
													
							tileMap[i][k]=TILE_BLANK;
							
							iEnemyAircraftCount++;		
							
							//continue; //stop once an available aircraft is found in the container
						}
					}					
				}
			}
	  }
	}
	
	//added by Mike, 20240805; from 20240731
	public void drawMargins(Graphics g) {
		Rectangle2D rect = new Rectangle2D.Float();

		//added by Mike, 20240623
		AffineTransform identity = new AffineTransform();

		Graphics2D g2d = (Graphics2D)g;
		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		g2d.setTransform(trans);
		
		//added by Mike, 20240731
		//https://stackoverflow.com/questions/1241253/inside-clipping-with-java-graphics; last accessed: 20240731
		//answer by: Savvas Dalkitsis, 20090806T2154

		//g2d.setClip(new Area(new Rectangle2D.Double(0, 0, iOffsetScreenWidthLeftMargin, iOffsetScreenHeightTopMargin+iStageHeight)));
		g2d.setClip(new Area(new Rectangle2D.Double(0, 0, iOffsetScreenWidthLeftMargin*2+iStageWidth, iOffsetScreenHeightTopMargin+iStageHeight)));

		//edited by Mike, 20240731; from 20240730
		//paint the margins;
		g.setColor(Color.decode("#adb2b6")); //gray; 
		
		//cover the left margin
		g.fillRect(0,0,iOffsetScreenWidthLeftMargin,iStageHeight);

		//cover the right margin
		g.fillRect(0+iOffsetScreenWidthLeftMargin+iStageWidth,0,0+iOffsetScreenWidthLeftMargin+iStageWidth+iOffsetScreenWidthLeftMargin,iStageHeight);

		//System.out.println(">>>");
		
		//removed by Mike, 20240711; from 20240625
		//put after the last object to be drawn
		//g2d.dispose();		
	}	

  @Override
  public void draw(Graphics g) {
	//added by Mike, 20240806
	//draw only if in viewport	
	iViewPortX=getX();
	iViewPortY=getY();
	
	for (int iEnemyAircraftCount=0; iEnemyAircraftCount<MAX_ENEMY_AIRCRAFT_COUNT; iEnemyAircraftCount++) {	
				
		if (myEnemyAircraftContainer[iEnemyAircraftCount].isActive()) {
			//added by Mike, 20240806
			if (isActorInsideViewport(iViewPortX, iViewPortY, myEnemyAircraftContainer[iEnemyAircraftCount])) {
				myEnemyAircraftContainer[iEnemyAircraftCount].draw(g);
			}				
		}
	}

	//added by Mike, 20240805
	drawMargins(g);
  }
}  
  
