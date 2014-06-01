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
import static java.awt.Color.CYAN;
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

public class BoxPosition {
    private Rectangle boxLoc;
    private Point topLeft, topRight, bottomLeft, bottomRight;
    private BoxState curState;
    
    public BoxPosition(NumberPairs vertPair, NumberPairs horizPair)
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
    public BoxPosition(BoxPosition parentBox, NumberPairs stripePair, boolean verticalStripes)
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
    }
    
    public int getWidth()
    {
        return boxLoc.width;
    }
    
    public int getHeight()
    {
        return boxLoc.height;
    }
    
    public BoxPosition(int inTop, int inBottom, int inLeft, int inRight)
    {
        int boxHeight = inBottom - inTop;
        int boxWidth = inRight - inLeft;
        
        boxLoc = new Rectangle(inLeft, inTop, boxWidth, boxHeight);
    }
    
    public int getTop()
    {
        return boxLoc.y;
    }
    
    public int getBottom()
    {
        return boxLoc.y + boxLoc.height;
    }
    
    public int getLeft()
    {
        return boxLoc.x;
    }
    
    public int getRight()
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
                curColor = CYAN;
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
    


    BoxState dectMouseOver(Point relLocation)
    {
        boolean redrawBox = false;
        boolean mouseInsideBox = boxLoc.contains(relLocation);
        
        switch(curState)
        {
            case selected:
                //DO NOTHING, we can only be de-selected if another box is selected;
                break;
            case highlighted:
                if(!mouseInsideBox)
                {
                    curState = notHighlighted;
                    redrawBox = true;
                }
                break;
            case notHighlighted:
                if(mouseInsideBox)
                {
                    curState = highlighted;
                    redrawBox = true;
                }
                break;
        }
        
        return curState;
    }

    void deselect()
    {
        curState = notHighlighted;
    }

   }
