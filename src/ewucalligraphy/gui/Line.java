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

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class Line
{
    private final int[] start = new int[2];
    private final int[] end = new int[2];
    
    public Line(int[] iStart, int[] iEnd)
    {
        start[0] = iStart[0];
        start[1] = iStart[1];
        end[0] = iEnd[0];
        end[1] = iEnd[1];
    }
    
    public Line(int iStartY, int iStartX, int iEndY, int iEndX)
    {
        start[0] = iStartY;
        start[1] = iStartX;
        end[0]   = iEndY;
        end[1]   = iEndX;
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
