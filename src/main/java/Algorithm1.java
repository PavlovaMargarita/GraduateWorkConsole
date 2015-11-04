import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

//        http://habrahabr.ru/post/120562/
public class Algorithm1 {

    public static void run() throws IOException, InterruptedException {
        int [] hash1 = getHash("images/1.jpg");
        int [] hash2 = getHash("images/4.jpg");
        int countOfDifferentPosition = Utils.getCountOfDifferentPosition(hash1, hash2);
        boolean isTheSame = isSameImage(countOfDifferentPosition);
        System.out.println(isTheSame);
    }

    public static int [] getHash(String img) throws IOException, InterruptedException {
        //-----resize image
        BufferedImage originalImage = ImageIO.read(new File(img));
        BufferedImage resizeImage = Utils.resizeImage(originalImage, 8, 8);

        //-----convert to gray image
        BufferedImage grayImage = Utils.convertToGray(resizeImage);

        //-----get medium pixels value
        PixelGrabber pg = new PixelGrabber(grayImage, 0, 0, -1, -1, false);
        pg.grabPixels();
        int[] pixels = (int[])pg.getPixels();

        double medium = getMedium(pixels);

        //-----generate hash
        int [] hashArray = getHashArray(pixels, medium);

        return hashArray;
    }

    private static boolean isSameImage(int countOfDifferentPosition){
        if(countOfDifferentPosition <= 10){
            return true;
        }
        return false;
    }

    private static int [] getHashArray(int[] baseArray, double medium) {
        int [] result = new int[baseArray.length];
        for(int i = 0; i < baseArray.length; i++){
            if((double)baseArray[i] > medium){
                result[i] = 1;
            } else {
                result[i] = 0;
            }
        }
        return result;
    }

    private static double getMedium(int [] array){
        int sum = 0;
        for(int i : array){
            sum += i;
        }
        return (double)sum / array.length;
    }

}
