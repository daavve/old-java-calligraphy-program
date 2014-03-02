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
    private int   minVal, medVal, maxVal;
    
    
    
    
    //NOte, this is a bit inefficiet, because I create a lot of
    //temporary copies of parts of the image.
    
    public Statistics(int[][] imG)
    {
        int iWidth = imG.length;
        int iHeight = imG[0].length;
        int iMax = iHeight * iWidth;
        
        System.out.println("Width: " + iHeight + " Height: " + iWidth + " Pixels: " + iMax);
        
        
        vertRow = new Row[iHeight];
        horRow  = new Row[iWidth];
        
        int x, y;
        int[] tempHorRow, tempVertRow;
        
        tempHorRow = new int[iHeight];
        tempVertRow = new int[iWidth];
    
        sortedGlobal = new int[iMax];


        int cntr = 0;

        for(x = 0; x < iWidth; ++x)
        {
            for(y = 0; y < iHeight; ++y)
            {
                tempHorRow[y] = imG [x][y];
                sortedGlobal[cntr] = imG[x][y];
                ++cntr;
            }
            horRow[x] = new Row(tempHorRow);
          
        }
        
        for(x = 0; x < iHeight; ++x)
        {
            for(y = 0; y < iWidth; ++y)
            {
                tempVertRow[y] = imG [y][x];
            }
            vertRow[x] = new Row(tempVertRow);
        }
         
        sort(sortedGlobal);
        minVal = sortedGlobal[0];
        medVal = sortedGlobal[iMax / 2];
        maxVal = sortedGlobal[iMax - 1];
        
        System.out.println("Min: " + minVal + " Med: " + medVal + " Max: " + maxVal);
            
    }
    
    public int getMedian()
    {
        return medVal;
    }
   
    public int getEdgeMedianUnderTarget(ImgDir edgeWeWant, int tValue)
    {
        int edgeVal, x;
        
        edgeVal = -1;
        switch(edgeWeWant)
        {
            case TOP:
                for(x =  vertRow.length / 2; x >= 0 && edgeVal == -1; --x)
                {
                    if(vertRow[x].getMedian() > tValue)
                    {
                        edgeVal = x;
                    }
                }
                break;
            case BOTTOM:
                for(x = vertRow.length / 2; x < vertRow.length && edgeVal == -1; ++x)
                {
                    if(vertRow[x].getMedian() > tValue)
                    {
                        edgeVal = x;
                    }
                }
                break;
            case RIGHT:
                for(x = horRow.length / 2; x < horRow.length && edgeVal == -1; ++x)
                {
                    if(horRow[x].getMedian() > tValue)
                    {
                        edgeVal = x;
                    }
                }
                break;
            case LEFT:
                for(x = horRow.length / 2; x > 0 && edgeVal == -1; --x)
                {
                    if(horRow[x].getMedian() > tValue)
                    {
                        edgeVal = x;
                    }
                }
                break;
        }
        

        return edgeVal;
    }
      
    public String getGnuPlotHorizontalRows()
    {
        String outPut = "# horizontal row statistics for image\n";
        outPut += "# row     min     median     max\n";
        
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
        }
        return outPut;
    }
    public String getGnuPlotVerticalRows()
    {
        String outPut = "# vertical row statistics for image\n";
        outPut += "# row     min     median     max\n";
        
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
        }
        return outPut;
    }
    
    private class Row
    {
        private final int[] sortedRow;
        private final int min, median, max;
        
        public Row(int[] inRow)
        {
            int rowLength;
            
            rowLength = inRow.length;
            
            sortedRow = copyOf(inRow, rowLength);
            
            sort(sortedRow); //Sorts the array
            
            min    = sortedRow[0];
            median = sortedRow[rowLength / 2];
            max    = sortedRow[rowLength - 1];
            
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
        
    }
    
   
}