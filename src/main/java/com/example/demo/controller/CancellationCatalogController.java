package com.example.demo.controller;

import com.example.demo.service.CancellationCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class CancellationCatalogController {

    @Autowired
    CancellationCatalogService cancellationCatalogService;


    @RequestMapping(value = "/getList", method = {RequestMethod.GET})
    @CrossOrigin
    public Object cancelCatallogList(){
        return cancellationCatalogService.findAll();
    }
}
