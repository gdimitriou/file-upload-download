package com.upload.download.file.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @PostMapping(value = "/upload")
    public String uploadFile(){
        return "upload";
    }

    @GetMapping(value = "/download")
    public String downloadFile(){
        return "download";
    }

}
