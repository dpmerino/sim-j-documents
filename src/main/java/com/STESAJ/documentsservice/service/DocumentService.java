package com.STESAJ.documentsservice.service;

import com.STESAJ.documentsservice.entity.Document;

import java.util.List;

public interface DocumentService {
    public List<Document> findAllDocuments();
    public Document createDocument(Document document);
    public Document getDocument(Long id) throws Exception;

}
