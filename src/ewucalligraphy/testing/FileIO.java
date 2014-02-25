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

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class FileIO
{
    public static boolean saveToFile(String toSave, String fileName)
    {
        boolean success = true;
        
        try
        {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(toSave);
            }
        }
        catch(Exception e)
        {
            success = false;
        }
        
        
        
        
        return success;
    }
}
