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
        int[] vertHoriz = new int[2];

        vertHoriz[0] = imGStats[0].GetSmallestMedian(ImgDir.VERTICAL);
        vertHoriz[1] = imGStats[0].GetSmallestMedian(ImgDir.HORIZONTAL);
        
        add2Lines(disWindow, vertHoriz);
        
        Statistics[][] quadStats = new Statistics[2][2];
        
        
        quadStats[0][0] = new Statistics(imG[0], 0, 0, vertHoriz[0], vertHoriz[1]);
        quadStats[1][0] = new Statistics(imG[0], vertHoriz[0], 0, imgWidth, vertHoriz[1]);
        quadStats[0][1] = new Statistics(imG[0], 0, vertHoriz[1], vertHoriz[0], imgHeight);
        quadStats[1][1] = new Statistics(imG[0], vertHoriz[0], vertHoriz[1], imgWidth, imgHeight);
        
        
        int maxMedian = 0;
        
        for(int x = 0; x < 2; ++x)
        {
            for(int y = 0; y < 2; ++y)
            {
                System.out.print(quadStats[x][y].getMedian() + " : ");
                if(maxMedian < quadStats[x][y].getMedian())
                {
                    maxMedian = quadStats[x][y].getMedian();
                }
            
            }
            System.out.println();
        }
        
        System.out.println("---------------" + maxMedian);

        
        
        int topLeft  = quadStats[0][0].growTillTargetMedian(ImgDir.BOTTOM, maxMedian , 10);
        int topRight = quadStats[1][0].growTillTargetMedian(ImgDir.BOTTOM, maxMedian, 10);
       
       int boxTop = Math.min(topLeft, topRight);
        
       int bottomLeft  = quadStats[0][1].growTillTargetMedian(ImgDir.TOP, maxMedian, 10);
       int bottomRight = quadStats[1][1].growTillTargetMedian(ImgDir.TOP, maxMedian, 10);
       
       int boxBotom = Math.max(bottomLeft, bottomRight);
       
       disWindow.addLine(new Line(0, bottomLeft, vertHoriz[0], bottomLeft, Color.BLUE));
       disWindow.addLine(new Line(vertHoriz[0], bottomRight, imgWidth, bottomRight, Color.BLUE));
       
       int leftTop   = quadStats[0][0].growTillTargetMedian(ImgDir.LEFT, maxMedian, 10);
       int leftBotom = quadStats[0][1].growTillTargetMedian(ImgDir.LEFT, maxMedian, 10);
       
       int boxLeft = Math.max(leftTop, leftBotom);
       
       disWindow.addLine(new Line(leftTop, 0, leftTop, vertHoriz[1], Color.RED));
       disWindow.addLine(new Line(leftBotom, vertHoriz[1], leftBotom, imgHeight, Color.RED));
       
       int rightTop    = quadStats[1][0].growTillTargetMedian(ImgDir.RIGHT, maxMedian, 10);
       int rightBottom = quadStats[1][1].growTillTargetMedian(ImgDir.RIGHT, maxMedian, 10);
       
       int boxRight = Math.min(rightTop, rightBottom);
       
       disWindow.addLine(new Line(rightTop, 0, rightTop, vertHoriz[1], Color.red));
       disWindow.addLine(new Line(rightBottom, vertHoriz[1], rightBottom, imgHeight, Color.RED));

        
        
       
       
       
        
//      exportForGnuPlot();
    }
    
    
    private void addVertLine(DisplayWindow disWindow, int offSet)
    {
        disWindow.addLine(new Line(offSet, 0, offSet, imgHeight, Color.MAGENTA));
    }
    
    private void addHorizLine(DisplayWindow disWindow, int offSet)
    {
        disWindow.addLine(new Line(0, offSet, imgWidth, offSet, Color.CYAN));
    }
    
    private void add2Lines(DisplayWindow disWindow, int[] vertHoriz)
    {
        assert(vertHoriz.length == 2);
        
        disWindow.addLine(new Line(vertHoriz[0], 0, vertHoriz[0], imgHeight, Color.MAGENTA));
        disWindow.addLine(new Line(0, vertHoriz[1], imgWidth, vertHoriz[1], Color.CYAN));
    }
    
    
    private void add4Lines(DisplayWindow disWindow, int[] topDownRightLeft)
    {
        assert(topDownRightLeft.length == 4);
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