package com.example.demo.repository;

import com.example.demo.entity.CancellationCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CancellationCatalogRepository extends JpaRepository<CancellationCatalog, UUID> {

    List<CancellationCatalog> findAll();
}
