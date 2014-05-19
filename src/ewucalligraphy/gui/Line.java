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


package ewucalligraphy.gui;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class Line
{
     private final int[] start = new int[2];
    private final int[] end = new int[2];
    private final Point startP, endP;
    private Color myColor;
    
    
    public Line(int iStartX, int iStartY, int iEndX, int iEndY, Color newColor)
    {
        start[0] = iStartX;
        start[1] = iStartY;
        end[0]   = iEndX;
        end[1]   = iEndY;
        startP = new Point(iStartX, iStartY);
        endP   = new Point(iEndX, iEndY);
        
        myColor = newColor;
    }
    
    public Color getColor()
    {
        return myColor;
    }
    
    public Point getStart()
    {
        return startP;
    }
    
    public Point getEnd()
    {
        return endP;
    }
    
    
    

       
}
