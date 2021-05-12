package com.upload.download.file.controller;

import com.upload.download.file.minio.MinioAdapter;
import io.minio.errors.*;
import io.minio.messages.Bucket;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    MinioAdapter minioAdapter;

    @GetMapping(path = "/buckets")
    public List<Bucket> listBuckets() {
        return minioAdapter.getAllBuckets();
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException, RegionConflictException, InvalidBucketNameException, InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException, NoResponseException, InvalidKeyException, InternalException {
        minioAdapter.uploadFile(files.getOriginalFilename(), files.getBytes());
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
