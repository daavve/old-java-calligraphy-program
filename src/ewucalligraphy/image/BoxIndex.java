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

import static ewucalligraphy.image.BoxState.highlighted;
import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


class BoxIndex
{
   private final ImgBox[][] boxArray;
   
   private ImgBox lastHighlight;
   
   //TODO: Accomodate overlapping boxes
   
   BoxIndex(LinkedList<ImgBox> boxes, int imgWidth, int imgHeight)
   {
       boxArray = new ImgBox[imgWidth][imgHeight];
       int x, y;
       BoxPosition curBoxPos;
       
       for(ImgBox curBox : boxes)
       {
           curBoxPos = curBox.getPosition();
           
           for(x = curBoxPos.getLeft(); x < curBoxPos.getRight(); ++x)
           {
               for(y = curBoxPos.getTop(); y < curBoxPos.getBottom(); ++y)
               {
                   boxArray[x][y] = curBox;
               }
           }
       }
   }
   
   
   LinkedList<ImgBox> searchBoxes(Point pointerLoc)
   {
       LinkedList<ImgBox> changedBoxes = new LinkedList<>();
       
       ImgBox overBox = boxArray[pointerLoc.x][pointerLoc.y];
       
       
       
       if(overBox != null)
       {
           overBox.setState(highlighted);
           changedBoxes.add(overBox);
       }
       
       
       return changedBoxes;
   }
   
   
}
