package com.zlj.oss.controller;

import com.zlj.oss.sevice.OssDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jiang-gege
 * 2019/12/1517:43
 */
@RestController
public class OssDemoController {

    @Autowired
    private OssDemoService service;
//@RequestParam("file")MultipartFile file
    @GetMapping("upload")
    public void uploadImage(){
        service.uploadImage();
    }
}