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

import java.awt.Graphics;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class Line
{
    private boolean horizontal;
    private int offSet;
    private int[] imageSize = new int[2];
    
    public Line(boolean iHorizontal, int iOffset, int[] iMageSize)
    {
        horizontal = iHorizontal;
        offSet = iOffset;
        imageSize[0] = iMageSize[0];
        imageSize[1] = iMageSize[1];
    }
    
    public void drawLine(Graphics g, int[] newCanvasSize)
    {
        if(horizontal)
        {
            int newHeight = newCanvasSize[0] * imageSize[0] / offSet;
        }
        else //vertial
        {
            int newSlide = newCanvasSize[1] * imageSize[1] / offSet;
        }
    }
    
}
