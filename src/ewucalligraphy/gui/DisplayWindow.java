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
    

    private boolean drawed = false;
    
    private final LinkedList<Line> myLines = new LinkedList<>();
    
    private Rectangle imgRectangle = new Rectangle();
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
        
        imgHeightWidthRatio = imgHeight / imgWidth;
        imgWidthHeightRatio = imgWidth / imgHeight;
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
			Rectangle boxRectangle = new Rectangle();
                        boxRectangle.width = this.getWidth() - brlOffset * 2;
                        
                        boxRectangle.height = this.getHeight() - topOffset - brlOffset;
                        //Inefficient but very intuative       
			double windowHeightWidthRatio = boxRectangle.getHeight() / boxRectangle.getWidth();


			if(windowHeightWidthRatio < imgHeightWidthRatio) //window not long enough
			{
                            imgRectangle.height = boxRectangle.height;
                            imgRectangle.width = (int) ((boxRectangle.getHeight() * imgWidthHeightRatio));
			}
                        else //window not wide enough
			{
                            imgRectangle.width = boxRectangle.width;
                            imgRectangle.height = (int) ((boxRectangle.getWidth() * imgHeightWidthRatio));
			}

			if((imgRectangle.width > 0 && imgRectangle.height > 0) || !drawed)
			{
                            scaleFactor = imgRectangle.getWidth() / fileImage.getWidth();
                                       
                            Image scaledImage = fileImage.getScaledInstance(imgRectangle.width, imgRectangle.height, Image.SCALE_FAST);
                            drawed = g.drawImage(scaledImage, brlOffset, topOffset, imgRectangle.width, imgRectangle.height, null);
                        }
                }
        }

        public void mouseWatch(Point mouseLoc) //TODO: Modify to handle rectangles
        {
            /*
            int imageTopCornerX = this.getX() + brlOffset;
            int imageTopCornerY = this.getY() + topOffset;
            
            int imageBottomCornerX = imageTopCornerX + imageSizeScaled[0];
            int imageBottomCornerY = imageTopCornerY + imageSizeScaled[1];
            
            if(mouseLoc.x < imageBottomCornerX &&
               mouseLoc.x > imageTopCornerX && 
               mouseLoc.y > imageTopCornerY && 
               mouseLoc.y < imageBottomCornerY)
            {
                 //On top of image
            }
            else
            {
                //Outside image
            }

        */    
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
                
                Point lStart = transformCoordinates(iStart);
                Point lEnd   = transformCoordinates(iEnd);
                
                g.setColor(curLine.getColor());
                
                
                g.drawLine(lStart.x, lStart.y, lEnd.x, lEnd.y);
                
            }
        }
        
        private double scaleFactor;
        
        public Point transformCoordinates(int[] XYin)
        {
            int newX = brlOffset + (int) (XYin[0] * scaleFactor);
            int newY = topOffset + (int) (XYin[1] * scaleFactor);
           
            Point newCoordinants = new Point(newX, newY);
        
            return newCoordinants;
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
