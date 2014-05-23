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
import ewucalligraphy.gui.Line;
import static java.awt.Color.MAGENTA;
import java.awt.Rectangle;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */




public class BoxPosition {
    private Rectangle boxLoc;
    
    public BoxPosition(NumberPairs vertPair, NumberPairs horizPair)
    {
        int edgeTop = vertPair.getFirst();
        int edgeBottom = vertPair.getLast();
        int boxHeight = edgeBottom - edgeTop;
        
        int edgeLeft =  horizPair.getFirst();
        int edgeRight = horizPair.getLast();
        int boxWidth = edgeRight - edgeLeft;
        
        boxLoc = new Rectangle(edgeLeft, edgeTop, boxWidth, boxHeight);
        
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
    
    public Rectangle getRectangle()
    {
        return boxLoc;
    }

    void drawBox(DisplayWindow disWindow)
    {
        int edgeLeft = this.getLeft();
        int edgeTop = this.getTop();
        int edgeRight = this.getRight();
        int edgeBottom = this.getBottom();

        Line top    = new Line(edgeLeft, edgeTop, edgeRight, edgeTop, MAGENTA);
        Line right  = new Line(edgeRight, edgeTop, edgeRight, edgeBottom, MAGENTA);
        Line bottom = new Line(edgeRight, edgeBottom, edgeLeft, edgeBottom, MAGENTA);
        Line left   = new Line(edgeLeft, edgeBottom, edgeLeft, edgeTop, MAGENTA);
        
        disWindow.addLine(top);
        disWindow.addLine(right);
        disWindow.addLine(bottom);
        disWindow.addLine(left);
    }
}
