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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public class SegmentList {
    
   Segment head = null;
   Segment current = null;
   Segment iterator = null;
   int count=0;
   
    void add(Segment s) {
        if(head == null){
            head = s;
            head.setBackwardLink(null);
            current = head;
            count =1;
        }else{
            current.setForwardLink(s);
            s.setBackwardLink(current);
            current = s;
            count++;
        }
    }
    
    int size(){
        return count;
    }
    
    Segment get(int i){
        Segment t = head;
        for(int j=0; j<count;   j++){
            if(i==j){
                return t;
            }
            t = t.fLink;
        }
        return null;
    }
    
     Segment getCurrent(){
        return current;
    }
    
    void removeAll(){
        count =0;
        Segment cur = head.fLink;
        Segment prev = head;
        
        while(cur!=null){
            prev = null;
            cur = cur.fLink;
            prev = cur;
       }
    }
    
    Segment getIterator(){
        iterator = head;
        return iterator;
    }
    
    void render(Graphics g){
        Segment seg = head;
        head.paintHead(g);
        seg = seg.fLink;
        while(seg!=null){
            seg.paintSegment(g);
            seg = seg.fLink;
      }
    }
    
    Segment create(int startX, int startY, int nSegments, int segSize){
        int dir = Canvas.RIGHT;
        Segment h = new Segment(startX, startY, dir);
        h.setSize(segSize, segSize);
        add(h);
        int xpos=startX, ypos=startY;
        
        for(int i=1; i < nSegments; i++) {
            xpos = 30 - (i*segSize);
            Segment s = new Segment(xpos, ypos, dir);
            s.setSize(segSize, segSize);
            add(s);
        }
        return head;
    }
    
    boolean checkSelfCollision(){
        Segment ref = head;
        Segment seg = head.fLink.fLink.fLink;
        
        while(seg!=null){
            if(seg.isCollision(ref)){
                return true;
            }
            seg = seg.fLink;
       }
       return false;
    }
    
    boolean move()
    {
        boolean flag = false;
        flag = checkSelfCollision();
        
        if(!flag)
        {
            Segment seg = current;
            while(seg != head){
                seg.move();
                seg.dir =seg.bLink.dir;
                seg = seg.bLink;
            }
           head.move();
        }
        return flag;
    }
        
        
    
    
     
    
    
    
    
    
    
    

}
