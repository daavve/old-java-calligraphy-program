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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


class ImgBox
{
    private final int[][] imgRef;
    private final BoxPosition imgBox;
    private final Statistics statsRef;
    private Statistics boxStats;
    
    
    ImgBox(int[][] inImg, Statistics inStats, BoxPosition inPosition)
    {
        imgRef = inImg;
        statsRef = inStats;
        imgBox = inPosition;
    }
    
    BoxPosition getPosition()
    {
        return imgBox;
    }
    
    void drawBox(Graphics g, DisplayWindow myWindow)
    {
        imgBox.drawBox(g, myWindow);
    }
    
    void setState(BoxState newState)
    {
        imgBox.setState(newState);
    }
    
    boolean setActive(Point relLocation)
    {
        return imgBox.setActive(relLocation);
    }
    
    static LinkedList<ImgBox> buildImgBoxes(int[][] inImg, Statistics inStats, boolean findDarkest)
    {
        LinkedList<ImgBox> boxList = new LinkedList<>();
        
        LinkedList<BoxPosition> vertStripes = inStats.buildBoxes(true, findDarkest); //Always do vertical first.....
        LinkedList<BoxPosition> horizStripes = new LinkedList<>();
        
        for(BoxPosition curVertStripe : vertStripes)
        {
            Statistics vertStripeStats = inStats.buildChildStats(curVertStripe);
            horizStripes.addAll(vertStripeStats.buildBoxes(false, findDarkest));
        }
        
        for(BoxPosition allBoxes: horizStripes)
        {
            boxList.add(new ImgBox(inImg, inStats, allBoxes));
        }
        
        return boxList;
    }

    void deselect()
    {
        imgBox.deselect();
    }

    BoxState getState()
    {
        return imgBox.getState();
    }

    Rectangle getRectangle()
    {
        return imgBox.getRectangle();
    }
   
}
