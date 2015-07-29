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

/*
jad file for reference
MIDlet-1: Snake, Snake.png, GameMIDlet
MIDlet-Jar-Size: 7218
MIDlet-Jar-URL: Snake.jar
MIDlet-Name: Snake
MIDlet-Vendor: Unknown
MIDlet-Version: 1.0
MicroEdition-Configuration: CLDC-1.1
MicroEdition-Profile: MIDP-2.1
*/

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class GameMIDlet extends MIDlet{

  SnakeCanvas gameScreen;
  private Display display;

  public GameMIDlet(){
   //constructor
  }
  public void startApp()
  {
      if(gameScreen==null){
          gameScreen = new SnakeCanvas(this);
          gameScreen.start();
          Display.getDisplay(this).setCurrent(gameScreen);
      }else{
          Display.getDisplay(this).setCurrent(gameScreen);
      }
  
    
  }
  
  public Display getDisplay() {
    return display;
  }

  public void pauseApp(){
      gameScreen.stopRunning();
  }
  public void destroyApp(boolean f){
      gameScreen.cleanUP();
      gameScreen.stop();
      gameScreen=null;
      notifyDestroyed();
      System.gc();
  }

}