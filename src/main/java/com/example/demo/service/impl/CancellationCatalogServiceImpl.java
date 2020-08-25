package com.example.demo.service.impl;

import com.example.demo.entity.CancellationCatalog;
import com.example.demo.repository.CancellationCatalogRepository;
import com.example.demo.service.CancellationCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CancellationCatalogServiceImpl implements CancellationCatalogService {

    @Autowired
    CancellationCatalogRepository cancellationCatalogRepository;

    @Override
    public List<CancellationCatalog> findAll() {
        List<CancellationCatalog> cancellationCatalogList = new ArrayList<CancellationCatalog>();
        try{
            cancellationCatalogList = cancellationCatalogRepository.findAll();
        }catch (Exception e){
            log.error("Error................ "+e.getMessage());
        }
        return cancellationCatalogList;
    }
}
