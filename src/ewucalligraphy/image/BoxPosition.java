/*
 * Copyright (C) 2014 David McInnis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package ewucalligraphy.image;

import ewucalligraphy.gui.DisplayWindow;
import static ewucalligraphy.image.BoxState.highlighted;
import static ewucalligraphy.image.BoxState.notHighlighted;
import static ewucalligraphy.image.BoxState.selected;
import java.awt.Color;
import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.MAGENTA;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */



 enum BoxState{highlighted, notHighlighted, selected}

class BoxPosition {
    private Rectangle boxLoc;
    private Point topLeft, topRight, bottomLeft, bottomRight;
    private BoxState curState;
    
    BoxPosition(NumberPairs vertPair, NumberPairs horizPair)
    {
        int edgeTop = vertPair.getFirst();
        int edgeBottom = vertPair.getLast();
        int boxHeight = edgeBottom - edgeTop;
        
        int edgeLeft =  horizPair.getFirst();
        int edgeRight = horizPair.getLast();
        int boxWidth = edgeRight - edgeLeft;
        
        curState = notHighlighted;
        boxLoc = new Rectangle(edgeLeft, edgeTop, boxWidth, boxHeight);
        setCorners();
    }
    
    private void setCorners()
    {
        topLeft  = new Point(getLeft(), getTop());
        topRight = new Point(getRight(), getTop());
        
        bottomLeft  = new Point(getLeft() , getBottom());
        bottomRight = new Point(getRight(), getBottom());
    }
    BoxPosition(BoxPosition parentBox, NumberPairs stripePair, boolean verticalStripes)
    {
        int boxWidth = 0;
        int boxHeight = 0;
        int edgeTop = 0;
        int edgeLeft = 0;
        
        
        if(verticalStripes)
        {
            edgeTop = parentBox.getTop();
            boxHeight = parentBox.getHeight();
            
            edgeLeft = stripePair.getFirst();
            boxWidth  = stripePair.getLast() - edgeLeft;
        }
        else
        {
            edgeLeft = parentBox.getLeft();
            boxWidth = parentBox.getWidth();
            
            edgeTop = stripePair.getFirst();
            boxHeight = stripePair.getLast() - edgeTop;
        }
        boxLoc = new Rectangle(edgeLeft, edgeTop, boxWidth, boxHeight);
        setCorners();
        curState = notHighlighted;
    }
    
    private int getWidth()
    {
        return boxLoc.width;
    }
    
    private int getHeight()
    {
        return boxLoc.height;
    }
    
    BoxPosition(int inTop, int inBottom, int inLeft, int inRight)
    {
        int boxHeight = inBottom - inTop;
        int boxWidth = inRight - inLeft;
        
        boxLoc = new Rectangle(inLeft, inTop, boxWidth, boxHeight);
    }
    
    int getTop()
    {
        return boxLoc.y;
    }
    
    int getBottom()
    {
        return boxLoc.y + boxLoc.height;
    }
    
    int getLeft()
    {
        return boxLoc.x;
    }
    
    int getRight()
    {
        return boxLoc.x + boxLoc.width;
    }
    
    void drawBox(Graphics g, DisplayWindow myWindow)
    {
        Color curColor = null;
        
        switch(curState)
        {
            case selected:
                curColor = GREEN;
                break;
            case highlighted:
                curColor = BLUE;
                break;
            case notHighlighted:
                curColor = MAGENTA;
                break;
        }
        
        myWindow.drawLine(g, topLeft, topRight, curColor);
        myWindow.drawLine(g, topRight, bottomRight, curColor);
        myWindow.drawLine(g, bottomRight, bottomLeft, curColor);
        myWindow.drawLine(g, bottomLeft, topLeft, curColor);
        
        
    }

    boolean setActive(Point relLocation)
    {
        boolean boxChanged = false;
        
        if(boxLoc.contains(relLocation)) // && curState == highlighted)
        {

            curState = selected;
            boxChanged = true;
        }
        return boxChanged;
    }
    

    void setState(BoxState newState)
    {
        curState = newState;
    }
    

    void deselect()
    {
        curState = notHighlighted;
    }

    BoxState getState()
    {
        return curState;
    }

}
