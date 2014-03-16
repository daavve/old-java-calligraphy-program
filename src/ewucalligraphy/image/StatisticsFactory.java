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
 * @author davidm@eagles.ewu.edu
 */


public class StatisticsFactory
{
    public static Statistics[][] buildStatsGrid(int[][] imG, int[] xLinesI, int[] yLinesI)
    {
       
        int iWidth = xLinesI.length;
        int iHeight = yLinesI.length;
        
        int[] xLines = addEdges(xLinesI, imG.length - 1);
        int[] yLines = addEdges(yLinesI, imG[0].length - 1);
        
        
         
        Statistics[][] statsGrid = new Statistics[iWidth + 1][iHeight + 1];
        
        for(int x = 0; x <= iWidth; ++x)
        {
            for(int y = 0; y <= iHeight; ++y)
            {
                statsGrid[x][y] = new Statistics(imG, xLines[x], yLines[y], xLines[x + 1], yLines[y + 1]);
            }
        }
        
        
        return statsGrid;
    }
    
    private static int[] addEdges(int[] inInt, int farEdge)
    {
        int newIntSize = inInt.length + 2;
        int[] newInt = new int[newIntSize];
        
        newInt[0] = 0;
        newInt[newIntSize - 1] = farEdge;
        
        for(int x = 0; x < inInt.length; ++x)
        {
            newInt[x + 1] = inInt[x];
        }
               
        return newInt;
    }
    
    
}
