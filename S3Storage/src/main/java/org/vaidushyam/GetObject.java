package org.vaidushyam;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetObject {
    public static void main(String[] args) {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

        String bucketName = System.getenv("MY_BUCKET_NAME");
        String fileName = System.getenv("fileName");
        printHeadObject(s3Client, fileName, bucketName);
        byte[] objectData = getObjectBytes(s3Client, buildObjectRequest(fileName, bucketName));
        writeBytesToFile(objectData, Paths.get("").toAbsolutePath() + "/"+ fileName);
        s3Client.close();
    }

    private static void writeBytesToFile(byte[] objectData, String path) {
        try {
            File myFile = new File(path);
            OutputStream outputStream = Files.newOutputStream(myFile.toPath());
            outputStream.write(objectData);
            System.out.println("File written successfully to: " + myFile + " " + path);
            outputStream.close();
        }catch(IOException ex){
            System.out.println("Error writing file: " + ex.getMessage());
        }

    }

    private static byte[] getObjectBytes(S3Client s3Client, GetObjectRequest objectRequest) {
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
        return objectBytes.asByteArray();
    }

    private static GetObjectRequest buildObjectRequest(String fileName, String bucketName) {
        return GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
    }

    private static void printHeadObject(S3Client s3Client, String fileName, String bucketName) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest
                .builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
        System.out.println("Object Metadata:" + headObjectResponse.toString());
    }
}
