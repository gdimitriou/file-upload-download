package com.upload.download.file.controller;

import com.google.api.client.util.Lists;
import com.upload.download.file.minio.MinioAdapter;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controller {

    @Autowired
    MinioAdapter minioAdapter;

    @GetMapping(path = "/buckets")
    public List<Bucket> listBuckets() {
        return minioAdapter.getAllBuckets();
    }

    @PostMapping(path = "/remove/object")
    public void removeObject(
            @RequestParam(value = "bucketName") String bucketName,
            @RequestParam(value = "objectName") String objectName) throws InvalidArgumentException, InvalidBucketNameException, InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException, IOException, NoResponseException, InvalidKeyException, InternalException {
        minioAdapter.removeObject(bucketName, objectName);
    }

    @GetMapping(path = "/objects")
    public List<String> listObjects(@RequestParam(value = "bucketName") String bucketName) {
        Iterable<Result<Item>> list = minioAdapter.getObjects(bucketName);
        List<String> objectNameList = new ArrayList<String>();

        list.iterator().forEachRemaining(itemResult -> {
            try {
                objectNameList.add(itemResult.get().objectName());
            } catch (InvalidBucketNameException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InsufficientDataException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoResponseException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (ErrorResponseException e) {
                e.printStackTrace();
            } catch (InternalException e) {
                e.printStackTrace();
            }
        });
        return objectNameList;
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> uploadFile(
            @RequestPart(value = "file", required = false) MultipartFile files,
            @RequestParam(value = "bucketName") String bucketName) throws IOException, RegionConflictException, InvalidBucketNameException, InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException, NoResponseException, InvalidKeyException, InternalException {

        minioAdapter.uploadFile(files.getOriginalFilename(), files.getBytes(), bucketName);
        Map<String, String> result = new HashMap<>();
        result.put("key", files.getOriginalFilename());
        return result;
    }

    @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
        byte[] data = minioAdapter.getFile(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                .body(resource);

    }
}
