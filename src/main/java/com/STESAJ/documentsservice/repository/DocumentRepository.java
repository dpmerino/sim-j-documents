package com.STESAJ.documentsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.STESAJ.documentsservice.entity.Document;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
