/*
    Description: A simple snake game for j2me platform
	Author: mmj-the-fighter 
    Copyright (C) 2010 mmj-the-fighter

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class SnakeCanvas extends Canvas  implements Runnable, CommandListener
{
    Thread gameThread = null;
    private boolean isPlay;   // Game Loop runs when isPlay is true
    private boolean running=false;
    private long delay;       // To give thread consistency
    private int width;
    private int height;
    private int curKeycode;
    private int curGameAction;
    private int lastKeycode;
    private int lastGameAction;
    private int score;
    private int[] scoreDigit = new int[16];
    private int digitStart=0;
    private int fpsCounter=0;
    private long fpsStartTime=0;
    private long fpsEndTime=0;
    private int calculatedFPS=0;
    
    private Command startCommand = new Command("Start", Command.OK, 1);
    private Command stopCommand  = new Command("Stop", Command.EXIT, 1);
    private Command pauseCommand  = new Command("Pause", Command.EXIT, 1);

    private boolean gameOver = false;

    //Game specific
    public int  fruit_xpos, fruit_ypos, fruit_w=3, fruit_h=4;
    
    private SegmentList segmentList;
    private Random randVar;
    private Segment headSegment;
    private Segment fruit;
    private GameMIDlet midlet;
    private Image digitImage;
    private Font font;
    private int fontHeight;
    private int fgColor = 0xffffffff;
    private int bgColor = 0xff44ee22;
    private String digitString = "0123456789";
    private String yourScoreString = "Score:";
    private String gameOverString= "Game Over!";
    // Constructor and initialization
    
    private long frameTime = 1000/31;
    private boolean debug=false;
     public boolean firstInstnce=true;
	 public static long lastGCRequetTime=System.currentTimeMillis();
     
    public SnakeCanvas(GameMIDlet m){
        midlet =m;
        width = getWidth();
        height = getHeight();
        delay = 30;
        randVar = new Random(System.currentTimeMillis());

        addCommand(startCommand);
        addCommand(stopCommand);
        setCommandListener(this);
        font=Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_BOLD,Font.SIZE_LARGE);
        fontHeight = font.getHeight();
        
    }

    public void initSnake()
    {
        //init snake with 5 blocks -
        segmentList = new SegmentList();
        headSegment = segmentList.create(30, 64, 6, 5);
        fruit = new Segment(0,0,1);
        fruit_w = fruit_h = 5;
        fruit.setSize(fruit_w, fruit_h);
        Segment.setBounds(width, height);
        relocateFruit();
        gameOver= false;
        score =0;
        scoreDigit[0] =0;
        digitStart = 0;
        //updateScore(0);
    }
    


  // Start thread for game loop
  public void start() 
  {
      isPlay = true;
      gameThread = new Thread(this);
      gameThread.start();
      initSnake();
      repaint();
      serviceRepaints();
      fpsStartTime = System.currentTimeMillis();
  }

  // Stop thread for game loop
  public void stop() {
      isPlay=false;
      gameThread = null;
  }

  protected void hideNotify(){
  //when something causes to pause the game
     stopRunning(); 
     
  }
  
  public void stopRunning(){
      if(running==true){
         running = false;
         removeCommand(pauseCommand);
         addCommand(startCommand);
      }
  }
  
  

  protected void showNotify(){
      
      
  }

  protected void keyPressed(int keyCode){
    //store the processed game key to buffer
      curKeycode = keyCode;
      curGameAction  = getGameAction(keyCode);
  }
  protected void keyRepeated(int keyCode){
    //store the processed game key to buffer
      curKeycode = keyCode;
      curGameAction  = getGameAction(keyCode);
  }
  protected void keyReleased(int keyCode){
      curGameAction = -2222;
  }

  private void changeDirection(){
           switch(curGameAction)
     {
        case UP:
            if(lastGameAction==DOWN){
                break;
            }else{
                lastGameAction = curGameAction;
                headSegment.setDir(curGameAction);
                
            }break;
        case DOWN:
            if(lastGameAction==UP){
                break;
            }else{
                lastGameAction = curGameAction;
                headSegment.setDir(curGameAction);
            }break;
        case LEFT:
            if(lastGameAction==RIGHT){
                break;
            }else{
                lastGameAction = curGameAction;
                headSegment.setDir(curGameAction);
            }break;
        case RIGHT:
            if(lastGameAction==LEFT){
                break;
            }else{
                lastGameAction = curGameAction;
                headSegment.setDir(curGameAction);
            }break;
         default:
             break;
            
    }
  }
 public void tick() 
 {

     if(isInsideArena(headSegment.xpos, headSegment.ypos)){
         changeDirection();
     }

     
     if(headSegment.isCollision(fruit)){
         updateScore(1);
         relocateFruit();
         growSnake();
     }

     gameOver = segmentList.move();
     

 }

 public boolean isInsideArena(int x, int y){
      //g.setClip(6,6,width-11,height-11);
     if( (x<6||x>width-11) || (y<6 || y > height-11) )
         return false;
     else
         return true;
         
 }
 
  
 protected void paint(Graphics g)
 {
     int anchor = Graphics.TOP|Graphics.LEFT;
     g.setColor(0x223366);
	 //g.setColor(0xffffff);
     g.fillRect(0, 0, width, height);
     g.setColor(0x0000ff);
     g.drawRect(5, 5, width-10, height-10);
     g.setClip(6,6,width-11,height-11);
          
     //paintSegment segments
     segmentList.render(g);
     fruit.paintFruit(g);
     
     displayScore(g,10,10);
     //paintSegment game over
     if(gameOver){
         //g.setColor(0xffaa2211);
         //g.fillRect(5, 5, width, height);
         g.setColor(0xffffffff);
         int w = font.stringWidth(gameOverString); 
         int xp =(width - w)/2;
         int yp = (height - fontHeight)/2;
		 g.setFont(font);
         g.drawString(gameOverString,xp,yp,anchor);
         yp = yp + fontHeight+1;
         g.drawString(yourScoreString,xp,yp,anchor);
         w = font.stringWidth(yourScoreString); 
         displayScore(g,xp+w+1,yp);
     }
     if(debug){
        String s = Integer.toString(calculatedFPS);
        g.drawString(s, 100, 100, anchor);
        s=null;
     }
	 g=null;
 }

 
  
 public int getRandPos(int modv){
     return (Math.abs(((randVar.nextInt()) >>1))%(modv));
 }

 public void relocateFruit(){
       //g.drawRect(5, 5, width-10, height-10);
     //g.setClip(6,6,width-11,height-11);
     
     int x = getRandPos(width-12-fruit_w);
     int y = getRandPos(height-12-fruit_h);
     if(x<7){
         x=7;
     }
     if(y<7){
         y=7;
     }
     fruit.setPosition(x, y);
 }
 
         
 void cleanUP(){
     segmentList.removeAll();
     segmentList = null;
 }

 public void growSnake(){
    int x,y,dir=UP,len;
    int vecx,vecy;
   
    Segment s = segmentList.getCurrent();

    vecx = s.xpos - s.bLink.xpos;
    vecy = s.ypos - s.bLink.ypos;

    x = s.xpos;
    y = s.ypos;

    if(vecx == 0)
    {
        if(vecy > 0) 
        {
            dir = UP;
            y += s.height;
        }
        else if(vecy < 0)
        {
            dir = DOWN;
            y -= s.height;
        }
    }
    else if(vecy == 0) {
        if(vecx > 0)
        {
            dir = LEFT;
            x += s.width;
        }
        else if(vecx < 0)
        {
            dir = RIGHT;
            x -= s.width;
        }
    }
    segmentList.add(new Segment(x,y,dir));
}

 public void checkFPS(){
    fpsCounter++;
    if(fpsCounter > 30){
      fpsEndTime = System.currentTimeMillis();
      calculatedFPS = (int) (fpsCounter * 1000 / (fpsEndTime - fpsStartTime));
      fpsStartTime = fpsEndTime;
      fpsCounter =0;
    }
 }
 
 public void run(){
  while(isPlay == true)
  {
      if(running){
          long st = System.currentTimeMillis();
		  if((st - lastGCRequetTime) > 60000L){
			System.gc();
			lastGCRequetTime = st;
		 }
	
          tick();
          repaint();
          serviceRepaints();
          long et = System.currentTimeMillis();
          delay = frameTime - (et - st);
          
          if(debug){
               checkFPS();
          }

          if(delay > 0){
              synchronized(this){
                        try {
                            wait(delay);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
              }
          }
        }
    }//end while
  }

    public void commandAction(Command c, Displayable d) {
        if(c == startCommand){
            
            if(running == false){
                running = true;
                removeCommand(startCommand);
                addCommand(pauseCommand);
            }
    
            if(gameOver){
                segmentList.removeAll();
                initSnake();
                repaint();
                serviceRepaints();
            }
            
        }else if(c==stopCommand){
            midlet.destroyApp(false);
        }else if(c==pauseCommand){
            running = false;
            removeCommand(pauseCommand);
            addCommand(startCommand);
        }
    }
    
    private void updateScore(int a) {
        score+=a;
        int n = score;
        int i=0;
        while(n!=0){
            scoreDigit[i++] = (n % 10);
            n /= 10;
        }
        digitStart = i-1;
    }
    
    private void displayScore(Graphics g, int x, int y){
        int k=0;
        g.setColor(fgColor);
		g.setFont(font);
        for(int i=digitStart; i>=0;i--){
            int d = scoreDigit[i];
            char c = digitString.charAt(d);
            g.drawChar(c, x, y, Graphics.LEFT|Graphics.TOP);
            x += font.charWidth(c);
        }
    
    }
    

    
  //-end run()

}
