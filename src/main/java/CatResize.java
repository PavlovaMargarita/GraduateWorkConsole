import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Margarita on 21-Feb-16.
 */
public class CatResize {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedImage image = ImageIO.read(new File("cat.png"));
        int [] hash = Algorithm1.getHash(image);
        BufferedImage result = new BufferedImage(8,8,BufferedImage.TYPE_BYTE_BINARY);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(hash[i * 8 + j] == 0) {
                    result.setRGB(i, j, 255);
                } else {
                    result.setRGB(i, j, 0);
                }
            }
        }
        ImageIO.write(result, "png", new File("car result.png"));
    }
}
