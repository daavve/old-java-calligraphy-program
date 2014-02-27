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
    private Row[][] vertRow, horRow;
    
    private int[][] sortedGlobal;
    private int[]   minVal, medVal, maxVal;
    
    
    
    
    //NOte, this is a bit inefficiet, because I create a lot of
    //temporary copies of parts of the image.
    
    public Statistics(int[][][] imG)
    {
        int iDepth = imG.length;
        int iHeight = imG[0].length;
        int iWidth = imG[0][0].length;
        int iMax = iWidth * iHeight;
        
        System.out.println("Width: " + iWidth + " Height: " + iHeight + " Pixels: " + iMax);
        
        
        vertRow = new Row[iDepth][iWidth];
        horRow  = new Row[iDepth][iHeight];
        
        int y, z;
        int[] tempRow, tempColumn;
        
        tempRow = new int[iWidth];
        tempColumn = new int[iHeight];
    
        sortedGlobal = new int[iDepth][iMax];
        minVal = new int[iDepth];
        medVal = new int[iDepth];
        maxVal = new int[iDepth];

        int cntr = 0;
        for(int x = 0; x < iDepth; ++x)
        {

            for(y = 0; y < iHeight; ++y)
            {
                for(z = 0; z < iWidth; ++z)
                {
                    tempRow[z] = imG[x][y][z];
                    sortedGlobal[x][cntr] = imG[x][y][z];
                    ++cntr;
                }
                horRow[x][y] = new Row(tempRow);
              
            }
           
            for(y = 0; y < iWidth; ++y)
            {
                for(z = 0; z < iHeight; ++z)
                {
                    tempColumn[z] = imG[x][z][y];
                }
                vertRow[x][y] = new Row(tempColumn);
            }
            cntr = 0;
         
            sort(sortedGlobal[x]);
            minVal[x] = sortedGlobal[x][0];
            medVal[x] = sortedGlobal[x][iMax / 2];
            maxVal[x] = sortedGlobal[x][iMax - 1];
            
            System.out.println("Min: " + minVal[x] + " Med: " + medVal[x] + " Max: " + maxVal[x]);
            
        }
    }
    
    
    public int getImgEdge(ImgDir edgeWeWant, int rVal) //Note: Currently only works for Black-on-White.
    {                 //Also: I might need to include general direction to accomodate noise
    //NOTE: On some images we return -1
    int retVal = -1;
            
       retVal = getImgEdgeMedianUnderMedian(edgeWeWant, rVal);


       return retVal;
    }
    
    private int getImgEdgeMedianUnderMedian(ImgDir edgeWeWant, int rVal)
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
