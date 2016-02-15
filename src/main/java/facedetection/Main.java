package facedetection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import jjil.algorithm.Gray8Rgb;
import jjil.algorithm.RgbAvgGray;
import jjil.core.Image;
import jjil.core.Rect;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;

public class Main {

    public static void findFaces(BufferedImage bi, int minScale, int maxScale, File output) {
        try {
            InputStream is  = new FileInputStream("frontalfacealt.txt");
            Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(is, minScale, maxScale);
            RgbImage im = RgbImageJ2se.toRgbImage(bi);
            RgbAvgGray toGray = new RgbAvgGray();
            toGray.push(im);
            List<Rect> results = detectHaar.pushAndReturn(toGray.getFront());
//            System.out.println("Found "+results.size()+" faces");
//            Image i = detectHaar.getFront();
//            Gray8Rgb g2rgb = new Gray8Rgb();
//            g2rgb.push(i);
//            RgbImageJ2se conv = new RgbImageJ2se();
//            conv.toFile((RgbImage)g2rgb.getFront(), output.getCanonicalPath());



            Graphics2D graph = bi.createGraphics();
            for(Rect rect : results) {
                graph.draw(new Rectangle(rect.getTopLeft().getX(), rect.getTopLeft().getY(), rect.getWidth(), rect.getHeight()));
            }

            graph.dispose();

            ImageIO.write(bi, "jpg", output);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) throws Exception {        
        BufferedImage bi = ImageIO.read(new File("company1.jpg"));
        findFaces(bi, 1, 40, new File("result.jpg")); // change as needed
    }

}