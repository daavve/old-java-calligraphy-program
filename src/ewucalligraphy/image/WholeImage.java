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
import ewucalligraphy.gui.Line;
import ewucalligraphy.testing.FileIO;
import java.awt.Color;
import static java.awt.color.ColorSpace.TYPE_GRAY;
import static java.awt.color.ColorSpace.TYPE_RGB;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */
public final class WholeImage {
    
    private BufferedImage myImage;
    private int[] [][] imG;
    private Statistics imGStats;
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

    public void segmentImage(DisplayWindow disWindow)
    {
        int topEdge, bottomEdge, rightEdge, leftEdge, imgMedian, curMedian;
        
        imgMedian = imGStats.getMedian(0);
      
        topEdge = imGStats.getEdgeMedianUnderTarget(ImgDir.TOP, 0, imgMedian);
        disWindow.addLine(new Line(0, topEdge, imgWidth, topEdge, Color.MAGENTA));
        
        bottomEdge = imGStats.getEdgeMedianUnderTarget(ImgDir.BOTTOM, 0, imgMedian);
        disWindow.addLine(new Line(0, bottomEdge, imgWidth, bottomEdge, Color.RED));
        
        rightEdge = imGStats.getEdgeMedianUnderTarget(ImgDir.RIGHT, 0, imgMedian);
        disWindow.addLine(new Line(rightEdge, 0, rightEdge, imgHeight, Color.CYAN));
        
        leftEdge = imGStats.getEdgeMedianUnderTarget(ImgDir.LEFT, 0, imgMedian);
        disWindow.addLine(new Line(leftEdge, 0, leftEdge, imgHeight, Color.BLUE));
        
        

        
        disWindow.repaint();
        
        System.out.println("\nTOP: " + topEdge + ", Bottom: " + bottomEdge +
                            ", Right: " + rightEdge + ", Left: " + leftEdge + 
                            "\n Height: " + imgHeight + ", Width: " + imgWidth);

/*
        String data, fileName;
        
        fileName = myName + "-Hz.dat";
        
        data = imGStats.getGnuPlotHorizontalRows(0);
        FileIO.saveToFile(data, fileName);
        
        fileName = myName + "-Vt.dat";
        
        data = imGStats.getGnuPlotVerticalRows(0);
        FileIO.saveToFile(data, fileName);
*/
        
    }
    
    public void buildIntArray()
    {
	//NOTE: There seem to be just 1 tile for jpg's < 8Mb
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
	
        imG = new int[imgDepth] [imgWidth][imgHeight];
	
	int[] myPixel = new int[imgDepth];
	
	
	int[] intArray = null;
	
	//0 = darkest 255 = lightest
	
	for(int x = 0; x < imgWidth; ++x)
	{
	    for(int y = 0; y < imgHeight; ++y)
	    {
		myPixel = myTile.getPixel(x, y, intArray); //Slow, but flexible

		for(int z = 0; z < imgDepth; ++z)
		{
                    imG[z] [x][y] = myPixel[z];
		}
	    }
	}
        imGStats = new Statistics(imG);
    }
}