package org.vaidushyam;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class PresignedURL {
    public static void main(String[] args) {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

        String bucketName = System.getenv("MY_BUCKET_NAME");
        String fileName = System.getenv("fileName");
        String presignedURL = getPresignedURL(presigner, bucketName, fileName);
        System.out.println("Presigned URL: " + presignedURL);

        presigner.close();
    }

    private static String getPresignedURL(S3Presigner presigner, String bucketName, String fileName) {
        String presignedURL = "";

        GetObjectRequest objectRequest = GetObjectRequest
                                            .builder()
                                            .bucket(bucketName)
                                            .key(fileName)
                                            .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                                .signatureDuration(java.time.Duration.ofMinutes(60))
                                                .getObjectRequest(objectRequest)
                                                .build();
        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

        presignedURL = presignedRequest.url().toString();

        return presignedURL;
    }
}
