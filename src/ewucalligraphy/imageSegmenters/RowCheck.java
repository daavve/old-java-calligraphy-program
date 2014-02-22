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


package ewucalligraphy.imageSegmenters;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class RowCheck
{
    public static void rowSums(int[][][] image)
    {
       int y, sum;
       for(int x = 0; x < image.length; ++x)
       {
           sum = 0;
           for(y = 0; y < image[x].length; ++y)
           {
               sum += image[0][x][y];
           }
           System.out.println(sum);
       }
    }
    
    public static void columnSums(int[][][] image)
    {
        int x, sum;
        
        for(int y = 0; y < image[0].length; ++y)
        {
            sum = 0;
            for(x = 0; x < image.length; ++x)
            {
                sum += image[0][x][y];
            }
            System.out.println(sum);
        }
    }
}
