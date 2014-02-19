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

import ewucalligraphy.gui.DisplayWindow;
import static java.awt.color.ColorSpace.TYPE_GRAY;
import static java.awt.color.ColorSpace.TYPE_RGB;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */
public final class WholeImage {
    
    private BufferedImage myImage;
    private BufferedImage filteredImage;
    private int[][][] imG;
    private int[][][] filteredImg;
    private Segment[][] imgCharacters;
    private int imgHeight;
    private int imgWidth;
    private int imgDepth;
    private String myName;
    
    public WholeImage(BufferedImage inImage, String imageName)
    {
	myImage = inImage;
	myName = imageName;
        buildIntArray();
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
    
    
    public void segmentImage()
    {
        
    }
    
    public void buildIntArray()
    {
	//NOTE: There seem to be just 1 tile for jpg < 8Mb
	Raster myTile = myImage.getTile(0, 0);
	ColorModel myColorModel = myImage.getColorModel();
	
	assert(myColorModel.hasAlpha()); //TODO: Handle or accomodate image
        
	switch(myColorModel.getColorSpace().getType())
	{
	    case TYPE_GRAY:
		buildModel(myTile, 1); //1 color channel
		break;
	    case TYPE_RGB:
		buildModel(myTile, 3); //3 color channels
		checkFixGrayScale();
		break;
	    default:
		System.out.println("Unexpected ColorSpace Detected: " +myColorModel.getColorSpace().getType());
		break;
	}

		
    }

    private void buildModel(Raster myTile, int depth) {
	imgHeight = myTile.getHeight();
	imgWidth  = myTile.getWidth();
	imgDepth  = depth;
	
	imG = new int[imgHeight][imgWidth][imgDepth];
	
	int[] myPixel = new int[imgDepth];
	
	
	int[] intArray = null;
	

	
	//0 = darkest 255 = lightest
	
	for(int y = 0; y < imgHeight; ++y)
	{
	    for(int x = 0; x < imgWidth; ++x)
	    {
		myPixel = myTile.getPixel(x, y, intArray);

		for(int z = 0; z < imgDepth; ++z)
		{
		    imG[y][x][z] = myPixel[z];
		}
	    }
	}
    }

    private void checkFixGrayScale()
    {
	boolean isGray = true;
	int firstVal = 0;
	
	for(int x = 0; x < imgHeight; ++x)
	{
	    for(int y = 0; y < imgWidth; ++y)
	    {
		for(int z = 0; z < imgDepth; ++z)
		{
		    if(z == 0)
		    {
			firstVal = imG[x][y][z];
		    }
		    else
		    {
			if(firstVal != imG[x][y][z])
			{
			    isGray = false;
			}
		    }
		}
	    }
	}
	if(isGray)
	{
	    turnImgGray();
	}
    }

    private void turnImgGray() {
	int[][][] imgNew = new int[imgHeight][imgWidth][1];
	
	for(int x = 0; x < imgHeight; ++x)
	{
	    for(int y = 0; y < imgWidth; ++y)
	    {
		imgNew[x][y][0] = imG[x][y][0];
	    }
	}
	
	imG = imgNew;
        buildGrayImage();
    }
    
    private void buildGrayImage()
    {
        Raster imageRaster = myImage.getData();
        
        DataBuffer imageBuffer = imageRaster.getDataBuffer();
        
        System.out.println(imageBuffer.getSize() + " : " + imgWidth * imgHeight);
        
        BufferedImage grayImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster rstr = (WritableRaster) grayImg.getData();
        
        //Now we need to convert the stupid pixel array
        int[] flatImg = new int[imgWidth * imgHeight];
        int x1 = 0;
        
        for(int x = 0; x < imG.length; ++x)
        {
            for(int y = 0; y < imG[0].length; ++y)
            {
               flatImg[x1] = imG[x][y][0];
               x1++;
            }
        }
        
        
        
        rstr.setPixels(0, 0, imgWidth, imgHeight, flatImg);
        grayImg.setData(rstr);
        
        DisplayWindow newWindow = new DisplayWindow();
        newWindow.setImage(grayImg);
        newWindow.setVisible(true);
    }
}