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

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class Line
{
    private final int[] start = new int[2];
    private final int[] end = new int[2];
    private Color myColor;
    
    
    public Line(int iStartY, int iStartX, int iEndY, int iEndX, Color newColor)
    {
        start[0] = iStartY;
        start[1] = iStartX;
        end[0]   = iEndY;
        end[1]   = iEndX;
        myColor = newColor;
    }
    
    public Color getColor()
    {
        return myColor;
    }
    
    public int[] getStart()
    {
        return start;
    }
    
    public int[] getEnd()
    {
        return end;
    }
    
    

    
}
