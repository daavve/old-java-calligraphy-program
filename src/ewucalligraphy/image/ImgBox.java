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
import static ewucalligraphy.image.ImgDir.BOTTOM;
import static ewucalligraphy.image.ImgDir.LEFT;
import static ewucalligraphy.image.ImgDir.RIGHT;
import static ewucalligraphy.image.ImgDir.TOP;
import static ewucalligraphy.image.ImgQuadrant.I;
import static ewucalligraphy.image.ImgQuadrant.II;
import static ewucalligraphy.image.ImgQuadrant.III;
import static ewucalligraphy.image.ImgQuadrant.IV;
import static java.awt.Color.MAGENTA;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class ImgBox
{
    private Statistics imgStats;
    private Statistics[][] boxStats;
    private int[][] imgRef;
    private BoxPosition mainBox;
    private int imgWidth, imgHeight, xCross, yCross;
    
    
    public ImgBox(int[][] inImg)
    {
        imgStats = new Statistics(inImg);
        imgRef = inImg;
        
        imgWidth  = imgRef.length;
        imgHeight = imgRef[0].length;
        
        int[] lineX = new int[1];
        int[] lineY = new int[1];
        
        yCross = imgStats.GetSmallestSum(ImgDir.HORIZONTAL);
        lineX[0] = yCross;
        
        xCross = imgStats.GetSmallestSum(ImgDir.VERTICAL);
        lineY[0] = xCross;
        
        
        
//        Statistics[][] quadStats = StatisticsFactory.buildStatsGrid(imgRef, lineX, lineY);
        

    }
    
    public void drawCross(DisplayWindow disWindow)
    {
        Line vertLine = new Line(xCross, 0, xCross, imgHeight, MAGENTA);
        Line horLine  = new Line(0, yCross, imgWidth, yCross, MAGENTA);
        
        disWindow.addLine(vertLine);
        disWindow.addLine(horLine);
        disWindow.repaint();
    }
    
    
    public void drawBox(DisplayWindow disWindow)
    {
        mainBox.drawBox(disWindow);
    }
    
}
