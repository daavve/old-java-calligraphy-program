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
import static java.awt.Color.CYAN;
import static java.awt.Color.MAGENTA;
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
        int[] lineX = new int[1];
        int[] lineY = new int[1];
        lineX[0] = imGStats[0].GetSmallestMedian(ImgDir.VERTICAL);
        lineY[0] = imGStats[0].GetSmallestMedian(ImgDir.HORIZONTAL);
        
        Statistics[][] quadStats = StatisticsFactory.buildStatsGrid(imG[0], lineX, lineY);
        
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

        ImgQuadrant darkestQuadrant = getDarkestQuadrant(minMedX, minMedY);
        
        int[] boxX = new int[2];
        int[] boxY = new int[2];
        
        ImgBox mainBox = null;
        
        switch(darkestQuadrant)
        {
            case I:
                int right = lineX[0];
                int bottom = lineY[0];
                int left = quadStats[0][0].growTillTargetMedian(LEFT, maxMedian, true);
                int top = quadStats[0][0].growTillTargetMedian(TOP, maxMedian, true);
                mainBox = new ImgBox(top, bottom, left, right);
                break;
            case II:
                left = lineX[0];
                bottom = lineY[0];
                right = quadStats[1][0].growTillTargetMedian(RIGHT, maxMedian, true);
                top = quadStats[1][0].growTillTargetMedian(TOP, maxMedian, true);
                mainBox = new ImgBox(top, bottom, left, right);
                break;
            case III:
                left = lineX[0];
                top = lineY[0];
                right = quadStats[1][1].growTillTargetMedian(RIGHT, maxMedian, true);
                bottom = quadStats[1][1].growTillTargetMedian(BOTTOM, maxMedian, true);
                mainBox = new ImgBox(top, bottom, left, right);
                break;
            case IV:
                right = lineX[0];
                top = lineY[0];
                left = quadStats[0][1].growTillTargetMedian(LEFT, maxMedian, true);
                bottom = quadStats[0][1].growTillTargetMedian(BOTTOM, maxMedian, true);
                mainBox = new ImgBox(top, bottom, left, right);
                break;
        }
       

        
        
        Statistics[][] boxStats = StatisticsFactory.buildStatsGrid(imG[0], mainBox);
        
        for(int y = 0; y < 3; ++y)
        {
            for(int x = 0; x < 3; ++x)
            {
                curMedian = boxStats[x][y].getMedian();
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

        
       int newTop    = boxStats[1][0].growTillTargetMedian(TOP, maxMedian, false);
       int newLeft   = boxStats[0][1].growTillTargetMedian(LEFT, maxMedian, false);
       int newRight  = boxStats[2][1].growTillTargetMedian(RIGHT, maxMedian, false);
       int newBottom = boxStats[1][2].growTillTargetMedian(BOTTOM,maxMedian, false);
        
        addHorizLine(disWindow, newTop);
        addHorizLine(disWindow, newBottom);
        addVertLine(disWindow, newLeft);
        addVertLine(disWindow, newRight);
                
//      exportForGnuPlot();
    }
    
        private ImgQuadrant getDarkestQuadrant(int minMedX, int minMedY)
        {
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
    return darkestQuadrant;
    }
    
    
    private void addVertLine(DisplayWindow disWindow, int offSet)
    {
        disWindow.addLine(new Line(offSet, 0, offSet, imgHeight, MAGENTA));
    }
    
    private void addHorizLine(DisplayWindow disWindow, int offSet)
    {
        disWindow.addLine(new Line(0, offSet, imgWidth, offSet, CYAN));
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