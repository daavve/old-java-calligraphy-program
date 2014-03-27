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

import static java.lang.Integer.MAX_VALUE;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.sort;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class Statistics
{
     private Row[] horRow, vertRow;
    
    private int[] sortedGlobal;
    private int[] topLeftCorner;
    private int   medVal;
    private boolean zeroSize;
    
    
    
    
    //Note, this is a bit inefficiet, because I create a lot of
    //temporary copies of parts of the image.
    
    public Statistics(int[][] imG)
    {
           buildStatistics(imG, 0, 0, imG.length, imG[0].length);
    }
    
    public Statistics(int[][] imG, int startX, int startY, int endX, int endY)
    {
        if(startX != endX && startY != endY)
        {
            buildStatistics(imG, startX, startY, endX, endY);
        }
        else
        {
            zeroSize = true;
        }
    }
    
   
    private void buildStatistics(int[][] imG, int startX, int startY, int endX, int endY)
    {
        topLeftCorner = new int[2];
        topLeftCorner[0] = startX;
        topLeftCorner[1] = startY;
        
        
        int distX = endX - startX;
        int distY = endY - startY;
      

        int iMax = distX * distY;
        
        vertRow = new Row[distX];
        horRow  = new Row[distY];
        
        int x, y;
        int[] tempHorRow, tempVertRow;
        
        tempHorRow = new int[distX];
        tempVertRow = new int[distY];
    
        sortedGlobal = new int[iMax];


        int cntr = 0;
        int Xcntr = 0;
        int Ycntr;

        for(x = startX; x < endX; ++x)
        {
            Ycntr = 0;
            for(y = startY; y < endY; ++y)
            {
                tempVertRow[Ycntr] = imG [x][y];
                sortedGlobal[cntr] = imG[x][y];
                ++cntr; ++Ycntr;
            }
            vertRow[Xcntr] = new Row(tempVertRow);
            ++Xcntr;
        }
        Ycntr = 0;
                
        for(y = startY; y < endY; ++y)
        {
            Xcntr = 0;
            for(x = startX; x < endX; ++x)
            {
                tempHorRow[Xcntr] = imG [x][y];
                ++Xcntr;
            }
            horRow[Ycntr] = new Row(tempHorRow);
            ++Ycntr;
        }
         
        sort(sortedGlobal);
        medVal = sortedGlobal[iMax / 2];
    }
    
    public int getMedian()
    {
        return medVal;
    }
   
    
    public String[] getGnuPlotCorrectedVals()
    {
        String[] outPut = new String[2];
        
        outPut[0] =  "#Overlapping vertical and horizontal sums for image\n";
        outPut[0] += "#X     xVal\n\n";
        
        outPut[1] = "#Overlapping vertical and horizontal sums for image\n";
        outPut[1] += "#Y     yVal\n\n";
        
        int imgHeight = horRow.length;
        int imgWidth  = vertRow.length;
        
        if(imgHeight > imgWidth)
        {
            double scaleFactor = (double) imgHeight / (double) imgWidth; 
            System.out.println("H: " + scaleFactor);
            for(int y = 0; y < imgHeight; ++y)
            {
                outPut[1] += y + " " + horRow[y].getSum() + "\n";
            }
            for(int x = 0; x < imgWidth; ++x)
            {
                outPut[0] += (((double) x) * scaleFactor) + " " + vertRow[x].getSum() + "\n";
            }
            
            
        }
        else
        {
            double scaleFactor = (double) imgWidth / (double) imgHeight;
            System.out.println("W: " + scaleFactor);
            for(int x = 0; x < imgWidth; ++x)
            {
                outPut[0] += x + " " + vertRow[x].getSum() + "\n";
            }
            for(int y = 0; y < imgHeight; ++y)
            {
                outPut[1] += (((double) y) * scaleFactor) + " " + horRow[y].getSum() + "\n";
            }
            
        }
        return outPut;
    }

      
    public String getGnuPlotHorizontalRows()
    {
        String outPut = "# horizontal row statistics for image\n";
        outPut += "# row     min     median     max     sum\n";
        
        for(int x = 0; x < horRow.length; ++x)
        {
            outPut += "\n";
            outPut += x;
            outPut += " ";
            outPut += horRow[x].getMin();
            outPut += " ";
            outPut += horRow[x].getMedian();
            outPut += " ";
            outPut += horRow[x].getMax();
            outPut += " ";
            outPut += horRow[x].getSum();
        }
        return outPut;
    }
    public String getGnuPlotVerticalRows()
    {
        String outPut = "# vertical row statistics for image\n";
        outPut += "# row    sum\n";
        
        for(int x = 0; x < vertRow.length; ++x)
        {
            outPut += "\n";
            outPut += x;
            outPut += " ";
            outPut += vertRow[x].getMin();
            outPut += " ";
            outPut += vertRow[x].getMedian();
            outPut += " ";
            outPut += vertRow[x].getMax();
            outPut += " ";
            outPut += vertRow[x].getSum();
        }
        return outPut;
    }
    
    public int GetSmallestSum(ImgDir horizOrVert)
    {
        int targetPos = 0;
        int curMinSum = MAX_VALUE;
        
        assert(horizOrVert == ImgDir.VERTICAL || horizOrVert == ImgDir.HORIZONTAL); 
        
        if(horizOrVert == ImgDir.HORIZONTAL)
        {
            int curSum;
            for(int x = 0; x < horRow.length; ++x)
            {
                curSum = horRow[x].getSum();
                if(curMinSum > curSum)
                {
                    curMinSum = curSum;
                    targetPos = x;
                }
            }
        }
        else
        {
            int curSum;
            for(int x = 0; x < vertRow.length; ++x)
            {
                curSum = vertRow[x].getSum();
                if(curMinSum > curSum)
                {
                    curMinSum = curSum;
                    targetPos = x;
                }
            }
        }
        return targetPos;
    }
    
 
    public int growTillTargetMedian(ImgDir startPosition, int targetMedian, boolean forceWindow)
    {
        if(zeroSize)
        {
            return -1;
        }

        assert(targetMedian >= 0 && targetMedian <= 255);
        assert(startPosition != ImgDir.HORIZONTAL && startPosition != ImgDir.VERTICAL);
        
        int cntr, newOffset;
        newOffset = 0;
        
        switch(startPosition)
        {
            case  TOP:
                cntr = vertRow.length - 1;
                while(targetMedian > vertRow[cntr].getMedian() && cntr > 0)
                {
                    --cntr;
                }
                
                if(cntr == vertRow.length - 1 && forceWindow)
                {
                    cntr -= vertRow.length / 10;
                }
                newOffset = topLeftCorner[1] + cntr;
                break;
            case BOTTOM:
                cntr = 0;
                while(targetMedian > vertRow[cntr].getMedian() && cntr < vertRow.length - 1)
                {
                    ++cntr;
                }
                if(cntr == 0 && forceWindow)
                {
                    cntr += vertRow.length / 10;
                }
                
                newOffset = topLeftCorner[1] + cntr;
                break;
            case RIGHT:
                cntr = 0;
                while(targetMedian > horRow[cntr].getMedian() && cntr < horRow.length - 1)
                {
                    ++cntr;
                }
                if(cntr == 0 && forceWindow)
                {
                    cntr += horRow.length / 10;
                }
                
                newOffset = topLeftCorner[0] + cntr;
                break;
            case LEFT:
                cntr = horRow.length - 1;
                while(targetMedian > horRow[cntr].getMedian() && cntr > 0)
                {
                    --cntr;
                }
                if(cntr == horRow.length - 1 && forceWindow)
                {
                    cntr -= horRow.length / 10;
                }
                
                newOffset = topLeftCorner[0] + cntr;
                break;
        }
        return newOffset;
    }
    
    
    private class Row
    {
        private final int[] sortedRow;
        private final int min, median, max;
        private int sum;
        
        public Row(int[] inRow)
        {
            int rowLength;
            
            rowLength = inRow.length;
            
            sortedRow = copyOf(inRow, rowLength);
            
            
            sort(sortedRow); //Sorts the array
            
            min    = sortedRow[0];
            median = sortedRow[rowLength / 2];
            max    = sortedRow[rowLength - 1];
            
            sum = 0;
            
            for(int x = 0; x < sortedRow.length; ++x)
            {
                sum += sortedRow[x];
            }
            
        }
              
        public int getMin()
        {
            return min;
        }
        
        public int getMedian()
        {
            return median;
        }
        
        public int getMax()
        {
            return max;
        }
        
        public int getSum()
        {
            return sum;
        }
        
    }
    
   
}