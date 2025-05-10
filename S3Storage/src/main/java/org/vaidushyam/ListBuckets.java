package org.vaidushyam;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

public class ListBuckets {
    public static void main (String[] args) {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        S3Client s3client = S3Client.builder()
                            .credentialsProvider(credentialsProvider)
                            .region(region)
                            .build();
        printBuckets(s3client);
    }

    private static void printBuckets(S3Client s3client) {
        ListBucketsResponse buckets = s3client.listBuckets();
        System.out.println("Your buckets are: ");
        for( Bucket bucket : buckets.buckets()) {
            System.out.println(bucket.name());
        }
    }
}
