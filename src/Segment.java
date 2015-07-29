/**
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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

class Segment
 {
    public Segment fLink;
    public Segment bLink;
    
    static final int ALIVE = 1;
    static final int DEAD  = 0;
    static final int FRUIT_INSIDE = 3;
    public int xpos,ypos;
    int dir;
    int status;
    int width=5, height=5;
    
    static int boundHt;
    static int boundWt;
    public static void setBounds(int boundW, int boundH){
        boundHt = boundH;
        boundWt = boundW;
    }
    
    public void  setForwardLink(Segment s){
        fLink = s;
    }
    
     public void  setBackwardLink(Segment s){
        bLink = s;
    }
     
    public Segment  getForwardLink(){
        return fLink;
    }
    
     public Segment  getBackwardLink(){
       return bLink;
    }
    
    public Segment(){
     xpos = 0;
     ypos =0;
     dir = Canvas.UP;
     status = ALIVE;
    }
 
    public Segment(int x,int y,int d){
        xpos = x;
        ypos =y;
        dir = d;
        status = ALIVE;
    }
    
    public void setDir(int d){
        dir = d;
    }
      
    
    public void move(){
        switch(dir)
        {
            case Canvas.UP:
                ypos -= height;
                if(ypos < 0){
                    ypos = boundHt;
                }
                break;
            case Canvas.DOWN:                
                ypos += height;
                ypos %= boundHt;
                break;
            case Canvas.RIGHT:
                xpos += width;
                xpos %= boundWt;
                break;
            case Canvas.LEFT:
                xpos -= width;
                if(xpos < 0){
                    xpos = boundWt;
                }
                break;
        }
        
    }
    
    
    public void update(int x,int y,int d,int s)
    {
        xpos = x;
        ypos =y;
        dir = d;
        status = s;
		
    }
	
	int mixColor(int srcColor, int dstColor){
	   int sr = ((srcColor & 0x00ff0000) >> 16);
       int sg = ((srcColor & 0x0000ff00) >> 8);
       int sb = (srcColor & 0x000000ff);
	   sr = (sr*4)/10;
	   sg = (sg*4)/10;
	   sb = (sb*4)/10;
	   
       int dr = ((dstColor & 0x00ff0000) >> 16);
       int dg = ((dstColor & 0x0000ff00) >> 8);
       int db = (dstColor & 0x000000ff);
	   dr = (dr*6)/10;
	   dg = (dg*6)/10;
	   db = (db*6)/10;	   
	   
	   sr = (sr+dr);
	   sg = (sg+dg);
	   sb = (sb+db);
	   int c = ((sr << 16) + (sg << 8) + sb);
	   
	   return c;
 }
        
    public void paintSegment(Graphics g){
        g.setColor(2176705);
        g.fillRect(xpos, ypos, width, height);
    }
	
	public void paintHead(Graphics g){
		g.setColor(2176705);
        g.fillRect(xpos, ypos, width, height);
    }

    
     public void paintFruit(Graphics g){
		g.setColor(0xcc7f90);
        g.drawArc(xpos, ypos, width, height, 0, 360);
		g.setColor(0xcd8091);
        g.fillArc(xpos, ypos, width, height, 0, 360);
		
    }
     
    
    public static boolean checkCollision(
            int left1,int top1,int right1,int bottom1, 
            int left2,int top2,int right2,int bottom2
            )
    {
        int  x1, x2;
        // If it is outside our Vertical Height limit simply neglect it!
        if(bottom2 < top1 || top2 > bottom1){
            return false;
        }
        else
        {
            x1 = ( left2  > left1  )? left2 :  left1;
            x2 = ( right2 < right1 )? right2 : right1;
            if (x1 > x2)
                return false;
            return true;
        }
    }
    
    public boolean isCollision(Segment s){
        return checkCollision(
                s.xpos,
                s.ypos, 
                s.xpos + s.width, 
                s.ypos + s.height,
                xpos,
                ypos, 
                xpos + width, 
                ypos + height
        );
    }
    public boolean isCollision(int x, int y, int w, int h){
        return checkCollision(
                x,
                y, 
                x+w, 
                y+h,
                xpos,
                ypos, 
                xpos + width, 
                ypos + height
        );
    }
    
    public void setPosition(int _x, int _y){
        xpos = _x;
        ypos  = _y;
    }
    
    public void setSize(int _w, int _h){
        width = _w;
        height = _h;
    }
    


 }//end class segment

