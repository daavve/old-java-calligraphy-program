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
import static ewucalligraphy.image.ImgDir.BOTTOM;
import static ewucalligraphy.image.ImgDir.LEFT;
import static ewucalligraphy.image.ImgDir.RIGHT;
import static ewucalligraphy.image.ImgDir.TOP;
import static ewucalligraphy.image.ImgQuadrant.I;
import static ewucalligraphy.image.ImgQuadrant.II;
import static ewucalligraphy.image.ImgQuadrant.III;
import static ewucalligraphy.image.ImgQuadrant.IV;

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
    
    
    public ImgBox(int[][] inImg)
    {
        imgStats = new Statistics(inImg);
        imgRef = inImg;
        
        int[] lineX = new int[1];
        int[] lineY = new int[1];
        lineX[0] = imgStats.GetSmallestMedian(ImgDir.VERTICAL);
        lineY[0] = imgStats.GetSmallestMedian(ImgDir.HORIZONTAL);
        
        Statistics[][] quadStats = StatisticsFactory.buildStatsGrid(imgRef, lineX, lineY);
        
        int maxMedian = 0;
        int minMedian = 255;
        int minMedX = 0;
        int minMedY = 0;
        int curMedian;
        
        for(int x = 0; x < 2; ++x)
        {
            for(int y = 0; y < 2; ++y)
            {
                curMedian = quadStats[x][y].getMedian();

                if(maxMedian < curMedian)
                {
                    maxMedian = curMedian;

                }
                if(minMedian > curMedian)
                {
                    minMedian = curMedian;
                    minMedX = x; minMedY = y;
                }
            
            }
            System.out.println();
        }
        
        ImgQuadrant darkestQuadrant = getDarkestQuadrant(minMedX, minMedY);
        
       
        switch(darkestQuadrant)
        {
            case I:
                int right = lineX[0];
                int bottom = lineY[0];
                int left = quadStats[0][0].growTillTargetMedian(LEFT, maxMedian, true);
                int top = quadStats[0][0].growTillTargetMedian(TOP, maxMedian, true);
                mainBox = new BoxPosition(top, bottom, left, right);
                break;
            case II:
                left = lineX[0];
                bottom = lineY[0];
                right = quadStats[1][0].growTillTargetMedian(RIGHT, maxMedian, true);
                top = quadStats[1][0].growTillTargetMedian(TOP, maxMedian, true);
                mainBox = new BoxPosition(top, bottom, left, right);
                break;
            case III:
                left = lineX[0];
                top = lineY[0];
                right = quadStats[1][1].growTillTargetMedian(RIGHT, maxMedian, true);
                bottom = quadStats[1][1].growTillTargetMedian(BOTTOM, maxMedian, true);
                mainBox = new BoxPosition(top, bottom, left, right);
                break;
            case IV:
                right = lineX[0];
                top = lineY[0];
                left = quadStats[0][1].growTillTargetMedian(LEFT, maxMedian, true);
                bottom = quadStats[0][1].growTillTargetMedian(BOTTOM, maxMedian, true);
                mainBox = new BoxPosition(top, bottom, left, right);
                break;
        }
       
    }
    
    public void growBox()
    {
        Statistics[][] boxStats = StatisticsFactory.buildStatsGrid(imgRef, mainBox);
        
        int maxMedian = 0;
        int minMedian = 255;
        int minMedX;
        int minMedY;
        int curMedian;
        
        
        for(int y = 0; y < 3; ++y)
        {
            for(int x = 0; x < 3; ++x)
            {
                curMedian = boxStats[x][y].getMedian();
                System.out.print(curMedian + ":");

                if(maxMedian < curMedian)
                {
                    maxMedian = curMedian;

                }
                if(minMedian > curMedian)
                {
                    minMedian = curMedian;
                    minMedX = x; minMedY = y;
                }
            
            }
            System.out.println();
        }
        System.out.println("--------------------");
        
        
        
       int newTop    = boxStats[1][0].growTillTargetMedian(TOP, maxMedian, false);
       int newLeft   = boxStats[0][1].growTillTargetMedian(LEFT, maxMedian, false);
       int newRight  = boxStats[2][1].growTillTargetMedian(RIGHT, maxMedian, false);
       int newBottom = boxStats[1][2].growTillTargetMedian(BOTTOM,maxMedian, false);
       
       if(newTop != -1 && newLeft != -1 && newRight != -1 && newBottom != -1)
       {
           mainBox = new BoxPosition(newTop, newBottom, newLeft, newRight);
       }
       
    }
    
    
    //Usually shrinks way too much or not nearly enough.
    public void shrinkBox()
    {
        Statistics[][] boxStats = StatisticsFactory.buildStatsGrid(imgRef, mainBox);
        
        int maxMedian = 0;
        int minMedian = 255;
        int minMedX = 0;
        int minMedY = 0;
        int curMedian;
        
        
        for(int y = 0; y < 3; ++y)
        {
            for(int x = 0; x < 3; ++x)
            {
                curMedian = boxStats[x][y].getMedian();

                if(maxMedian < curMedian)
                {
                    maxMedian = curMedian;

                }
                if(minMedian > curMedian)
                {
                    minMedian = curMedian;
                    minMedX = x; minMedY = y;
                }
            
            }
        }
        
        
        
       int newBottom    = boxStats[1][1].growTillTargetMedian(TOP, maxMedian, false);
       int newRight   = boxStats[1][1].growTillTargetMedian(LEFT, maxMedian, false);
       int newLeft  = boxStats[1][1].growTillTargetMedian(RIGHT, maxMedian, false);
       int newTop = boxStats[1][1].growTillTargetMedian(BOTTOM,maxMedian, false);
       
       if(newTop != -1 && newLeft != -1 && newRight != -1 && newBottom != -1)
       {
           mainBox = new BoxPosition(newTop, newBottom, newLeft, newRight);
       }
    }
    
    
    
    public void drawBox(DisplayWindow disWindow)
    {
        mainBox.drawBox(disWindow);
    }
    
    
        private ImgQuadrant getDarkestQuadrant(int minMedX, int minMedY)
        {
        ImgQuadrant darkestQuadrant;
        
        if(minMedX == 0)
        {
            if(minMedY == 0)
            {
                darkestQuadrant = I;
            }
            else
            {
                darkestQuadrant = II;
            }
        }
        else
        {
            if(minMedY == 0)
            {
                darkestQuadrant = IV;
            }
            else
            {
                darkestQuadrant = III;
            }
        }
    return darkestQuadrant;
    }
    
    
}
