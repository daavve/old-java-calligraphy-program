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


package ewucalligraphy.testing;

import ewucalligraphy.image.WholeImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import javax.imageio.ImageIO;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class BulkOperations
{
    public static void testDarkEdges(String targetDir)
    {
        Path p1, p2;
        DirectoryStream d1 = null;
                
        p1 = Paths.get(targetDir);
        try
        {
            d1 = Files.newDirectoryStream(p1);
        }
        catch(IOException e)
        {
            System.out.println(e);
            System.out.println("Invalid Directory, Exiting");
            System.exit(1);
        }
        
        
        Iterator di1 = d1.iterator();
        
        BufferedImage inImage = null;
        Boolean noException;
        WholeImage workingImage;
        
        int totalImage = 0;
        int badEdgeImage = 0;
        
        while(di1.hasNext())
        {
            p2 = (Path) di1.next();
            File p2File = p2.toFile();
            noException = true;
            try
            {
                inImage = ImageIO.read(p2File);
            }
            catch(IOException e)
            {
                noException = false;
                System.out.println(e);
                System.out.println("Skipping" + p2);
            }
            
            if(noException && inImage != null)
            {
                ++totalImage;
                workingImage = new WholeImage(inImage, p2.toString());
                if(workingImage.isGray())
                {
                    if(workingImage.doWeHaveABadEdge())
                    {
                        System.out.println("Bad: " + workingImage.getName());
                        ++badEdgeImage;
                    }
                }
            }
        }

        System.out.println("TotalGrayImages: " + totalImage + " Num With BadEdges: " + badEdgeImage);
        
    }
}
