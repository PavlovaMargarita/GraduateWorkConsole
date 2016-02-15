package facedetection2;

import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainFaceDetection {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedImage image = ImageIO.read(new File("test.jpg"));
        BufferedImage imageWithFace = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int [] pixels = Utils.getPixels(image);
        for(int i = 0; i < pixels.length; i++){
            int R = (pixels[i] >> 16) & 0xff;
            int G = (pixels[i] >> 8) & 0xff;
            int B = pixels[i] & 0xff;
            double r = (double)R / (R + G + B);
            double g = (double)G / (R + G + B);
            double F1R = -1.376 * r * r + 1.0743 * r + 0.2;
            double F2R = -0.776 * r * r + 0.5601 * r + 0.18;
            double w = Math.pow(r - 0.33, 2) + Math.pow(g - 0.33, 2);
//
            double tetta = 1 / Math.cos(((R - G) + (R - B)) / 2 / Math.sqrt(Math.pow(R - G, 2) + (R - B) * (G - B)));
            double H;
            if(B <= G){
                 H = tetta;
            } else {
                H = 360 - tetta;
            }

            int x = i % image.getWidth();
            int y = i / image.getWidth();


//            if(g < F1R && g > F2R && w > 0.001 && (H > 240 || H <= 20)) {
//                imageWithFace.setRGB(x, y, pixels[i]);
//            } else {
//                try {
//                    imageWithFace.setRGB(x, y, 0);
//                } catch (Exception e){
//                    System.out.println(x + " " + y);
//                }
//            }
            double I = (R + G + B) / 3.0;
            if((I < 80 && (B - G < 15 || B - R < 15)) || (H > 20 && H <= 40)) {
                imageWithFace.setRGB(x, y, pixels[i]);
            } else {
                imageWithFace.setRGB(x, y, 0);
            }
        }
        ImageIO.write(imageWithFace, "jpg", new File("test2.jpg"));
    }
}
