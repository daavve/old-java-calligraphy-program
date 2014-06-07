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

import ewucalligraphy.image.ImagePart;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON3;
import java.awt.image.BufferedImage;


public class DisplayWindow extends javax.swing.JFrame
{
    private BufferedImage fileImage;
    
    private static final int TOP_OFFSET  = 30;
    private static final int B_R_L_OFFSET = 10; //Botom, Right & Left
   

    private boolean drawed = false;
    
    private final Rectangle imgRect = new Rectangle();
    private Double imgHeightWidthRatio, imgWidthHeightRatio;
    
    private final ImagePart imgRef;
    
    private DisplayWindow childWindow;
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
        
    public DisplayWindow(ImagePart iPrntImg, BufferedImage iFileImage)
    {
        imgRef = iPrntImg;
        setImage(iFileImage);
        initComponents();
    }
    
    void setImage(BufferedImage iFileImage)
    {
        fileImage = iFileImage;
      
        double imgWidth = (double) fileImage.getWidth();
        double imgHeight = (double) fileImage.getHeight();
        
        imgHeightWidthRatio = imgHeight / imgWidth;
        imgWidthHeightRatio = imgWidth / imgHeight;
        
        imgRect.setLocation(B_R_L_OFFSET, TOP_OFFSET);
    }
    

    
    	@Override
	public void paint(Graphics g)
	{
            if(justPaintBoxes)
            {
                imgRef.drawChangedBoxes(g);
                justPaintBoxes = false;
            }
            else
            {
                super.paint(g);
                drawImage(g);
                drawOverImage(g);
            }

        }
        
        
        void drawImage(Graphics g)
        {
               //This part scales the image to fit within the window
            
		if(fileImage != null)
		{
			Rectangle boxRectangle = new Rectangle();
                        boxRectangle.width = this.getWidth() - B_R_L_OFFSET * 2;
                        
                        boxRectangle.height = this.getHeight() - TOP_OFFSET - B_R_L_OFFSET;
     
			double windowHeightWidthRatio = boxRectangle.getHeight() / boxRectangle.getWidth();


			if(windowHeightWidthRatio < imgHeightWidthRatio) //window not long enough
			{
                            imgRect.height = boxRectangle.height;
                            imgRect.width = (int) ((boxRectangle.getHeight() * imgWidthHeightRatio));
			}
                        else //window not wide enough
						{
                            imgRect.width = boxRectangle.width;
                            imgRect.height = (int) ((boxRectangle.getWidth() * imgHeightWidthRatio));
			}

			if((imgRect.width > 0 && imgRect.height > 0) || !drawed)
			{
                            scaleFactor = imgRect.getWidth() / fileImage.getWidth();
                            invScaleFactor = fileImage.getWidth() / imgRect.getWidth();
                            Image scaledImage = fileImage.getScaledInstance(imgRect.width, imgRect.height, Image.SCALE_FAST);
                            drawed = g.drawImage(scaledImage, imgRect.x, imgRect.y, imgRect.width, imgRect.height, null);
                            
                        }
                }
        }
        
        
        private void drawOverImage(Graphics g) //This function draws stuff over the actual image
        {
            imgRef.drawBoxes(g);
        }
        
        private double scaleFactor, invScaleFactor;
        
        private Point transformCoordinates(Point XYin, boolean normalTransform)
        {
            int newX = 0;
            int newY = 0;
            
            if(normalTransform)
            {
                newX = B_R_L_OFFSET + (int) (XYin.x * scaleFactor);
                newY = TOP_OFFSET + (int) (XYin.y * scaleFactor);

            }
            else
            {
                newX =  + (int) ((XYin.x - B_R_L_OFFSET) * invScaleFactor);
                newY =  + (int) ((XYin.y - TOP_OFFSET) * invScaleFactor);
            }
            
           
            Point newCoordinants = new Point(newX, newY);
        
            return newCoordinants;
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
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseMoved(java.awt.event.MouseEvent evt)
            {
                formMouseMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                formMouseClicked(evt);
            }
        });
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

    private boolean justPaintBoxes = false;
    
    private void formMouseMoved(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseMoved
    {//GEN-HEADEREND:event_formMouseMoved
        
        Point mouseLoc = evt.getPoint();
        Point relLocation = this.transformCoordinates(mouseLoc, false);
        if(imgRef.detectMouseOver(relLocation))
        {
            justPaintBoxes = true;
            repaint();
        }
    }//GEN-LAST:event_formMouseMoved

    private void formMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseClicked
    {//GEN-HEADEREND:event_formMouseClicked
        Point mouseLoc = transformCoordinates(evt.getPoint(), false);
        switch(evt.getButton())
        {
            case BUTTON1: //Left-Click
            if(imgRef.selectThisBox(mouseLoc))
            {
                justPaintBoxes = true;
                repaint();
            }
            break;
            case BUTTON3: //Right-Click
                if(childWindow == null)
                {
                    Rectangle hRect = imgRef.getHighlightedRectangle();
                    BufferedImage childImage = fileImage.getSubimage(WIDTH, WIDTH, WIDTH, WIDTH);
                }
                break;
        }
    }//GEN-LAST:event_formMouseClicked




    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


    public void drawLine(Graphics g, Point start, Point end, Color curColor)
    {
        Point tStart = transformCoordinates(start, true);
        Point tEnd   = transformCoordinates(end, true);
        g.setColor(curColor);
        
        g.drawLine(tStart.x, tStart.y, tEnd.x, tEnd.y);
    }

    private void getHighlightedRectangle()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
