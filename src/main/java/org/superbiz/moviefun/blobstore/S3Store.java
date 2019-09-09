package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Optional;

public class S3Store implements BlobStore {
    private final AmazonS3Client s3Client;
    private final String photoStorageBucket;

    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }

    @Override
    public void put(Blob blob) throws IOException {
        s3Client.putObject(
                photoStorageBucket,
                blob.name,
                blob.inputStream,new ObjectMetadata()
        );

    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        System.out.println("2");
        S3Object s3object = s3Client.getObject(photoStorageBucket, name);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        System.out.println(s3object.getObjectContent().available());
        byte[] bytes = IOUtils.toByteArray(inputStream);
        Blob blob = new Blob(name,new ByteArrayInputStream(bytes),new Tika().detect(bytes));
        return Optional.ofNullable(blob);
    }

    @Override
    public void deleteAll() {
        // ...
    }
}