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
 * @author dmcinnis
 */




public class ImgBox {
    private int edgeTop, edgeBottom, edgeLeft, edgeRight;
    
    public ImgBox(int top, int  bottom, int left, int right)
    {
        edgeTop = top;
        edgeBottom = bottom;
        edgeLeft = left;
        edgeRight = right;
    }
    
    public int getTop()
    {
        return edgeTop;
    }
    
    public int getBottom()
    {
        return edgeBottom;
    }
    
    public int getLeft()
    {
        return edgeLeft;
    }
    
    public int getRight()
    {
        return edgeRight;
    }
    
    public void setTop(int top)
    {
        edgeTop = top;
    }
    
    public void setBottom(int bottom)
    {
        edgeBottom = bottom;
    }
    
    public void setLeft(int left)
    {
        edgeLeft = left;
    }
    
    public void setRight(int right)
    {
        edgeRight = right;
    }
}