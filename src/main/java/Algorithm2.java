import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;

public class Algorithm2 {

	private static int size = 32;
    private static int smallerSize = 8;
    private static double[] c;
	
	static{
		initCoefficients();
	}
	
    private static void initCoefficients() {
        c = new double[size];

        for (int i=1;i<size;i++) {
            c[i]=1;
        }
        c[0]=1/Math.sqrt(2.0);
    }

//    public static void run() throws Exception {
//        int [] hash1 = getHash("images/1.jpg");
//        int [] hash2 = getHash("images/2.jpg");
//        int difCount = utils.Utils.getCountOfDifferentPosition(hash1, hash2);
//        System.out.println(difCount);
//        boolean isTheSame = isSameImage(difCount);
//        System.out.println(isTheSame);
//    }

	public static int [] getHash(File imageFile) throws Exception {
		BufferedImage image = ImageIO.read(imageFile);
		return getHash(image);
	}

    public static int [] getHash(BufferedImage image) throws Exception {
		//-----resize image
        BufferedImage resizedImage = Utils.resizeImage(image, size, size);
		
		//-----convert to gray image
        resizedImage = Utils.convertToGray(resizedImage);

        double[][] vals = new double[size][size];

        for (int x = 0; x < resizedImage.getWidth(); x++) {
            for (int y = 0; y < resizedImage.getHeight(); y++) {
                vals[x][y] = getBlue(resizedImage, x, y);
            }
        }
		
		/* 3. Compute the DCT. 
		 * The DCT separates the image into a collection of frequencies 
		 * and scalars. While JPEG uses an 8x8 DCT, this algorithm uses 
		 * a 32x32 DCT.
		 */
        double[][] dctVals = applyDCT(vals);
       		
		/* 4. Reduce the DCT. 
		 * This is the magic step. While the DCT is 32x32, just keep the 
		 * top-left 8x8. Those represent the lowest frequencies in the 
		 * picture.
		 */
		/* 5. Compute the average value. 
		 * Like the Average Hash, compute the mean DCT value (using only 
		 * the 8x8 DCT low-frequency values and excluding the first term 
		 * since the DC coefficient can be significantly different from 
		 * the other values and will throw off the average).
		 */
        double total = 0;

        for (int x = 0; x < smallerSize; x++) {
            for (int y = 0; y < smallerSize; y++) {
                total += dctVals[x][y];
            }
        }
        total -= dctVals[0][0];

        double avg = total / (double) ((smallerSize * smallerSize) - 1);
	
		/* 6. Further reduce the DCT. 
		 * This is the magic step. Set the 64 hash bits to 0 or 1 
		 * depending on whether each of the 64 DCT values is above or 
		 * below the average value. The result doesn't tell us the 
		 * actual low frequencies; it just tells us the very-rough 
		 * relative scale of the frequencies to the mean. The result 
		 * will not vary as long as the overall structure of the image 
		 * remains the same; this can survive gamma and color histogram 
		 * adjustments without a problem.
		 */
        int [] hash = new int [smallerSize * smallerSize];

        for (int x = 0; x < smallerSize; x++) {
            for (int y = 0; y < smallerSize; y++) {
                if (x != 0 && y != 0) {
                    hash[x*y + x] = (dctVals[x][y] > avg? 1 :0);
                }
            }
        }

        return hash;
		
    }

    public static boolean isSameImage(int difCount){
        if(difCount <= 10){
            return true;
        }
        return false;
    }
	
	private static BufferedImage resize(BufferedImage image, int width,	int height) {
		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private static ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    private static BufferedImage grayscale(BufferedImage img) {
        colorConvert.filter(img, img);
        return img;
    }

    private static int getBlue(BufferedImage img, int x, int y) {
        return (img.getRGB(x, y)) & 0xff;
    }

    private static double[][] applyDCT(double[][] f) {
        int N = 32;

        double[][] F = new double[N][N];
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double sum = 0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        sum += Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*(f[i][j]);
                    }
                }
                sum *= ((c[u] * c[v]) / 4.0);
                F[u][v] = sum;
            }
        }
        return F;
    }
}
