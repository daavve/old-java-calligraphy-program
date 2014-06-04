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

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


class BoxIndex
{
   private BoxPosition[][] boxArray;
   
   //TODO: Accomodate overlapping boxes
   
   BoxIndex(LinkedList<BoxPosition> boxes, Rectangle boxRec)
   {
       boxArray = new BoxPosition[boxRec.width][boxRec.height];
       int x, y;
       
       for(BoxPosition curBox : boxes)
       {
           for(x = curBox.getLeft(); x < curBox.getRight(); ++x)
           {
               for(y = curBox.getTop(); y < curBox.getBottom(); ++y)
               {
                   boxArray[x][y] = curBox;
               }
           }
       }
   }
   
   BoxPosition getBox(Point searchPos)
   {
       return boxArray[searchPos.x][searchPos.y];
   }
   
}
