import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Algorithm3 {
	
	static int[] getPixels(File imageFile) throws IOException, InterruptedException {
		BufferedImage image = ImageIO.read(imageFile);
		return getPixels(image);
	}

    static int[] getPixels(BufferedImage image) throws IOException, InterruptedException {
        //-----resize image
        BufferedImage resizedImage = Utils.resizeImage(image, 32, 32);
//        ImageIO.write(resizeImageJpg, "jpg", new File("cat_32.jpg"));

        //-----convert to gray
        resizedImage = Utils.convertToGray(resizedImage);

        //-----get image pixels
        int[] pixels = Utils.getPixels(resizedImage);

        return pixels;
    }

    public static double getMeanSquare(int[] arr1, int[] arr2){
        double sum = 0;
        for(int i = 0; i < arr1.length; i++){
            sum += Math.pow(arr1[i] - arr2[i], 2);
        }
        return Math.sqrt(sum / arr1.length);
    }

    public static boolean isSameImage(double meanSquare){
		System.out.print("--" + meanSquare + "---");
        double a = 0.15 * (Math.pow(2, 24) * 2 + 1);
        if(meanSquare <= a){
            return true;
        }
        return false;
    }

}
