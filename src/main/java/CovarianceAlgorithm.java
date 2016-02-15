import Jama.LUDecomposition;
import Jama.Matrix;
import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CovarianceAlgorithm {

    private static int baseWidth = 64;
    private static int baseHeight = 64;

    public static boolean sC(BufferedImage originalImage, File databaseImageFile) throws IOException, InterruptedException {
        BufferedImage databaseImage = ImageIO.read(databaseImageFile);

        BufferedImage originalImageResize = Utils.resizeImage(originalImage, baseWidth, baseHeight);
        BufferedImage databaseImageResize = Utils.resizeImage(databaseImage, baseWidth, baseHeight);

        double[] databaseAverage = getAverage(Utils.getPixels(databaseImageResize));
        double[] originalAverage = getAverage(Utils.getPixels(originalImageResize));
        double[][] databaseCovarianceMatrix = covarianceMatrix(databaseImageResize, databaseAverage);
        double[][] originalCovarianceMatrix = covarianceMatrix(originalImageResize, originalAverage);
        double dC = dC(databaseCovarianceMatrix, databaseAverage, originalCovarianceMatrix, originalAverage);
//        System.out.println(dC);

//        int[] databaseTexture = textureFeature(databaseImage);
//        int[] originalTexture = textureFeature(originalImage);
//        double dT = dT(databaseTexture, originalTexture);
//        System.out.println(dT);
//        double wC = 0.7;
//        return wC * dC + (1-wC) * dT;
        if(dC < 0.33){
            return true;
        }
        return false;
    }

    public static double dT(int[] textureFeature1, int[] textureFeature2){
        double sum = 0;
        for(int i = 0; i < textureFeature1.length; i++){
            sum += Math.pow(textureFeature1[i] - textureFeature2[i], 2);
        }
        return Math.sqrt(sum);
    }

    public static double dC(double[][] covarianceMatrix1, double[] average1, double[][] covarianceMatrix2, double[] average2){
        Matrix A = new Matrix(3, 3);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                A.set(i, j, (covarianceMatrix1[i][j] + covarianceMatrix2[i][j]) / 2);
            }
        }

        LUDecomposition luDecomposition = new LUDecomposition(A);

        double[][] staticMatrix = {{1,0,0}, {0,1,0},{0,0,1}};
        Matrix b = new Matrix(staticMatrix);

        Matrix x = luDecomposition.solve(b); // solve Ax = b for the unknown vector x

        double[] avgSub = new double[3];
        for(int i = 0; i < 3; i++){
            avgSub[i] = average1[i] - average2[i];
        }

        double[] mult1 = new double[3];
        for(int i = 0; i < 3; i++){
            double sum = 0;
            for(int j = 0; j < 3; j++){
                sum += avgSub[j] * x.get(j, i);
            }
            mult1[i] = sum;
        }

        double sum1 = 0;
        for(int i = 0; i < 3; i++){
            sum1 += mult1[i] * avgSub[i];
        }

        double sum2 = Math.log(A.det() / Math.sqrt((new Matrix(covarianceMatrix1).det()) * (new Matrix(covarianceMatrix2).det())));

        return sum1 / 8 + sum2 / 2;

    }

    public static int[] textureFeature(BufferedImage image) throws InterruptedException {
        int[] grayPixels = Utils.getGrayPixels(image);

        int[] grayLevels = new int [256];

        for(int i : grayPixels){
            grayLevels[i]++;
        }
        return grayLevels;
    }

    public static double[][] covarianceMatrix(BufferedImage image, double[] average) throws InterruptedException {
        int [] pixels = Utils.getPixels(image);
        double [][]covarianceMatrix = new double [3][3];

        double CRR = 0;
        for(int i : pixels){
            int r = (i >> 16) & 0xff;
            CRR += r * r;
        }
        CRR /= pixels.length;
        CRR -= Math.pow(average[0], 2); // r * r
        covarianceMatrix[0][0] = CRR;

        double CRG = 0;
        for(int i : pixels){
            int r = (i >> 16) & 0xff;
            int g = (i >> 8) & 0xff;
            CRG += r * g;
        }
        CRG /= pixels.length;
        CRG -= average[0] * average[1]; // r * g
        covarianceMatrix[0][1] = CRG;
        covarianceMatrix[1][0] = CRG;

        double CRB = 0;
        for(int i : pixels){
            int r = (i >> 16) & 0xff;
            int b = i & 0xff;
            CRB += r * b;
        }
        CRB /= pixels.length;
        CRB -= average[0] * average[2]; // r * b
        covarianceMatrix[0][2] = CRB;
        covarianceMatrix[2][0] = CRB;

        double CGG = 0;
        for(int i : pixels){
            int g = (i >> 8) & 0xff;
            CGG += g * g;
        }
        CGG /= pixels.length;
        CGG -= Math.pow(average[1], 2); // g * g
        covarianceMatrix[1][1] = CGG;

        double CGB = 0;
        for(int i : pixels){
            int g = (i >> 8) & 0xff;
            int b = i & 0xff;
            CGB += g * b;
        }
        CGB /= pixels.length;
        CGB -= average[1] * average[2]; // g * b
        covarianceMatrix[1][2] = CGB;
        covarianceMatrix[2][1] = CGB;

        double CBB = 0;
        for(int i : pixels){
            int b = i & 0xff;
            CBB += b * b;
        }
        CBB /= pixels.length;
        CBB -= Math.pow(average[2], 2); // b * b
        covarianceMatrix[2][2] = CBB;

        return covarianceMatrix;
    }
    private static double[] getAverage(int [] pixels){
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        for(int i : pixels){
            sumR += (i >> 16) & 0xff;
            sumG += (i >> 8) & 0xff;
            sumB += i & 0xff;
        }
        double [] result = new double[3];
        result[0] = (double)sumR / pixels.length;
        result[1] = (double)sumG / pixels.length;
        result[2] = (double)sumB / pixels.length;

        return result;

    }
}
