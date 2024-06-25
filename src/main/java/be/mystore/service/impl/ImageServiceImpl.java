package be.mystore.service.impl;


import be.mystore.service.ImageService;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;


@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String getImageUrl() {
        String keyName = "default-avatar.jpg";
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, keyName)
                .withMethod(HttpMethod.GET);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return getBaseUrl(url.toString());
    }

    private String getBaseUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return url.getProtocol() + "://" + url.getHost() + url.getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return imageUrl;
    }
}
