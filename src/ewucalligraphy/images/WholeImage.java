/*
 * Copyright (C) 2013 David McInnis
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

package ewucalligraphy.images;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 *
 * @author David McInnis
 */
public class WholeImage {
    
    private BufferedImage myImage;
    private String myName;
    
    public WholeImage(BufferedImage inImage, String imageName)
    {
	myImage = inImage;
	myName = imageName;
    }
    
    public BufferedImage getImage()
    {
	return myImage;
    }
    
    public String getName()
    {
	return myName;
    }
    
    public void setImage(BufferedImage inImage)
    {
	myImage = inImage;
    }
    
    public void setName(String newName)
    {
	myName = newName;
    }
    
//Now the fun really begins
//WARNING: this assumes pixels are made of 3 component colors
    public void segmentImage()
    {
	//NOTE: There seem to be just 1 tile for jpg < 8Mb
	Raster myTile = myImage.getTile(0, 0);
	
	int tileHeight = myTile.getHeight();
	int tileWidth  = myTile.getWidth();
	
	
	int[] intArray = null;
	
	int[][][] img = new int [tileWidth][tileHeight][];
	
	//0 = darkest 255 = lightest
	
	for(int y = 0; y < tileHeight; ++y)
	{
	    for(int x = 0; x < tileWidth; ++x)
	    {
		img[x][y] = myTile.getPixel(x, y, intArray);
	    }
	}

    }
}