import com.sun.org.apache.xpath.internal.SourceTree;
import opencv.HistogramTest;
import opencv.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) throws Exception {
        new DetectFaceDemo().run();

		File inputImage = new File("1.jpg");
		BufferedImage image = ImageIO.read(inputImage);

		File folder = new File("images/");
		File[] listOfImages = folder.listFiles();

        System.out.println("OpenCV -----");
        imageAnalysisByOpenCV(listOfImages, image);
        System.out.println("OpenCV -----");

		printImageAnalysis(listOfImages, image);

        System.out.println();
        for(int i = 0; i < 3; i++){
            image = rotateImage(image);
            printImageAnalysis(listOfImages, image);
            System.out.println();
        }

//        Algorithm3.run();

//        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "my_cloud_name",
//                "api_key", "my_api_key",
//                "api_secret", "my_api_secret"));
//
//        cloudinary.privateDownload()

//        FileUtils.copyURLToFile(URL, File)
//		ImageIO.write( ImageIO.read(inputImage), "jpg", new File("inputImage.jpg"));
//		BufferedImage rotatedImage = rotateImage(inputImage);
//		ImageIO.write(rotatedImage, "jpg", new File("1_1.jpg"));
    }
	
	private static BufferedImage rotateImage(BufferedImage image) throws IOException {
		int angel = 90;
		double sin = Math.abs(Math.sin(Math.toRadians(angel)));
        double cos = Math.abs(Math.cos(Math.toRadians(angel)));

		int width = image.getWidth(null), height = image.getHeight(null);

		int newWidth = (int) Math.floor(width*cos + height*sin);
        int newHeight = (int) Math.floor(height*cos + width*sin);

		int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, type);
		Graphics2D g = newImage.createGraphics();

		g.translate((newWidth - width) / 2, (newHeight - height) / 2);
		g.rotate(Math.toRadians(angel), width / 2, height / 2);
		g.drawRenderedImage(image, null);
		g.dispose();
		return newImage;
	}
	
	private static void printImageAnalysis(File[] listOfImages, BufferedImage inputImage) throws Exception {
		int[] inputImageHash1 = Algorithm1.getHash(inputImage);
		int[] inputImageHash2 = Algorithm2.getHash(inputImage);
		int[] inputImagePixelArray = Algorithm3.getPixels(inputImage);

		for (File image : listOfImages) {
			if (image.isFile()) {
				System.out.print(image.getName() + "\t");
				int[] imageHash = Algorithm1.getHash(image);
				boolean isSame = Algorithm1.isSameImage(utils.Utils.getCountOfDifferentPosition(inputImageHash1, imageHash));
				System.out.print("| " + isSame + " |");
				imageHash = Algorithm2.getHash(image);
				isSame = Algorithm2.isSameImage(utils.Utils.getCountOfDifferentPosition(inputImageHash2, imageHash));
				System.out.print("| " + isSame + " |");
				imageHash = Algorithm3.getPixels(image);
				isSame = Algorithm3.isSameImage(Algorithm3.getMeanSquare(inputImagePixelArray, imageHash));
				System.out.println("| " + isSame + " |");
            }
		}
	}

    private static void imageAnalysisByOpenCV(File[] listOfImages, BufferedImage inputImage) throws IOException {
        for (File image : listOfImages) {
            System.out.print(image.getName() + "\t");
            Test.isSameImage(inputImage, image);
        }
    }
}
