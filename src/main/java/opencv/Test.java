package opencv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Test {

    public static Mat getHist1(BufferedImage image) {
        Mat img1 = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC3);
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        img1.put(0,0, pixels);
        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGB2GRAY);
        img1.convertTo(img1, CvType.CV_32F);
        Mat hist1 = new Mat();
        MatOfInt histSize = new MatOfInt(256);
        MatOfInt channels = new MatOfInt(0);
        ArrayList<Mat> bgr_planes1 = new ArrayList<Mat>();
        Core.split(img1, bgr_planes1);
        MatOfFloat histRanges = new MatOfFloat(0f, 255f);
        boolean accumulate = false;
        Imgproc.calcHist(bgr_planes1, channels, new Mat(), hist1, histSize, histRanges, accumulate);
        Core.normalize(hist1, hist1, 0, hist1.rows(), Core.NORM_MINMAX, -1, new Mat());
        hist1.convertTo(hist1, CvType.CV_32F);
        return hist1;
    }

    public static double isSameImage(BufferedImage baseImage1, File inputImage1) throws IOException {
        BufferedImage baseImage = Utils.resizeImage(baseImage1, 100, 100);
        BufferedImage image = ImageIO.read(inputImage1);
        BufferedImage inputImage = Utils.resizeImage(image, 100, 100);

        Mat baseHist1 = getHist1(baseImage);
        Mat inputHist1 = getHist1(inputImage);
        double compare = Imgproc.compareHist(baseHist1, inputHist1, Imgproc.CV_COMP_CHISQR);
        return compare;
    }
}

