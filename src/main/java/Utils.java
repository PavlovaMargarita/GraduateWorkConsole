import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.PixelGrabber;

public class Utils {
    public static int getCountOfDifferentPosition(int[] arr1, int[] arr2){
        int count = 0;
        for(int i = 0; i < arr1.length; i++){
            if(arr1[i] != arr2[i]){
                count++;
            }
        }
        return count;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage,int width, int height){
        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    public static BufferedImage convertToGray(BufferedImage originalImage){
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
		op.filter(originalImage, originalImage);
		return originalImage;
    }

    public static int[] getPixels(BufferedImage originalImage) throws InterruptedException {
        PixelGrabber pg = new PixelGrabber(originalImage, 0, 0, -1, -1, false);
        pg.grabPixels();
        int[] pixels = (int[])pg.getPixels();
        return pixels;
    }
}
