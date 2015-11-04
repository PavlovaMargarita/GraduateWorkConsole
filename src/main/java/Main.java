import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
		
		File inputImage = new File("1.jpg");
		BufferedImage image = ImageIO.read(inputImage);
		
		File folder = new File("images/");
		File[] listOfImages = folder.listFiles();

		printImageAnalisys(listOfImages, image);
		
//        Algorithm3.run();

//        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "my_cloud_name",
//                "api_key", "my_api_key",
//                "api_secret", "my_api_secret"));
//
//        cloudinary.privateDownload()

//        FileUtils.copyURLToFile(URL, File)
		ImageIO.write( ImageIO.read(inputImage), "jpg", new File("inputImage.jpg"));
		BufferedImage rotatedImage = rotateImage(inputImage);
		ImageIO.write(rotatedImage, "jpg", new File("1_1.jpg"));
    }
	
	private static BufferedImage rotateImage(File imageFile) throws IOException {
		int angel = 90;
		BufferedImage image = ImageIO.read(imageFile);
		double sin = Math.abs(Math.sin(Math.toRadians(angel)));
        double cos = Math.abs(Math.cos(Math.toRadians(angel)));

		int w = image.getWidth(null), h = image.getHeight(null);

		int neww = (int) Math.floor(w*cos + h*sin);
        int newh = (int) Math.floor(h*cos + w*sin);

		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
		BufferedImage bimg = new BufferedImage(neww, newh, type);
		Graphics2D g = bimg.createGraphics();

		g.translate((neww-w)/2, (newh-h)/2);
		g.rotate(Math.toRadians(angel), w/2, h/2);
		g.drawRenderedImage(image, null);
		g.dispose();
		return bimg;
	}
	
	private static void printImageAnalisys(File [] listOfImages, BufferedImage inputImage) throws Exception {
		int[] inputImageHash1 = Algorithm1.getHash(inputImage);
		int[] inputImageHash2 = Algorithm2.getHash(inputImage);
		int[] inputImagePixelArray = Algorithm3.getPixels(inputImage);
		
		for (File image : listOfImages) {
			if (image.isFile()) {
				System.out.print(image.getName() + "\t");
				int[] imageHash = Algorithm1.getHash(image);
				boolean isSame = Algorithm1.isSameImage(Utils.getCountOfDifferentPosition(inputImageHash1, imageHash));
				System.out.print("| " + isSame + " |");
				imageHash = Algorithm2.getHash(image);
				isSame = Algorithm2.isSameImage(Utils.getCountOfDifferentPosition(inputImageHash2, imageHash));
				System.out.print("| " + isSame + " |");
				imageHash = Algorithm3.getPixels(image);
				isSame = Algorithm3.isSameImage(Algorithm3.getMeanSquare(inputImagePixelArray, imageHash));
				System.out.println("| " + isSame + " |");
			}
		}
	}
}
