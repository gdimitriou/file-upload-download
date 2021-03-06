package com.upload.download.file.minio;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class MinioAdapter {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.folder.name}")
    String baseFolder;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void uploadFile(String name, byte[] content, String bucketName) throws RegionConflictException, InvalidBucketNameException, InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException, IOException, NoResponseException, InvalidKeyException, InternalException {

        if(!minioClient.bucketExists(bucketName)){
            minioClient.makeBucket(bucketName);
        }

        File file = new File("/tmp/" + name);
        file.canWrite();
        file.canRead();
        try {
            FileOutputStream iofs = new FileOutputStream(file);
            iofs.write(content);
            minioClient.putObject(bucketName, baseFolder + name, file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public byte[] getFile(String key, String bucketName) {
        try {
            InputStream obj = minioClient.getObject(bucketName, baseFolder + "/" + key);

            byte[] content = IOUtils.toByteArray(obj);
            obj.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostConstruct
    public void init() {
    }

    public Iterable<Result<Item>> getObjects(String bucketName) {
        try {
            return minioClient.listObjects(bucketName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeObject(String bucketName, String objectName) throws InvalidArgumentException, InvalidBucketNameException, InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException, IOException, NoResponseException, InvalidKeyException, InternalException {
        minioClient.removeObject(bucketName, objectName);
    }
}
