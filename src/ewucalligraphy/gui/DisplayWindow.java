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


package ewucalligraphy.gui;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class DisplayWindow extends javax.swing.JFrame
{
    private BufferedImage fileImage;
    
    private final int topOffset  = 30;
    private final int brlOffset = 10; //Botom, Right & Left
    private final int mouseInterval = 1000; //Interval in ms
    
    private final int[] imageSizeScaled = new int[2];
    
    private final int newWindowSize[] = new int[2];

    private boolean drawed = false;
    
    private final LinkedList<Line> myLines = new LinkedList<>();
    
    private Rectangle scaledImgRect = new Rectangle();
    private Double imgHeightWidthRatio, imgWidthHeightRatio;
    
        ScheduledFuture cursorDetectorThread;
        
    public DisplayWindow(BufferedImage iFileImage)
    {
        setImage(iFileImage);
        initComponents();
        
        ScheduledThreadPoolExecutor stp = new ScheduledThreadPoolExecutor(1);
        MouseWatcher mouseWatch = new MouseWatcher(this);
        cursorDetectorThread = stp.scheduleAtFixedRate(mouseWatch, mouseInterval, mouseInterval, TimeUnit.MILLISECONDS);
    }
    
    public void setImage(BufferedImage iFileImage)
    {
        fileImage = iFileImage;
      
        double imgWidth = (double) fileImage.getWidth();
        double imgHeight = (double) fileImage.getHeight();
        
        imgHeightWidthRatio = imgWidth / imgHeight;
        imgWidthHeightRatio = imgHeight / imgWidth;
    }
    

    
    	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
                
                drawImage(g);
                
                drawOverImage(g);
        }
        
        
        private void drawImage(Graphics g)
        {
               //This part scales the image to fit within the window
            
		if(fileImage != null)
		{
                        newWindowSize[0] = this.getWidth() - brlOffset * 2;
			newWindowSize[1] = this.getHeight() - topOffset - brlOffset;
			
                        //Inefficient but very intuative       
			double windowRatio = ((double)newWindowSize[0]) / ((double)newWindowSize[1]);

			int newImageSizeWidth, newImageSizeLength;
			newImageSizeWidth = 0; newImageSizeLength = 0;

			if(windowRatio > imgHeightWidthRatio) //window not long enough
			{
			
			newImageSizeLength = newWindowSize[1];
			newImageSizeWidth = (int) (newImageSizeLength * imgHeightWidthRatio);
			}
                        else //window not wide enough
			{
			newImageSizeWidth = newWindowSize[0];
			newImageSizeLength = (int) (newImageSizeWidth * imgWidthHeightRatio);
			}

			if((newImageSizeWidth > 0 && newImageSizeLength > 0) || !drawed)
			{
                            imageSizeScaled[0] = newImageSizeWidth;
                            imageSizeScaled[1] = newImageSizeLength;
                            
                                                       
                            Image scaledImage = fileImage.getScaledInstance(newImageSizeWidth, newImageSizeLength, Image.SCALE_FAST);
                            drawed = g.drawImage(scaledImage, brlOffset, topOffset, newImageSizeWidth, newImageSizeLength, null);
                        }
                }
        }

        public void mouseWatch(Point mouseLoc) //TODO: Modify to handle rectangles
        {
            int imageTopCornerX = this.getX() + brlOffset;
            int imageTopCornerY = this.getY() + topOffset;
            
            int imageBottomCornerX = imageTopCornerX + imageSizeScaled[0];
            int imageBottomCornerY = imageTopCornerY + imageSizeScaled[1];
            
            if(mouseLoc.x < imageBottomCornerX &&
               mouseLoc.x > imageTopCornerX && 
               mouseLoc.y > imageTopCornerY && 
               mouseLoc.y < imageBottomCornerY)
            {
                System.out.println("*"); //On top of image
            }
            else
            {
                System.out.println(".");//Outside image
            }
        }
        
        
        
        private void drawOverImage(Graphics g) //This function draws stuff over the actual image
        {
            drawLines(g);
        }
        
        private void drawLines(Graphics g)
        {
            int[] iStart, oStart, iEnd, oEnd;
            
            for(Line curLine : myLines)
            {
                iStart = curLine.getStart();
                iEnd   = curLine.getEnd();
                
                oStart = transformCoordinates(iStart);
                oEnd   = transformCoordinates(iEnd);
                
                g.setColor(curLine.getColor());
                
                
                g.drawLine(oStart[0], oStart[1], oEnd[0], oEnd[1]);
                
            }
        }
        
        
        public int[] transformCoordinates(int[] XYin)
        {
            int[] XYout = new int[2];
           
            XYout[0] = brlOffset + (int) (XYin[0] * imgHeightWidthRatio);
                    
            XYout[1] = topOffset + (int) (XYin[1] * imgWidthHeightRatio);
            
           
            return XYout;
        }
        
        public void addLine(Line newLine)
        {
            myLines.add(newLine);
        }
        
        public void clearLines()
        {
            myLines.clear();
        }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        setMinimumSize(new java.awt.Dimension(200, 200));
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                exitForm(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm




    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    void wipeLines() {
        myLines.clear();
    }
    
 
    
}
