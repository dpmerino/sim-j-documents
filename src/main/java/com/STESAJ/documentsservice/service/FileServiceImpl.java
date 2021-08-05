package com.STESAJ.documentsservice.service;

import com.STESAJ.documentsservice.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    private final Path rootFolder = Paths.get("uploads");
    @Override
    public File save(MultipartFile file, long id) throws IOException{
        File filedoc = new File();
        filedoc.setName(file.getOriginalFilename());
        Files.copy(file.getInputStream(), this.rootFolder.resolve(file.getOriginalFilename()));
        filedoc.setUrl(String.valueOf(UriComponentsBuilder.fromPath(file.getName() + "/" + file.getOriginalFilename()).build().toUri().toString()));

        return filedoc;
    }

    @Override
    public Resource load(String filename) throws Exception{
        Path file = rootFolder.resolve(String.valueOf(filename));
        Resource resource = new UrlResource(file.toUri());
        return resource;
    }

//    @Override
//    public void save(List<MultipartFile> files) throws Exception {
//        for (MultipartFile file: files){
//            this.save(file);
//        }
//    }

    @Override
    public Stream<Path> loadAll() throws Exception {
        return Files.walk(rootFolder, 1).filter(path -> !path.equals(rootFolder)).map(rootFolder::relativize);

    }
}
