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
    private int edgeTop, edgeBottom, edgeLeft, edgeRight;
    private Rectangle boxLoc;
    
    public BoxPosition(NumberPairs vertPair, NumberPairs horizPair)
    {
        edgeTop = vertPair.getFirst();
        edgeBottom = vertPair.getLast();
        int boxHeight = edgeBottom = edgeTop;
        
        edgeLeft =  horizPair.getFirst();
        edgeRight = horizPair.getLast();
        int boxWidth = edgeRight - edgeLeft;
        
        boxLoc = new Rectangle(edgeLeft, edgeTop, boxWidth, boxHeight);
        
    }
    
    public BoxPosition(BoxPosition parentBox, NumberPairs stripePair, boolean verticalStripes)
    {
        int boxWidth = 0;
        int boxHeight = 0;
        
        if(verticalStripes)
        {
            edgeTop = parentBox.getTop();
            edgeBottom = parentBox.getBottom();
            
            edgeLeft = stripePair.getFirst();
            edgeRight = stripePair.getLast();
        }
        else
        {
            edgeLeft = parentBox.edgeLeft;
            edgeRight = parentBox.edgeRight;
            
            edgeTop = stripePair.getFirst();
            edgeBottom = stripePair.getLast();
        }
        boxLoc = new Rectangle(edgeLeft, edgeTop, boxWidth, boxHeight);
    }
    
    public BoxPosition(int inTop, int inBottom, int inLeft, int inRight)
    {
        edgeTop = inTop;
        edgeBottom = inBottom;
        int boxHeight = inBottom - inTop;
        
        edgeLeft = inLeft;
        edgeRight = inRight;
        int boxWidth = inRight - inLeft;
        
        boxLoc = new Rectangle(edgeLeft, edgeTop, boxWidth, boxHeight);
    }
    
    public int getTop()
    {
        return edgeTop;
    }
    
    public int getBottom()
    {
        return edgeBottom;
    }
    
    public int getLeft()
    {
        return edgeLeft;
    }
    
    public int getRight()
    {
        return edgeRight;
    }
    
    public int getSize()
    {
        return (edgeBottom - edgeTop) * (edgeRight - edgeLeft);
    }
    
    public Rectangle getRectangle()
    {
        return boxLoc;
    }

    void drawBox(DisplayWindow disWindow)
    {
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
