package org.vaidushyam;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;


public class CreateBucketUsingHead {
    public static void main(String[] args) {

        String bucketName = "rangas-vaidushyam-bucket"; // Replace with your unique bucket name
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_WEST_1;
        S3Client s3Client = S3Client.builder()
                            .credentialsProvider(credentialsProvider) // Use the credentials provider
                            .region(region)
                            .build();

        if (!bucketExists(bucketName, s3Client)){
            createBucket(bucketName, s3Client);
        }

    }

    private static boolean bucketExists(String bucketName, S3Client s3Client) {
        boolean bucketExists = false;
        try{
            // Check if the bucket exists by calling headBucket
            HeadBucketRequest bucketRequest =  HeadBucketRequest.builder().bucket(bucketName).build();
            HeadBucketResponse response = s3Client.headBucket(bucketRequest);
            if (response.sdkHttpResponse().statusCode() == 200){
                // The bucket exists
                System.out.println("Bucket exists: " + bucketName);
                bucketExists = true;
            }
        } catch (AwsServiceException awsEx) {
            switch(awsEx.statusCode()) {
                case 404:
                    System.out.println("No such bucket exists " + bucketName);
                    break;
                case 400:
                    System.out.println("Attempted to access a bucket in a region other than where it exists");
                    break;
                case 403:
                    System.out.println("Access denied to the bucket: " + bucketName);
                    break;
            }
        }
        return bucketExists;
    }

    private static void createBucket(String bucketName, S3Client s3Client) {
        try {
            S3Waiter waiter = s3Client.waiter();

            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName) // Unique bucket name
                    .build();
            s3Client.createBucket(bucketRequest);

            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            WaiterResponse<HeadBucketResponse> waiterResponse = waiter.waitUntilBucketExists(headBucketRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println(bucketName + " is ready for use.");
        } catch (S3Exception ex) {
            System.err.println( "in createBucket " + ex.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
