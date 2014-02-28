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
     private Row[][] horRow, vertRow;
    
    private int[][] sortedGlobal;
    private int[]   minVal, medVal, maxVal;
    
    
    
    
    //NOte, this is a bit inefficiet, because I create a lot of
    //temporary copies of parts of the image.
    
    public Statistics(int[][][] imG)
    {
        int iDepth = imG.length;
        int iWidth = imG[0].length;
        int iHeight = imG[0][0].length;
        int iMax = iHeight * iWidth;
        
        System.out.println("Width: " + iHeight + " Height: " + iWidth + " Pixels: " + iMax);
        
        
        vertRow = new Row[iDepth][iHeight];
        horRow  = new Row[iDepth][iWidth];
        
        int y, x;
        int[] tempRow, tempColumn;
        
        tempRow = new int[iHeight];
        tempColumn = new int[iWidth];
    
        sortedGlobal = new int[iDepth][iMax];
        minVal = new int[iDepth];
        medVal = new int[iDepth];
        maxVal = new int[iDepth];

        int cntr = 0;
        for(int z = 0; z < iDepth; ++z)
        {

            for(y = 0; y < iWidth; ++y)
            {
                for(x = 0; x < iHeight; ++x)
                {
                    tempRow[x] = imG[z] [y][x];
                    sortedGlobal[z][cntr] = imG[z] [y][x];
                    ++cntr;
                }
                horRow[z] [y] = new Row(tempRow);
              
            }
           
            for(y = 0; y < iHeight; ++y)
            {
                for(x = 0; x < iWidth; ++x)
                {
                    tempColumn[x] = imG[z][x][y];
                }
                vertRow[z][y] = new Row(tempColumn);
            }
            cntr = 0;
         
            sort(sortedGlobal[z]);
            minVal[z] = sortedGlobal[z][0];
            medVal[z] = sortedGlobal[z][iMax / 2];
            maxVal[z] = sortedGlobal[z][iMax - 1];
            
            System.out.println("Min: " + minVal[z] + " Med: " + medVal[z] + " Max: " + maxVal[z]);
            
        }
    }
    
    public int getMedian(int x)
    {
        return medVal[x];
    }
   
    public int getEdgeMedianUnderTarget(ImgDir edgeWeWant, int rVal, int tValue)
    {
        int edgeVal, x;
        
        edgeVal = -1;
        switch(edgeWeWant)
        {
            case TOP:
                for(x =  horRow[rVal].length / 2; x >= 0 && edgeVal == -1; --x)
                {
                    if(horRow[rVal][x].getMedian() > medVal[rVal])
                    {
                        edgeVal = x;
                    }
                }
                break;
            case BOTTOM:
                for(x = horRow[rVal].length / 2; x < horRow[rVal].length && edgeVal == -1; ++x)
                {
                    if(horRow[rVal][x].getMedian() > medVal[rVal])
                    {
                        edgeVal = x;
                    }
                }
                break;
            case RIGHT:
                for(x = vertRow[rVal].length / 2; x < vertRow[rVal].length && edgeVal == -1; ++x)
                {
                    if(vertRow[rVal][x].getMedian() > medVal[rVal])
                    {
                        edgeVal = x;
                    }
                }
                break;
            case LEFT:
                for(x = vertRow[rVal].length / 2; x > 0 && edgeVal == -1; --x)
                {
                    if(vertRow[rVal][x].getMedian() > medVal[rVal])
                    {
                        edgeVal = x;
                    }
                }
                break;
        }
        

        return edgeVal;
    }
      
    public String getGnuPlotHorizontalRows(int rVal)
    {
        assert(rVal < horRow.length);
        String outPut = "# horizontal row statistics for image\n";
        outPut += "# row     min     median     max\n";
        
        for(int x = 0; x < horRow[rVal].length; ++x)
        {
            outPut += "\n";
            outPut += x;
            outPut += " ";
            outPut += horRow[rVal][x].getMin();
            outPut += " ";
            outPut += horRow[rVal][x].getMedian();
            outPut += " ";
            outPut += horRow[rVal][x].getMax();
        }
        return outPut;
    }
    public String getGnuPlotVerticalRows(int rVal)
    {
        assert(rVal < vertRow.length);
        String outPut = "# vertical row statistics for image\n";
        outPut += "# row     min     median     max\n";
        
        for(int x = 0; x < vertRow[rVal].length; ++x)
        {
            outPut += "\n";
            outPut += x;
            outPut += " ";
            outPut += vertRow[rVal][x].getMin();
            outPut += " ";
            outPut += vertRow[rVal][x].getMedian();
            outPut += " ";
            outPut += vertRow[rVal][x].getMax();
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
