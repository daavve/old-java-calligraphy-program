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

import static ewucalligraphy.image.ArrayType.SUM;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.sqrt;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.sort;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */

enum ArrayType{MIN, MEDIAN, MEAN, MAX, SUM};

public class Statistics
{
    private Row[] horRows, vertRows;
    private Row   vertSums, horSums;
    
    private int[] sortedGlobal;
    private int[] topLeftCorner;
    private int   imgMedian, imgMean;
    private double stdDev;
    
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
        
        vertRows = new Row[distX];
        horRows  = new Row[distY];
        
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
            vertRows[Xcntr] = new Row(tempVertRow);
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
            horRows[Ycntr] = new Row(tempHorRow);
            ++Ycntr;
        }
         
        sort(sortedGlobal);
        imgMedian = sortedGlobal[iMax / 2];
        stdDev = getStdDeviation(sortedGlobal, imgMedian);
        vertSums = buildAggregateArray(horRows, SUM);
        horSums  = buildAggregateArray(vertRows, SUM);
        
    }
    
    public int getMedian()
    {
        return imgMedian;
    }
    
    public int getMean()
    {
        return imgMean;
    }
   
    public double getStdDev()
    {
        return stdDev;
    }
    
    
    public String[] getGnuPlotVertHorizSums()
    {
        String[] outPut = new String[2];
        
        outPut[0] =  "#vertical sums for image\n";
        outPut[0] += "#X     ySum\n\n";
        
        
        
        
        
        
        outPut[1] = "#horizontal sums for image\n";
        outPut[1] += "#Y     xSum\n\n";


        outPut[0] += vertSums.getGnuPlot();
        outPut[1] += horSums.getGnuPlot();

        return outPut;
    }

      
    public String getGnuPlotHorizontalRows()
    {
        String outPut = "# horizontal row statistics for image\n";
        outPut += "# row     min     median     max     sum\n";
        
        for(int x = 0; x < horRows.length; ++x)
        {
            outPut += "\n";
            outPut += x;
            outPut += " ";
            outPut += horRows[x].getMin();
            outPut += " ";
            outPut += horRows[x].getMedian();
            outPut += " ";
            outPut += horRows[x].getMax();
            outPut += " ";
            outPut += horRows[x].getSum();
        }
        return outPut;
    }
    public String getGnuPlotVerticalRows()
    {
        String outPut = "# vertical row statistics for image\n";
        outPut += "# row    sum\n";
        
        for(int x = 0; x < vertRows.length; ++x)
        {
            outPut += "\n";
            outPut += x;
            outPut += " ";
            outPut += vertRows[x].getMin();
            outPut += " ";
            outPut += vertRows[x].getMedian();
            outPut += " ";
            outPut += vertRows[x].getMax();
            outPut += " ";
            outPut += vertRows[x].getSum();
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
            for(int x = 0; x < horRows.length; ++x)
            {
                curSum = horRows[x].getSum();
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
            for(int x = 0; x < vertRows.length; ++x)
            {
                curSum = vertRows[x].getSum();
                if(curMinSum > curSum)
                {
                    curMinSum = curSum;
                    targetPos = x;
                }
            }
        }
        return targetPos;
    }
    
    private Row buildAggregateArray(Row[] inRow, ArrayType typeToGet)
    {
        int[] outArray = new int[inRow.length];
        
        switch(typeToGet)
        {
            case SUM:
                for(int x = 0; x < inRow.length; ++x)
                {
                    outArray[x] = inRow[x].getSum();
                }
            break;
            case MIN:
                for(int x = 0; x < inRow.length; ++x)
                {
                    outArray[x] = inRow[x].getMin();
                }
            break;
                case MAX:
                for(int x = 0; x < inRow.length; ++x)
                {
                    outArray[x] = inRow[x].getMax();
                }
            break;
                case MEDIAN:
                for(int x = 0; x < inRow.length; ++x)
                {
                    outArray[x] = inRow[x].getMedian();
                }
            break;
                case MEAN:
                for(int x = 0; x < inRow.length; ++x)
                {
                    outArray[x] = inRow[x].getMean();
                }
        }
        
        for(int x = 0; x < inRow.length; ++x)
        {
            outArray[x] = inRow[x].getSum();
        }
        
        return  new Row(outArray);
    }

    boolean doWeHaveABadEdge()
    {
        int vertSumMin, horSumMin;
        int vertMinLoc, horMinLoc;
        
        int curVal;
        boolean weHaveBadEdge = false;
        
        vertSumMin = Integer.MAX_VALUE;  horSumMin  = Integer.MAX_VALUE;
        vertMinLoc = 0; horMinLoc = 0;
        
        for(int x = 0; x < vertRows.length; ++x)
        {
            curVal = vertRows[x].getSum();
            if(curVal < vertSumMin)
            {
                vertSumMin = curVal;
                vertMinLoc = x;
            }
        }
        
        for(int x = 0; x < horRows.length; ++x)
        {
            curVal = horRows[x].getSum();
            if(curVal < horSumMin)
            {
                horSumMin = curVal;
                horMinLoc = x;
            }
        }
        
        if(vertMinLoc == 0 || vertMinLoc == vertRows.length - 1 ||
           horMinLoc == 0  || horMinLoc  == horRows.length - 1)
        {
            weHaveBadEdge = true;
        }
        return weHaveBadEdge;
    }

 
    private class Row
    {
        private final int[] sortedRow;
        private int[] refRow;
        private final int min, median, mean, max;
        private final double stdDev;
        
        private int sum;
        
        public Row(int[] inRow)
        {
            int rowLength;
            
            refRow = inRow;
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
            
            mean = sum / sortedRow.length;
            
            stdDev = getStdDeviation(sortedRow, mean);
        }
        
        public String getGnuPlot()
        {
            String outPut = "";
            for(int x = 0; x < refRow.length ; ++x)
            {
                outPut += x + " " + refRow[x] + "\n";
            }
            return outPut;
        }
                   
        public int getMin()
        {
            return min;
        }
        
                
        public int getMax()
        {
            return max;
        }
        
        public int getSum()
        {
            return sum;
        }
        
        public int getMean()
        {
            return mean;
        }
        
        
        public int getMedian()
        {
            return median;
        }
        
        public double getStdDev()
        {
            return stdDev;
        }
    }
    
    private static double getStdDeviation(int[] inArray, int mean)
    {
        int sumVar = 0;
        
        for(int x = 0; x < inArray.length; ++x)
        {
            sumVar =+ (inArray[x] - mean) ^ 2;
        }
        
        int variance = sumVar / inArray.length;
        
        return sqrt(variance);
    }
   
}