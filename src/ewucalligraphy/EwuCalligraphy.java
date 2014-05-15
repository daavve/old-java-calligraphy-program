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

package ewucalligraphy;
import  ewucalligraphy.gui.MainWindow;
import ewucalligraphy.gui.MouseWatcher;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */
public class EwuCalligraphy {

    
    
    public static void main(String[] args)
    {
        ScheduledThreadPoolExecutor stp;
        ScheduledFuture sf;
        
        MouseWatcher mouseWatch;
        
        if(args.length == 0) //interactive mode
        {
            stp = new ScheduledThreadPoolExecutor(1);
            mouseWatch = new MouseWatcher();
            sf = stp.scheduleAtFixedRate(mouseWatch, 1000, 1000, TimeUnit.MILLISECONDS);
            
            
            MainWindow myWindow = new MainWindow();
            myWindow.start();
        }
        else //special mode
        {
                System.out.println("Entering Special Mode");
/*                switch(args[0])
                {
                    case "TestDarkEdges":
                        assert(args.length == 2); 
                    break; 
                }*/
                
            }
	}
}
