package com.STESAJ.documentsservice.service;

import com.STESAJ.documentsservice.controller.DocumentController;
import com.STESAJ.documentsservice.entity.Document;
import com.STESAJ.documentsservice.entity.File;
import com.STESAJ.documentsservice.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService{

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    FileServiceImpl fileService;

    @Override
    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }


    @Override
    public Document getDocument(Long id) throws Exception{
        Document document = documentRepository.findById(id).orElse(null);

        if (null != document){
            List<File> files = fileService.loadAll().map(path -> {
                String filename = path.getFileName().toString();
                String url = MvcUriComponentsBuilder.fromMethodName(DocumentController.class, "getFiles", path.getFileName().toString()).build().toString();
                return new File(filename, url);
            }).collect(Collectors.toList());

            //obtener la posicón (int) de la lista de archivos y relacionarlo con el document (setear el archivo)
            long indice =  document.getDocument_id();
            for (File file: files){
                if (indice == files.indexOf(file) + 1){
                    document.setFile(file);
                }

            }
        }
        return document;
    }
}
