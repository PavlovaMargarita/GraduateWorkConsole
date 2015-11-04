import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
		
		File inputImage = new File("1.jpg");
		int[] inputImageHash1 = Algorithm1.getHash(inputImage);
		int[] inputImageHash2 = Algorithm2.getHash(inputImage);
		int[] inputImagePixelArray = Algorithm3.getPixels(inputImage);
		
		File folder = new File("images/");
		File[] listOfImages = folder.listFiles();

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
//        Algorithm3.run();
    }
}
