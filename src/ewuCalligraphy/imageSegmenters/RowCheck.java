 
package ewuCalligraphy.imageSegmenters;

/**
 *
 * @author David McInnis <davidm@eagles.ewu.edu>
 */


public class RowCheck
{
    public static void rowSums(int[][][] image)
    {
       int y, sum;
       for(int x = 0; x < image.length; ++x)
       {
           sum = 0;
           for(y = 0; y < image[x].length; ++y)
           {
               sum += image[0][x][y];
           }
           System.out.println(sum);
       }
    }
    
    public static void columnSums(int[][][] image)
    {
        int x, sum;
        
        for(int y = 0; y < image[0].length; ++y)
        {
            sum = 0;
            for(x = 0; x < image.length; ++x)
            {
                sum += image[0][x][y];
            }
            System.out.println(sum);
        }
    }
}
