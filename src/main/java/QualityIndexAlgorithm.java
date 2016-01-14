import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QualityIndexAlgorithm {

    private static int baseWidth = 64;
    private static int baseHeight = 64;
    private static double T = 0.8;

    public static boolean getSimilarityCoefficient(BufferedImage originalImage, File databaseImageFile) throws IOException, InterruptedException {
        BufferedImage databaseImage = ImageIO.read(databaseImageFile);

        BufferedImage originalImageResize = Utils.resizeImage(originalImage, baseWidth, baseHeight);
        BufferedImage databaseImageResize = Utils.resizeImage(databaseImage, baseWidth, baseHeight);

        originalImageResize = Utils.convertToGray(originalImageResize);
        databaseImageResize = Utils.convertToGray(databaseImageResize);

        double originalImageResizeMu = calcMu(originalImageResize);
        double databaseImageResizeMu = calcMu(databaseImageResize);

        double originalImageResizeVariance = calcVariance(originalImageResize);
        double databaseImageResizeVariance = calcVariance(databaseImageResize);

        double luminanceDistortion = calcLuminanceDistortion(originalImageResizeMu, databaseImageResizeMu);
        double contrastDistortion = calcContrastDistortion(originalImageResizeVariance, databaseImageResizeVariance);

        double mult1 = luminanceDistortion * contrastDistortion;
        if(mult1 < T){
            return false;
        } else {
            mult1 *= calcCorrelationCoefficient(originalImageResize, originalImageResizeMu, originalImageResizeVariance, databaseImageResize, databaseImageResizeMu, databaseImageResizeVariance);
            if(mult1 < T){
                return false;
            } else {
                return true;
            }
        }
    }

    private static double calcMu(BufferedImage image) throws InterruptedException {
        int[] pixels = Utils.getGrayPixels(image);
        int sum = 0;
        for(int i : pixels){
            sum += i;
        }
        return (double) sum / pixels.length;
    }

    private static double calcVariance(BufferedImage image) throws InterruptedException {
        int[] pixels = Utils.getGrayPixels(image);
        double sum = 0;
        double sumIn2 = 0;

        for(int i : pixels){
            sum += i;
            sumIn2 += i * i;
        }

        sum /= pixels.length;
        sumIn2 /= pixels.length;
        return sumIn2 - sum * sum;
    }

    private static double calcLuminanceDistortion(double mu1, double mu2){
        return 2 * mu1 * mu2 / (mu1 * mu1 + mu2 * mu2);
    }

    private static double calcContrastDistortion(double variance1, double variance2){
        return 2 * Math.sqrt(variance1 * variance2) / (variance1 + variance2);
    }

    private static double calcCorrection(BufferedImage image1, double mu1, BufferedImage image2, double mu2) throws InterruptedException {
        double sum1 = 0;
        int[] pixels1 = Utils.getGrayPixels(image1);
        int[] pixels2 = Utils.getGrayPixels(image2);
        for(int i = 0; i < pixels1.length; i++){
            sum1 += (pixels1[i] - mu1) * (pixels2[i] - mu2);
        }


        return sum1 / pixels1.length;
    }

    private static double calcCorrelationCoefficient(BufferedImage image1, double mu1, double variance1, BufferedImage image2, double mu2, double variance2) throws InterruptedException {
        return calcCorrection(image1, mu1, image2, mu2) / Math.sqrt(variance1 * variance2);
    }
}
