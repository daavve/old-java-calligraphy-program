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
import static ewucalligraphy.image.ImgDir.BOTTOM;
import static ewucalligraphy.image.ImgDir.LEFT;
import static ewucalligraphy.image.ImgDir.RIGHT;
import static ewucalligraphy.image.ImgDir.TOP;
import static ewucalligraphy.image.ImgQuadrant.I;
import static ewucalligraphy.image.ImgQuadrant.II;
import static ewucalligraphy.image.ImgQuadrant.III;
import static ewucalligraphy.image.ImgQuadrant.IV;
import ewucalligraphy.testing.FileIO;
import static ewucalligraphy.testing.FileIO.saveToFile;
import java.awt.Color;
import static java.awt.Color.BLUE;
import static java.awt.Color.CYAN;
import static java.awt.Color.MAGENTA;
import static java.awt.Color.RED;
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
        
        
        //TODO:  Establish tie-breaker for when two or more medians match
        
        int maxMedian = 0;
        int minMedian = 255;
        int minMedX = 0;
        int minMedY = 0;
        int curMedian;
        
        for(int x = 0; x < 2; ++x)
        {
            for(int y = 0; y < 2; ++y)
            {
                curMedian = quadStats[x][y].getMedian();
                System.out.print(curMedian + " : ");

                if(maxMedian < curMedian)
                {
                    maxMedian = curMedian;

                }
                if(minMedian > curMedian)
                {
                    minMedian = curMedian;
                    minMedX = x; minMedY = y;
                }
            
            }
            System.out.println();
        }
        
        System.out.println("Max: " + maxMedian + " Min: " + minMedian);

        ImgQuadrant darkestQuadrant;
        
        if(minMedX == 0)
        {
            if(minMedY == 0)
            {
                darkestQuadrant = I;
            }
            else
            {
                darkestQuadrant = II;
            }
        }
        else
        {
            if(minMedY == 0)
            {
                darkestQuadrant = IV;
            }
            else
            {
                darkestQuadrant = III;
            }
        }
        
        int left, right, top, bottom;

        
        switch(darkestQuadrant)
        {
            case III:
                System.out.println("Growing to Quad: III");
                left   = vertHoriz[0];
                top    = vertHoriz[1];
                right  = quadStats[1][1].growTillTargetMedian(RIGHT, maxMedian, 10);
                addVertLine(disWindow, right);
                bottom = quadStats[1][1].growTillTargetMedian(BOTTOM, maxMedian, 10);
                addHorizLine(disWindow, bottom);
                break;
            case IV:
                System.out.println("Growing to Quad: IV");
                right  = vertHoriz[0];
                top    = vertHoriz[1];
                left   = quadStats[0][1].growTillTargetMedian(LEFT, maxMedian, 10);
                bottom = quadStats[0][1].growTillTargetMedian(BOTTOM, maxMedian, 10);
                addHorizLine(disWindow, bottom);
                addVertLine(disWindow, left);
                break;
            case I:
                System.out.println("Growing to Quad: I");
                right   = vertHoriz[0];
                bottom  = vertHoriz[1];
                left    = quadStats[0][0].growTillTargetMedian(LEFT, maxMedian, 10);
                top     = quadStats[0][0].growTillTargetMedian(TOP, maxMedian, 10);
                addHorizLine(disWindow, top);
                addVertLine(disWindow, left);
                break;
            case II:
                System.out.println("Growing to Quad: II");
                left   = vertHoriz[0];
                bottom = vertHoriz[1];
                right  = quadStats[1][0].growTillTargetMedian(RIGHT, maxMedian, 10);
                top    = quadStats[1][0].growTillTargetMedian(TOP, maxMedian, 10);
                addHorizLine(disWindow, top);
                addVertLine(disWindow, right);
                break;
        }
       
        
//      exportForGnuPlot();
    }
    
    
    private void addVertLine(DisplayWindow disWindow, int offSet)
    {
        disWindow.addLine(new Line(offSet, 0, offSet, imgHeight, RED));
    }
    
    private void addHorizLine(DisplayWindow disWindow, int offSet)
    {
        disWindow.addLine(new Line(0, offSet, imgWidth, offSet, BLUE));
    }
    
    private void add2Lines(DisplayWindow disWindow, int[] vertHoriz)
    {
        assert(vertHoriz.length == 2);
        
        disWindow.addLine(new Line(vertHoriz[0], 0, vertHoriz[0], imgHeight, MAGENTA));
        disWindow.addLine(new Line(0, vertHoriz[1], imgWidth, vertHoriz[1], CYAN));
    }
    
    
    private void add4Lines(DisplayWindow disWindow, int[] topDownRightLeft)
    {
        assert(topDownRightLeft.length == 4);
        disWindow.addLine(new Line(0, topDownRightLeft[0], imgWidth, topDownRightLeft[0], MAGENTA));
        disWindow.addLine(new Line(0, topDownRightLeft[1], imgWidth, topDownRightLeft[1], RED));
        disWindow.addLine(new Line(topDownRightLeft[2], 0, topDownRightLeft[2], imgHeight, CYAN));
        disWindow.addLine(new Line(topDownRightLeft[3], 0, topDownRightLeft[3], imgHeight, BLUE));
        
        disWindow.repaint();
    }
    
    
        private void exportForGnuPlot()
        {

        String data, fileName;
        
        fileName = myName + "-Hz.dat";
        
        data = imGStats[0].getGnuPlotHorizontalRows();
        saveToFile(data, fileName);
        
        fileName = myName + "-Vt.dat";
        
        data = imGStats[0].getGnuPlotVerticalRows();
        saveToFile(data, fileName);
        }
        
    
    public void buildIntArray()
    {
	//NOTE: There seem to be just 1 tile for jpg's < 8Mb
	Raster myTile = myImage.getTile(0, 0);
	ColorModel myColorModel = myImage.getColorModel();
	
	assert(myColorModel.hasAlpha());
        
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