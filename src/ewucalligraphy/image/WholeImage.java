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
    private Statistics[]  imGStats;
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
        int curMedian = imGStats[0].getMedian();
        int [] topDownRightLeft;
        
        boolean gotAllEdges  = false;

        while(!gotAllEdges)
        {
            topDownRightLeft = getEdges(curMedian, 0);
            add4Lines(disWindow, topDownRightLeft);

            gotAllEdges = (topDownRightLeft[0] != -1 && topDownRightLeft[1] != -1 && topDownRightLeft[2] != -1 && topDownRightLeft[3] != -1);
            --curMedian;
        }
        
        
        
        
        
//      exportForGnuPlot();
    }
    
    
    public int[] getEdges(int targetMedian, int rVal)
    {
        int[] topDownRightLeft = new int[4];
        
        topDownRightLeft[0] = imGStats[rVal].getEdgeMedianUnderTarget(ImgDir.TOP, targetMedian);
        topDownRightLeft[1] = imGStats[rVal].getEdgeMedianUnderTarget(ImgDir.BOTTOM, targetMedian);
        topDownRightLeft[2] = imGStats[rVal].getEdgeMedianUnderTarget(ImgDir.RIGHT, targetMedian);
        topDownRightLeft[3] = imGStats[rVal].getEdgeMedianUnderTarget(ImgDir.LEFT, targetMedian);
        
        return topDownRightLeft;
    }
    
    private void add4Lines(DisplayWindow disWindow, int[] topDownRightLeft)
    {
        disWindow.addLine(new Line(0, topDownRightLeft[0], imgWidth, topDownRightLeft[0], Color.MAGENTA));
        disWindow.addLine(new Line(0, topDownRightLeft[1], imgWidth, topDownRightLeft[1], Color.RED));
        disWindow.addLine(new Line(topDownRightLeft[2], 0, topDownRightLeft[2], imgHeight, Color.CYAN));
        disWindow.addLine(new Line(topDownRightLeft[3], 0, topDownRightLeft[3], imgHeight, Color.BLUE));
        
        disWindow.repaint();
    }
    
    
        private void exportForGnuPlot()
        {

        String data, fileName;
        
        fileName = myName + "-Hz.dat";
        
        data = imGStats[0].getGnuPlotHorizontalRows();
        FileIO.saveToFile(data, fileName);
        
        fileName = myName + "-Vt.dat";
        
        data = imGStats[0].getGnuPlotVerticalRows();
        FileIO.saveToFile(data, fileName);
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
        imGStats = new Statistics[imgDepth];
	
        imG = new int[imgDepth] [imgWidth][imgHeight];
	
	int[] myPixel = new int[imgDepth];
        int x, y, z;
	
	
	int[] intArray = null;
	
	//0 = darkest 255 = lightest
	
	for(x = 0; x < imgWidth; ++x)
	{
	    for(y = 0; y < imgHeight; ++y)
	    {
		myPixel = myTile.getPixel(x, y, intArray); //Slow, but flexible

		for(z = 0; z < imgDepth; ++z)
		{
                    imG[z] [x][y] = myPixel[z];
		}
	    }
	}
        
        for(z = 0; z < imgDepth; ++z)
        {
            imGStats[z] = new Statistics(imG[z]);
        }

    }
}