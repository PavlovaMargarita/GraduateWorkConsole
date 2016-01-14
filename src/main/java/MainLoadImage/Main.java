package MainLoadImage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "graduatework",
                "api_key", "593791151728485",
                "api_secret", "yU_xhmPOozFfRQa3izDzT3sIFmE"));
//        File toUpload = new File("1.jpg");
//        Map uploadResult = cloudinary.uploader().upload(toUpload, null);
//        System.out.println(uploadResult);
        Map result = cloudinary.api().resources(ObjectUtils.emptyMap());
        System.out.println(result);

    }
}
