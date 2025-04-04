package org.vaidushyam;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

public class CreateBucket
{
    public static void main(String[] args) {
        System.out.println("Creating Bucket ......");
        Region region = Region.US_EAST_1;
        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create("restricted"))
                .build();
        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                .bucket("vaidushyam-bucket") // Unique bucket name
                .build();
        s3Client.createBucket(bucketRequest);
        System.out.println("Bucket created successfully: " + bucketRequest.bucket());
    }
}
