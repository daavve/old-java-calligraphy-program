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
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.sort;
import java.util.LinkedList;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */

enum ArrayType{MIN, MEDIAN, MEAN, MAX, SUM};

public class Statistics
{
    private int[][] imgRef;
    
    private Row[] horRows, vertRows;
    private Row   vertSums, horSums;
    
    private int[] sortedGlobal;
    private int[] topLeftCorner;
    private int   imgMedian, imgMean;
    private double stdDev;
    
    private boolean zeroSize;
    
    private BoxPosition myPosition;
    
    
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
    
    
    //TODO: Transfer Responsibility for size calculations into BoxPosition.
    
    protected Statistics buildChildStats(BoxPosition newPosition)
    {
        return new Statistics(imgRef, newPosition.getLeft(),
                                      newPosition.getTop(),
                                      newPosition.getRight(),
                                      newPosition.getBottom());
    }
   
    private void buildStatistics(int[][] imG, int startX, int startY, int endX, int endY)
    {
        imgRef = imG;
        
        topLeftCorner = new int[2];
        topLeftCorner[0] = startX;
        topLeftCorner[1] = startY;
        
        myPosition = new BoxPosition(startY, endY, startX, endX);
        
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
        int imgDarkSum = 0;
        int curDark;

        for(x = startX; x < endX; ++x)
        {
            Ycntr = 0;
            for(y = startY; y < endY; ++y)
            {
                curDark = imG[x][y];
                tempVertRow[Ycntr] = curDark;
                sortedGlobal[cntr] = curDark;
                imgDarkSum += curDark;
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
        imgMean = imgDarkSum / iMax;
        imgMedian = sortedGlobal[iMax / 2];
        stdDev = getStdDeviation(sortedGlobal, imgMean);
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
    
    public enum ImgDir {VERTICAL, HORIZONTAL};
    
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

    
    
    //TODO: Improve Performance of buildBoxes method.
    
    LinkedList<BoxPosition> buildBoxes(boolean findDarkest) 
    {
        LinkedList<NumberPairs> vertPairs = vertSums.buildPairs(findDarkest);
        LinkedList<NumberPairs>  horPairs = horSums.buildPairs(findDarkest);
        
        LinkedList<BoxPosition> newBoxes = new LinkedList<>();
        
        for(NumberPairs curVert: vertPairs)
        {
            for(NumberPairs curHoriz: horPairs)
            {
                newBoxes.add(new BoxPosition(curVert, curHoriz));
                
            }
        }
        
        
        
        return newBoxes;
        
    }
    
    
    
    LinkedList<BoxPosition> buildBoxes(boolean doVertical, boolean findDarkest)
    {
        LinkedList<BoxPosition> newBoxes = new LinkedList<>();
        
        if(doVertical)
        {
            LinkedList<NumberPairs> horizPairs = horSums.buildPairs(findDarkest);
            for(NumberPairs curHoriz: horizPairs)
            {
                newBoxes.add(new BoxPosition(myPosition, curHoriz, doVertical));
            }
            
        }
        else
        {
            LinkedList<NumberPairs> vertPairs = vertSums.buildPairs(findDarkest);
            for(NumberPairs curVert: vertPairs)
            {
                newBoxes.add(new BoxPosition(myPosition, curVert, doVertical));
            }
            
        }
    
    
        return newBoxes;
    
    }
    

 
    private class Row
    {
        private final int[] sortedRow;
        private final int[] refRow;
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
            String outPut = "# Min: " + min + " Med: " + median + " Mean: " + mean + " Max: " + max + " Std: " + stdDev; 
            outPut += "\n\n";
            for(int x = 0; x < refRow.length ; ++x)
            {
                outPut += x + " " + refRow[x] + "\n";
            }
            return outPut;
        }
        
        private LinkedList<NumberPairs> buildPairs(boolean searchForWhite)
        {
            int upperLimit = median + (int) stdDev; //Finds Black Characters
            int lowerLimit = median - (int) stdDev; //Finds White Characters
            
            LinkedList<NumberPairs> nP = new LinkedList<>();
            
            int boxStart = 0;
            
            boolean wasInBox = false;
            
            int curVal;
            
            for(int x = 0; x < refRow.length; ++x)
            {
                curVal = refRow[x];
                
                if(testIfOutsideLimits(curVal, searchForWhite, lowerLimit, upperLimit)) //I am outside the box
                {
                    if(wasInBox) //I am leaving the box
                    {
                        nP.add(new NumberPairs(boxStart, x));
                        wasInBox = false;
                    }
                }
                else //I am inside the box
                {
                    if(!wasInBox) //I am entering the box
                    {
                        boxStart = x;
                        wasInBox = true;
                    }
                }
            }
            
            //Catch edge-case where the box is against the edge of paper
            
            if(wasInBox)
            {
                nP.add(new NumberPairs(boxStart, refRow.length - 1));
            }
            
            
            return nP;
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
    
    
        private static boolean testIfOutsideLimits(int sampleVal, boolean useLowerLimit, int lowLimit, int highLimit)
        {
             if(!useLowerLimit)
            {
                return sampleVal >= highLimit;
                
            }
            else
            {
                return sampleVal <= lowLimit;
            }
        }

    
    private static double getStdDeviation(int[] inArray, int mean)
    {
        double sumVar = 0;
        
        for(int x = 0; x < inArray.length; ++x)
        {
            sumVar +=  pow(inArray[x] - mean, 2);
        }
        
        double variance = sumVar / inArray.length;
        
        return sqrt(variance);
    }
   
   
    
    
    
    
}