package org.vaidushyam;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;

public class PutItemInBucket {
    public static void main(String[] args) {

        Region region = Region.US_EAST_1;
        String bucketName = "vaidushyam-bucket";
        String fileName = "airports.csv";
        String filePath = "data/airports.csv";

        S3Client s3Client = S3Client.builder()
                .region(region)
                .build();

       putItemInBucket(s3Client, bucketName, fileName, filePath);
    }

    private static void putItemInBucket(S3Client s3Client, String bucketName, String fileName, String filePath)
    {
        try {
            PutObjectRequest putObject = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            File file = new File(filePath);
            System.out.println("filePath = " + filePath + " filePath from fileName = " + file.getAbsolutePath());
            s3Client.putObject(putObject, RequestBody.fromFile(new File(filePath)));
            System.out.println("Successfully placed file "+fileName +" in bucket" + bucketName);
        }catch(S3Exception s3Ex){
            System.err.println(s3Ex.awsErrorDetails().errorMessage());
            System.exit(1);
            }
    }
}


