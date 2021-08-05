package com.STESAJ.documentsservice.service;

import com.STESAJ.documentsservice.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;


public interface FileService {
    public File save(MultipartFile file, long id) throws Exception;
    public Resource load(String id) throws Exception;
//    public void save(List<MultipartFile> files) throws Exception ;
    public Stream<Path> loadAll() throws Exception;
    //public  Document deleteDocument(Long id);
}
