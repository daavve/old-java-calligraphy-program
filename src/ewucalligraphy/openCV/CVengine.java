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


package ewucalligraphy.openCV;

import org.opencv.core.Core;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */

//The class is going to use the OpenCV engine to compare and contrast
//The results of my custom algorithm with the available algorithms from the
//OpenCV project


public class CVengine
{
    public static void launch()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
