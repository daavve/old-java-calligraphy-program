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

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class ImgBox
{
    private int[][] imgRef;
    private BoxPosition mainBox;
    
    public ImgBox(int[][] inImg)
    {
        imgRef = inImg;
        
    }
    
    public void drawBox(DisplayWindow disWindow)
    {
        mainBox.drawBox(disWindow);
    }
    
}
