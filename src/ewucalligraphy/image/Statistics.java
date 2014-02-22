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

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class Statistics
{
    private Row[][] vertRow, horRow;
    
    
    
    //NOte, this is a bit inefficiet, because I create a lot of
    //temporary copies of parts of the image.
    
    public Statistics(int[][][] imG)
    {
        int iDepth = imG.length;
        int iHeight = imG[0].length;
        int iWidth = imG[0][0].length;
        
        
        vertRow = new Row[iDepth][iWidth];
        horRow  = new Row[iDepth][iHeight];
        
        int y, z;
        int[] tempRow, tempColumn;
        
        tempRow = new int[iWidth];
        tempColumn = new int[iHeight];
        
        
        for(int x = 0; x < iDepth; ++x)
        {

            for(y = 0; y < iHeight; ++y)
            {
                for(z = 0; z < iWidth; ++z)
                {
                    tempRow[z] = imG[x][y][z];
                }
                horRow[x][y] = new Row(tempRow);
              
            }
           
            for(y = 0; y < iWidth; ++y)
            {
                for(z = 0; z < iHeight; ++z)
                {
                    tempColumn[z] = imG[x][z][y];
                }
                vertRow[x][y] = new Row(tempRow);
            }
         
            
            
        }
    }
    
    private class Row
    {
        private int[] sourceRow, sortedRow, lookUpTable;
        
        public Row(int[] inRow)
        {
            int x, rowLength;
            
            rowLength = inRow.length;
            
            sourceRow = new int[rowLength];
            
            for(x = 0;  x < rowLength; ++x)
            {
                sourceRow[x] = inRow[x];
            }
            
            
        }
    }
}
