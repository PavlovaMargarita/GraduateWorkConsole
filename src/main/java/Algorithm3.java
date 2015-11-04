import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Algorithm3 {

    public static void run() throws Exception {
        int [] pixels1 = getPixels("images/1.jpg");
        int [] pixels2 = getPixels("images/2.jpg");
        double test = getMeanSquare(pixels1, pixels2);
        System.out.println(test);
    }

    private static int[] getPixels(String img) throws IOException, InterruptedException {
        //-----resize image
        BufferedImage originalImage = ImageIO.read(new File(img));
        BufferedImage resizeImage = Utils.resizeImage(originalImage, 32, 32);
//        ImageIO.write(resizeImageJpg, "jpg", new File("cat_32.jpg"));

        //-----convert to gray
        BufferedImage grayImage = Utils.convertToGray(resizeImage);

        //-----get image pixels
        int[] pixels = Utils.getPixels(grayImage);

        return pixels;
    }

    private static double getMeanSquare(int[] arr1, int[] arr2){
        double sum = 0;
        for(int i = 0; i < arr1.length; i++){
            sum += Math.pow(arr1[i] - arr2[i], 2);
        }
        return Math.sqrt(sum);
    }

    private static boolean isSameImage(float meanSquare){
        if(meanSquare <= 0.05 * 32){
            return true;
        }
        return false;
    }

}
